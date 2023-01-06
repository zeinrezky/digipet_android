package com.stellkey.android.view.carer.log

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogInfoBinding
import com.stellkey.android.databinding.FragmentLogBinding
import com.stellkey.android.helper.extension.color
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.CarerLogModel
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.intro.intro.IntroAct
import kotlinx.android.synthetic.main.fragment_all_profile_icons.*
import org.koin.android.ext.android.inject

class LogFragment : BaseFragment(), AdapterView.OnItemSelectedListener {

    private lateinit var dataBinding: FragmentLogBinding

    //private val binding by viewBinding<FragmentLogBinding>()
    private val viewModel by inject<LogViewModel>()

    private lateinit var carerLogAdapter: LogAdapter

    var logTypes = arrayOf("Show all", "Tasks", "Rewards")

    companion object {
        @JvmStatic
        fun newInstance() = LogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_log,
                container,
                false
            )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this

        with(viewModel) {
            isLoading.observe(viewLifecycleOwner) { bool ->
                bool.let { loading ->
                    if (loading) {
                        showWaitingDialog()
                    } else {
                        hideWaitingDialog()
                    }
                }
            }

            responseError.observe(viewLifecycleOwner) {
                AppPreference.deleteAll()
                val intent = Intent(context, IntroAct::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                requireActivity().finish()
            }

            listLog.observe(viewLifecycleOwner) {
                setLogList(it)
            }

        }

        setView()
        setOnClick()
    }

    private fun setView() {
        viewModel.getCarerLog(type = null)
        dataBinding.apply {
            val logArrayAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, logTypes)
            logArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            with(spinnerCarerLog)
            {
                adapter = logArrayAdapter
                setSelection(0, false)
                onItemSelectedListener = this@LogFragment
                prompt = "Select Log Type"
                gravity = Gravity.CENTER

            }
        }
    }

    private fun setLogList(logResponse: ArrayList<CarerLogModel>) {
        dataBinding.apply {
            if (logResponse.isEmpty()) {
                rvCarerLog.isVisible = false
            } else {
                rvCarerLog.isVisible = true

                carerLogAdapter = LogAdapter(
                    requireContext(),
                    logResponse
                )

                rvCarerLog.apply {
                    layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter = carerLogAdapter
                }
            }
        }
    }

    /*private fun initInfoDialog(textTitle: String, textDesc: String) {
        dialogInfoBinding = DialogInfoBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        val customSnackBar =
            Snackbar.make(dataBinding.clFamilyMainContainer, "", Snackbar.LENGTH_LONG)
        val layout = customSnackBar.view as Snackbar.SnackbarLayout

        dialogInfoBinding.apply {
            tvTitle.textOrNull(textTitle)
            tvDesc.textOrNull(textDesc)
            clInfo.setOnClickListener {
                customSnackBar.dismiss()
            }
        }

        val view: View = customSnackBar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params

        layout.setPadding(0, 0, 0, 0)
        layout.setBackgroundColor(context.color(R.color.transparent))
        layout.elevation = 0F
        layout.addView(dialogInfoBinding.root, 0)
        customSnackBar.show()
    }*/

    private fun setOnClick() {
        dataBinding.apply {

        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val type = parent?.getItemAtPosition(position).toString()
        when (type) {
            "Show all" -> {
                viewModel.getCarerLog(type = null)
            }
            "Tasks" -> {
                viewModel.getCarerLog(type = "tasks")
            }
            "Rewards" -> {
                viewModel.getCarerLog(type = "rewards")
            }
            else -> {}
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}