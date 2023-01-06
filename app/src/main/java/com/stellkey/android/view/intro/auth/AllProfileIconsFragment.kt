package com.stellkey.android.view.intro.auth

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogInfoBinding
import com.stellkey.android.databinding.FragmentAllProfileIconsBinding
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.helper.extension.color
import com.stellkey.android.helper.extension.emptyString
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.ProfileIconModel
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.view.intro.auth.adapter.AllProfileIconAdapter
import com.stellkey.android.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_all_profile_icons.*
import org.koin.android.ext.android.inject

class AllProfileIconsFragment : BaseFragment(), AllProfileIconAdapter.Listener {

    private lateinit var dataBinding: FragmentAllProfileIconsBinding
    private val viewModel by inject<RegisterViewModel>()

    companion object {
        @JvmStatic
        fun newInstance() = AllProfileIconsFragment()
        var profileIconType = emptyString
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_all_profile_icons, container, false)
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
            listProfileIcons.observe(viewLifecycleOwner) {
                setProfileIconsList(it.orEmpty().toArrayList())
            }
        }

        setView()
        setOnClick()
    }

    private fun setProfileIconsList(list: ArrayList<ProfileIconModel.ProfileIconModelData>) {
        val profileIcons = arrayListOf<ProfileIconModel.ProfileIconModelData>()
        when (profileIconType) {
            Constant.ProfileIconType.PROFILE_ICON_CARER -> {
                list.forEach {
                    if (it.type == "carer")
                        profileIcons.add(it)
                }
            }
            Constant.ProfileIconType.PROFILE_ICON_KID -> {
                list.forEach {
                    if (it.type == "kid")
                        profileIcons.add(it)
                }
            }
        }

        rvProfileIcons.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = AllProfileIconAdapter(context, profileIcons, this@AllProfileIconsFragment)
        }
    }

    private fun setView() {
        viewModel.getAllProfileIcons()
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

    override fun onItemClicked(data: ProfileIconModel.ProfileIconModelData) {
        AppPreference.putProfileIconId(data.id)
        AppPreference.putProfileIcon(data.icon)
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }
}