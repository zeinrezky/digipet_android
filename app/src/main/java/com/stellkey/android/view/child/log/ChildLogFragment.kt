package com.stellkey.android.view.child.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentChildLogBinding
import com.stellkey.android.helper.extension.formatDate
import com.stellkey.android.helper.extension.getCurrentDate
import com.stellkey.android.helper.extension.getCurrentDayMonth
import com.stellkey.android.helper.extension.getCurrentDayYear
import com.stellkey.android.helper.extension.onBackBlockPressed
import com.stellkey.android.model.KidLogModel
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.child.ChildMainAct
import com.stellkey.android.view.child.ChildViewModel
import com.stellkey.android.view.child.log.adapter.ChildLogAdapter
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject

class ChildLogFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentChildLogBinding
    private val viewModel by inject<ChildViewModel>()
    private val childLogAdapter by lazy {
        ChildLogAdapter(arrayListOf())
    }
    private var DEFAULT_MONTH = getCurrentDayMonth().toInt()
    private var DEFAULT_YEAR = getCurrentDayYear().toInt()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_child_log, container, false)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this

        with(viewModel) {
            isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    showWaitingDialog()
                } else {
                    hideWaitingDialog()
                }
            }

            kidLogsResponse.observe(viewLifecycleOwner) {
                setKidLogsData(it)
            }
        }

        setView()
        setOnClick()
    }

    private fun setView() {
        onBackBlockPressed()
        setRecyclerviewLogs()
        dataBinding.tvMonthSelected.text = getCurrentDayMonth("MMM YYYY")

        viewModel.getKidLogs(
            month = getCurrentDayMonth().toInt(),
            year = getCurrentDayYear().toInt()
        )
    }

    private fun setOnClick() {
        dataBinding.apply {
            tvMonthSelected.setOnClickListener(onClickCallback)
            btnLeftMonth.setOnClickListener(onClickCallback)
            btnRightMonth.setOnClickListener(onClickCallback)
        }
    }

    private fun setRecyclerviewLogs() {
        dataBinding.rvListLog.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = childLogAdapter
        }
    }

    private fun setKidLogsData(data: List<KidLogModel>) {
        val listKidLogsAfterGroupByDate = arrayListOf<Pair<String, List<KidLogModel>>>()
        data.groupBy {
            it.createdAt.formatDate(
                from = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                to = "dd MMM"
            )
        }.forEach {
            listKidLogsAfterGroupByDate.add(
                Pair(it.key, it.value)
            )
        }
        childLogAdapter.listLogs = listKidLogsAfterGroupByDate
        childLogAdapter.notifyDataSetChanged()
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.tvMonthSelected -> {
                EventBus.getDefault()
                    .post(ChildMainAct.NavigationChildHomeEvent(Constant.KidWidget.DIALOG_WHEEL_PICKER))
            }

            dataBinding.btnLeftMonth -> {

            }

            dataBinding.btnRightMonth -> {

            }
        }
    }

    companion object {
        private var SELECTED_MONTH: Int? = null
        private var SELECTED_YEAR: Int? = null

        @JvmStatic
        fun newInstance(selectedMonth: Int? = null, selectedYear: Int? = null) =
            ChildLogFragment().apply {
                selectedMonth?.let {
                    SELECTED_MONTH = it
                }
                selectedYear?.let {
                    SELECTED_YEAR = it
                }
            }
    }
}