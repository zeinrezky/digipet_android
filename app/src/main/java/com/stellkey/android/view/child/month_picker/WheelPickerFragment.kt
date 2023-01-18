package com.stellkey.android.view.child.month_picker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.shawnlin.numberpicker.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentWheelPickerBinding
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.child.ChildMainAct
import org.greenrobot.eventbus.EventBus

class WheelPickerFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentWheelPickerBinding
    private val listDate = arrayListOf(
        "Jan 2022",
        "Feb 2022",
        "Mar 2022",
        "Apr 2022",
        "May 2022",
        "Jun 2022",
        "Jul 2022",
        "Aug 2022",
        "Sep 2022",
        "Oct 2022",
        "Nov 2022",
        "Dec 2022",
        "Jan 2023"
    )

    private var selectedIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_wheel_picker, container, false)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.lifecycleOwner = this

        setView()
        setOnClick()
    }

    private fun setView() {
        onBackPressed()

        dataBinding.apply {
            // for selected item
            datePicker.setSelectedTextSize(R.dimen.space_x6)
            datePicker.selectedTextColor =
                ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            datePicker.setSelectedTypeface(ResourcesCompat.getFont(requireContext(), R.font.nove))

            // for unselected item
            datePicker.setTextSize(R.dimen.space_x3)
            datePicker.textColor =
                ContextCompat.getColor(requireContext(), R.color.colorFadePrimary)
            datePicker.typeface = ResourcesCompat.getFont(requireContext(), R.font.nove)

            //divider
            datePicker.dividerColor = ContextCompat.getColor(requireContext(), R.color.transparent)

            //set value
            datePicker.minValue = 1
            datePicker.maxValue = listDate.size
            datePicker.displayedValues = listDate.toTypedArray()
            datePicker.value = 1

            datePicker.isFadingEdgeEnabled = true
            datePicker.isScrollerEnabled = true

            datePicker.wheelItemCount = 5

            datePicker.setOnScrollListener { picker, scrollState ->
                if (scrollState == SCROLL_STATE_IDLE) {
                    selectedIndex = picker.value
                }
            }
        }
    }

    private fun setOnClick() {
        dataBinding.ivDoneBtn.setOnClickListener {
            val selectedMonth = if (selectedIndex > 12) 1 else selectedIndex
            val selectedYear = if (selectedIndex > 12) 2023 else 2022
            EventBus.getDefault().post(
                ChildMainAct.NavigationChildHomeEvent(
                    Constant.KidWidget.DIALOG_WHEEL_DISMISSED,
                    extraParams = hashMapOf(
                        Constant.ExtraParams.MONTH_WHEEL_PICKER to selectedMonth.toString(),
                        Constant.ExtraParams.YEAR_WHEEL_PICKER to selectedYear.toString()
                    )
                )
            )
        }
        dataBinding.ivClose.setOnClickListener {
            EventBus.getDefault().post(ChildMainAct.NavigationChildHomeEvent(Constant.KidMenu.LOG))
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {}
    }

    companion object {
        @JvmStatic
        fun newInstance() = WheelPickerFragment()
    }
}