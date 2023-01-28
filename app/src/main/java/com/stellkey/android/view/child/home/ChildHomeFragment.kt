package com.stellkey.android.view.child.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.stellkey.android.R
import com.stellkey.android.constant.PetEmotion
import com.stellkey.android.constant.PetTheme
import com.stellkey.android.databinding.FragmentChildHomeBinding
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.helper.extension.loadFromUrl
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
import com.stellkey.android.view.child.home.dialog.TaskCompletedDialog
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
    private var petThemeColor = PetTheme(AppPreference.getKidPetColorTheme())

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
                showTaskCompletedDialog()
            }

            kidSuccessRemindCareerForAssignment.observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.kid_home_remind_parent_message),
                    Toast.LENGTH_LONG
                ).show()
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
        updateHappinessAndHungry(data.pet.happiness, data.pet.hunger)
    }

    override fun onTodayTaskItemClicked(data: AssignmentsModel) {
        viewModel.postCompleteKidTask(KidCompleteTaskRequest(data.id))
    }

    override fun onTodayTaskItemReminderClicked(data: AssignmentsModel) {
        viewModel.postKidRemindCareerAssignment(data.id)
    }

    private fun setOnClick() {
        dataBinding.apply {
            cvSwitchUser.setOnClickListener(onClickCallback)
            clPet.setOnClickListener(onClickCallback)
            clKidLevel.setOnClickListener(onClickCallback)
        }

        dataBinding.viewPetAnimation.lottiePet.setOnClickListener {
            viewModel.postKidTapThePet()
            dataBinding.viewPetAnimation.lottiePet.apply {
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
        dataBinding.viewPetAnimation.lottiePet.apply {
            setAnimation(petThemeColor.normalPose)
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
        }
    }

    /**
     * Giggle-Pose = happiness x% - x%, hungry x% - x% ||  When Clicked Pet
     * Hungry-Pose = happiness > 50, hungry < 50
     * Normal-Pose = happiness >= 50, hungry >= 50
     * Yummy-Pose = happiness >50, hungry >= 50 [If there are attachment for food]
     * Angry-Pose = happingess < 50, hungry < 50
     * Happy-Pose = happiness >= 75, hungry >= 75
     **/

    private fun updateHappinessAndHungry(happiness: Int, hunger: Int) {
        if (happiness > 50 && hunger < 50) {
            // Hungry
            AppPreference.putPetCurrentEmotion(PetEmotion.PET_EMOTION_HUNGRY)
        }
        if (happiness in 50..75 && hunger in 51..75) {
            //Normal
            AppPreference.putPetCurrentEmotion(PetEmotion.PET_EMOTION_NORMAL)
        }
        if (happiness > 50 && hunger >= 50 && AppPreference.getKidPetFoodAssignment()
                .isNotEmpty()
        ) {
            //Yummy
            AppPreference.putPetCurrentEmotion(PetEmotion.PET_EMOTION_YUMMY)
        }
        if (happiness < 50 && hunger < 50) {
            //Angry
            AppPreference.putPetCurrentEmotion(PetEmotion.PET_EMOTION_ANGRY)
        }
        if (happiness in 75..100 && hunger in 75..100) {
            //Happy
            AppPreference.putPetCurrentEmotion(PetEmotion.PET_EMOTION_HAPPY)
        }

        updatePetTheme()
    }


    private fun showTaskCompletedDialog() {
        val taskCompletedparams = TaskCompletedDialog.TaskCompletedParams(
            subtitleText = getString(R.string.kid_home_task_completed_subtitle),
            starSum = "X1",
            gemSum = "X1"
        )
        TaskCompletedDialog(
            onButtonClicked = {
                viewModel.getKidInfo()
            }
        ).apply {
            arguments = Bundle().apply {
                putParcelable(TaskCompletedDialog.TASK_COMPLETED_PARAMS, taskCompletedparams)
            }
        }.show(childFragmentManager, TaskCompletedDialog.TAG)
    }

    private fun updatePetTheme() {
        petThemeColor = PetTheme(AppPreference.getKidPetColorTheme())
        dataBinding.viewPetAnimation.ivFoodUsing.loadFromUrl(AppPreference.getKidPetFoodAssignment())
        dataBinding.viewPetAnimation.ivPetDecorUsing.loadFromUrl(AppPreference.getKidPetDecorAssignment())
        dataBinding.viewPetAnimation.lottiePet.apply {
            when (AppPreference.getPetCurrentEmotion()) {
                PetEmotion.PET_EMOTION_ANGRY -> {
                    setAnimation(petThemeColor.angryPose)
                }

                PetEmotion.PET_EMOTION_HAPPY -> {
                    setAnimation(petThemeColor.happyPose)
                }

                PetEmotion.PET_EMOTION_HUNGRY -> {
                    setAnimation(petThemeColor.hungryPose)
                }

                PetEmotion.PET_EMOTION_YUMMY -> {
                    setAnimation(petThemeColor.yummyPose)
                }

                PetEmotion.PET_EMOTION_NORMAL -> {
                    setAnimation(petThemeColor.normalPose)
                }

                PetEmotion.PET_EMOTION_GIGGLE -> {
                    setAnimation(petThemeColor.gigglePose)
                }
            }
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
        }
    }
}