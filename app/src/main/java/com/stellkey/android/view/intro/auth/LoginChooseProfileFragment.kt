package com.stellkey.android.view.intro.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentLoginChooseProfileBinding
import com.stellkey.android.model.AllProfileModel
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.intro.auth.adapter.AllProfileAdapter
import org.koin.android.ext.android.inject

class LoginChooseProfileFragment : BaseFragment(), AllProfileAdapter.Listener {

    private lateinit var dataBinding: FragmentLoginChooseProfileBinding
    private val viewModel by inject<LoginViewModel>()

    companion object {
        @JvmStatic
        fun newInstance() = LoginChooseProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_login_choose_profile,
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

            allProfileSelection.observe(viewLifecycleOwner) {
                setProfileList(it.dbResult.family)
            }
        }

        setView()
    }

    private fun setProfileList(profileList: ArrayList<AllProfileModel.Family>) {
        dataBinding.apply {
            rvProfile.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = AllProfileAdapter(context, profileList, this@LoginChooseProfileFragment)
            }
        }
    }

    override fun onItemClicked(data: AllProfileModel.Family) {
        if (data.profileIcon.type == Constant.ProfileIconType.PROFILE_ICON_CARER) {
            AppPreference.putCarerToken(data.loginToken)
            AppPreference.putKidLogin(false)
            AppPreference.putLoggedInCarerName(data.name)
            LoginPINFragment.profileType = Constant.ProfileIconType.PROFILE_ICON_CARER
            LoginPINFragment.selectedId = data.id
        } else {
            AppPreference.putKidToken(data.loginToken)
            AppPreference.putKidLogin(true)
            LoginPINFragment.profileType = Constant.ProfileIconType.PROFILE_ICON_KID
            LoginPINFragment.selectedId = data.id
        }
        addFragment(LoginPINFragment.newInstance())
    }

    private fun setView() {
        viewModel.getAllProfileSelection(AppPreference.getMainCarerLoginToken())
    }
}