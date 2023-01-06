package com.stellkey.android.view.carer.family.addkid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogCancelAddMemberBinding
import com.stellkey.android.databinding.FragmentAddKidFirstTaskBinding
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.helper.extension.alertDialog
import com.stellkey.android.helper.extension.emptyInt
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.KidGlobalChallengeModel
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.intro.auth.adapter.GlobalChallengeAdapter
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.family.FamilyFragment
import com.stellkey.android.view.carer.family.FamilyViewModel
import kotlinx.android.synthetic.main.fragment_register_kids_challenge.*
import org.koin.android.ext.android.inject

class AddKidFirstTaskFragment : BaseFragment(), GlobalChallengeAdapter.Listener {

    private lateinit var dataBinding: FragmentAddKidFirstTaskBinding
    private val viewModel by inject<FamilyViewModel>()

    private var selectedChallengeId = emptyInt
    private lateinit var dialogCancelBinding: DialogCancelAddMemberBinding
    private lateinit var cancelDialog: AlertDialog

    companion object {
        @JvmStatic
        fun newInstance() = AddKidFirstTaskFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_kid_first_task,
                container,
                false
            )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this.viewLifecycleOwner

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
            listGlobalChallenges.observe(viewLifecycleOwner) {
                setGlobalChallenges(it.orEmpty().toArrayList())
            }
        }

        setView()
        setOnClick()
    }

    private fun setGlobalChallenges(list: ArrayList<KidGlobalChallengeModel>) {
        val globalChallenges = arrayListOf<KidGlobalChallengeModel>()
        list.forEach {
            globalChallenges.add(it)
        }

        rvKidChallenge.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = GlobalChallengeAdapter(
                context,
                globalChallenges,
                this@AddKidFirstTaskFragment
            )
        }
    }

    override fun onItemClicked(data: KidGlobalChallengeModel) {
        selectedChallengeId = if (data.isSelected)
            data.id
        else
            emptyInt

        validateForm()
    }

    private fun setView() {
        validateForm()
        dataBinding.tvTitle.textOrNull = requireContext().resources.getString(
            R.string.register_child_challenge_title,
            AppPreference.getTempChildName()
        )

        viewModel.getGlobalChallenges(age = AppPreference.getTempChildAge())
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

    private fun validateForm() {
        dataBinding.apply {
            if (selectedChallengeId != emptyInt) {
                btnNext.apply {
                    isEnabled = true
                    isClickable = true
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    setOnClickListener(onClickCallback)
                }
            } else
                btnNext.apply {
                    isEnabled = false
                    isClickable = false
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.primary_disabled
                        )
                    )
                    setOnClickListener(null)
                }
        }
    }

    private fun initCancelDialog() {
        dialogCancelBinding = DialogCancelAddMemberBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        cancelDialog = requireActivity().alertDialog(
            view = dialogCancelBinding.root,
            isCancelable = false,
            fullScreen = false
        )
        cancelDialog.window?.setBackgroundDrawableResource(R.color.transparent)

        dialogCancelBinding.tvDesc.textOrNull =
            requireContext().getString(R.string.dialog_add_member_cancel_desc_kid)

        dialogCancelBinding.btnCancel.setOnClickListener {
            AppPreference.deleteTempCreateMemberData()

            requireActivity().supportFragmentManager.fragments.let {
                for (fragment in requireActivity().supportFragmentManager.fragments) {
                    requireActivity().supportFragmentManager.beginTransaction().remove(fragment)
                        .commit()
                }
                requireActivity().supportFragmentManager.popBackStack(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            }
            cancelDialog.dismiss()
            (activity as HomeAct).showMenu(isShow = true)
            addFragment(FamilyFragment.newInstance())
        }

        dialogCancelBinding.btnContinue.setOnClickListener {
            cancelDialog.dismiss()
        }

        cancelDialog.show()
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
            tvCancel.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.btnNext -> {
                AppPreference.putTempSelectedGlobalChallengeId(selectedChallengeId)
                addFragment(AddFirstTaskStartDateFragment.newInstance())
            }
            dataBinding.tvCancel -> {
                initCancelDialog()
            }
        }
    }

}