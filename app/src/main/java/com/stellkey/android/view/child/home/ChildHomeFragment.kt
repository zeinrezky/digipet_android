package com.stellkey.android.view.child.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.stellkey.android.R
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
        setOnClick()
    }

    private fun setView() {
        viewModel.getKidInfo()
    }

    private fun setKidView(data: KidInfoModel) {
        dataBinding.apply {
            tvChildLevel.textOrNull = data.level.level.toString()
            piChildLevel.progress = 100 - data.level.percentageToNextLevel
            tvTodayTaskStar.text = data.level.starsTotal.toString()

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

}