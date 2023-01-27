package com.stellkey.android.view.child.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.stellkey.android.R
import com.stellkey.android.constant.PetTheme
import com.stellkey.android.databinding.FragmentChildHomeBinding
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AssignmentsModel
import com.stellkey.android.model.KidInfoModel
import com.stellkey.android.model.TaskStarModel
import com.stellkey.android.model.request.KidCompleteTaskRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.child.ChildMainAct
import com.stellkey.android.view.child.ChildViewModel
import com.stellkey.android.view.child.home.adapter.KidTodayTaskAdapter
import com.stellkey.android.view.child.home.adapter.MyProgressAdapter
import com.stellkey.android.view.child.pet.ChildPetFragment
import com.stellkey.android.view.child.profile.ChildProfileFragment
import com.stellkey.android.view.intro.auth.LoginChooseProfileFragment
import com.stellkey.android.view.intro.intro.IntroAct
import org.koin.android.ext.android.inject

class ChildHomeFragment : BaseFragment(), KidTodayTaskAdapter.Listener {

    private lateinit var dataBinding: FragmentChildHomeBinding
    private lateinit var kidTodayTaskAdapter: KidTodayTaskAdapter
    private lateinit var myProgressTaskAdapter: MyProgressAdapter
    private val viewModel by inject<ChildViewModel>()
    private val petThemeColor = PetTheme(AppPreference.getKidPetColorTheme())

    companion object {

        @JvmStatic
        fun newInstance() = ChildHomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_child_home, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this

        with(viewModel) {
            responseError.observe(viewLifecycleOwner) {
                AppPreference.deleteAll()
                val intent = Intent(context, IntroAct::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                requireActivity().finish()
            }

            kidInfoResponse.observe(viewLifecycleOwner) {
                it?.let { it1 -> setKidView(it1) }
            }

            completeKidTaskResponse.observe(viewLifecycleOwner) {
                viewModel.getKidInfo()
            }

        }

        setView()
        initAnimation()
        setOnClick()
    }

    private fun setView() {
        viewModel.getKidInfo()
    }

    private fun setKidView(data: KidInfoModel) {
        dataBinding.apply {
            tvChildLevel.textOrNull = data.level.level.toString()
            piChildLevel.progress = 100 - data.level.percentageToNextLevel
            tvTodayTaskStar.text = "${data.level.starsTotal - data.starsSpent}"
            val availableForGem = (data.tasksCompleted ?: 0) - (data.rubiesSpent ?: 0)
            tvTodayTaskDiamond.text = availableForGem.toString()

            val groupedActiveTask = mutableListOf<Pair<AssignmentsModel, List<TaskStarModel>>>()
            //for global tasks
            data.activeTasks.assignments.groupBy { it.globalChallengeId }.forEach {
                if (it.key != null) {
                    val listStarTask = it.value.map { assignment ->
                        TaskStarModel(isCompleted = assignment.completedAt != null)
                    }
                    groupedActiveTask.add(Pair(it.value.first(), listStarTask))
                }
            }

            //for custom tasks
            data.activeTasks.assignments.groupBy { it.challengeId }.forEach {
                if (it.key != null) {
                    val listStarTask = it.value.map { assignment ->
                        TaskStarModel(isCompleted = assignment.completedAt != null)
                    }
                    groupedActiveTask.add(Pair(it.value.first(), listStarTask))
                }
            }

            kidTodayTaskAdapter = KidTodayTaskAdapter(
                requireContext(),
                data.tasksToday.assignments,
                this@ChildHomeFragment
            )

            myProgressTaskAdapter = MyProgressAdapter(
                groupedActiveTask.toArrayList()
            )

            rvTodayTask.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = kidTodayTaskAdapter
            }

            rvKidProgress.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = myProgressTaskAdapter
            }
        }
    }

    override fun onTodayTaskItemClicked(data: AssignmentsModel) {
        viewModel.postCompleteKidTask(KidCompleteTaskRequest(data.id))
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            cvSwitchUser.setOnClickListener(onClickCallback)
            clPet.setOnClickListener(onClickCallback)
            clKidLevel.setOnClickListener(onClickCallback)
        }

        dataBinding.lottiePet.setOnClickListener {
            dataBinding.lottiePet.apply {
                setAnimation(petThemeColor.gigglePose)
                repeatCount = LottieDrawable.INFINITE
                playAnimation()
            }
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.cvSwitchUser -> {
                (activity as ChildMainAct).showMenu(isShow = false)
                addFragment(LoginChooseProfileFragment.newInstance())
            }

            dataBinding.clPet -> {
                addFragment(ChildPetFragment.newInstance())
            }

            dataBinding.clKidLevel -> {
                addFragment(ChildProfileFragment.newInstance())
            }
        }
    }

    private fun initAnimation() {
        dataBinding.lottiePet.apply {
            setAnimation(petThemeColor.normalPose)
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
        }
    }

    /**
     * Giggle-Pose = happiness x% - x%, hungry x% - x% ||  When Clicked Pet
     * Hungry-Pose = happiness > 50, hungry < 50
     * Normal-Pose = happiness >= 50, hungry >= 50
     * Yummy-Pose = happiness >50, hungry >= 50
     * Angry-Pose = happingess < 50, hungry < 50
     * Happy-Pose = happiness >= 75, hungry >= 75
     **/

    private fun updatePetTheme() {

    }

    private fun updateHappiness(happiness: Int) {
        when (happiness) {
            in 0..25 -> {

            }

            in 26..50 -> {

            }

            in 51..75 -> {

            }

            in 76..100 -> {

            }
        }
    }

    private fun updateEat(hunger: Int) {
        when (hunger) {
            in 0..25 -> {

            }

            in 26..50 -> {

            }

            in 51..75 -> {

            }

            in 76..100 -> {

            }
        }
    }
}