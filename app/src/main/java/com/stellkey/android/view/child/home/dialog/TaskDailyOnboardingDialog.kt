package com.stellkey.android.view.child.home.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogKidOnboardingDailyTaskBinding
import com.stellkey.android.model.KidInfoModel
import com.stellkey.android.model.request.KidCompleteTaskRequest
import com.stellkey.android.view.child.ChildViewModel
import com.stellkey.android.view.child.home.adapter.KidOnboardingDailyAdapter
import org.koin.android.ext.android.inject


class TaskDailyOnboardingDialog(val onCloseClickListener: () -> Unit = {}) : DialogFragment() {

    private lateinit var dataBinding: DialogKidOnboardingDailyTaskBinding

    private val viewModel by inject<ChildViewModel>()

    private val kidOnboardingDailyAdapter by lazy {
        KidOnboardingDailyAdapter(
            arrayListOf(),
            onItemClick = {
                viewModel.postCompleteKidTask(KidCompleteTaskRequest(it.id))
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_kid_onboarding_daily_task,
            container,
            false
        )
        dialog?.let {
            it.window?.requestFeature(Window.FEATURE_NO_TITLE)
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            completeKidTaskResponse.observe(viewLifecycleOwner) {
                viewModel.getKidInfo()
            }

            kidInfoResponse.observe(viewLifecycleOwner) {
                it?.let { kidInfoModel -> mapKidInfoModel(kidInfoModel) }
            }
        }

        setupView()
        setupRecyclerview()

        viewModel.getKidInfo()
    }

    override fun onStart() {
        super.onStart()
        dialog ?: return
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    private fun setupView() {
        dataBinding.ivClose.setOnClickListener {
            onCloseClickListener.invoke()
            dismiss()
        }
    }

    private fun setupRecyclerview() {
        dataBinding.rvDailyTask.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = kidOnboardingDailyAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun mapKidInfoModel(kidModel: KidInfoModel) {
        val listAssignments =
            kidModel.tasksToday.assignments.filter { it.completedAt == null && it.confirmedAt == null }
        dataBinding.apply {
            tvTitle.text = "Hi ${kidModel.name},"
            tvSubtitle.text =
                getString(R.string.kid_subtitle_daily_task, listAssignments.size)
        }
        kidOnboardingDailyAdapter.listDailyTask = listAssignments
        kidOnboardingDailyAdapter.notifyDataSetChanged()
    }

    companion object {
        const val TAG = "TaskDailyOnboardingDialog"
    }
}