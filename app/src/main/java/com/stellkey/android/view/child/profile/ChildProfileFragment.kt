package com.stellkey.android.view.child.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentChildProfileBinding
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.KidInfoModel
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.child.ChildMainAct
import com.stellkey.android.view.child.ChildViewModel
import com.stellkey.android.view.intro.intro.IntroAct
import org.koin.android.ext.android.inject

class ChildProfileFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentChildProfileBinding

    //private val binding by viewBinding<FragmentChildProfileBinding>()
    private val viewModel by inject<ChildViewModel>()

    companion object {
        @JvmStatic
        fun newInstance() = ChildProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_child_profile, container, false)
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

            kidInfoResponse.observe(viewLifecycleOwner) {
                it?.let { it1 -> setKidView(it1) }
            }

        }

        (activity as ChildMainAct).showMenu(isShow = false)
        setView()
        setOnClick()
    }

    private fun setView() {
        if (AppPreference.isUpdateLocale()) {
            AppPreference.putUpdateLocale(false)
            addFragment(ChildSettingsFragment.newInstance())
        }

        onBackPressed()
        viewModel.getKidInfo()
    }

    private fun setKidView(data: KidInfoModel) {
        dataBinding.apply {
            ivAvatar.loadImage(
                data.profileIcon.icon,
                ImageCornerOptions.ROUNDED,
                100
            )
            tvChildName.textOrNull = data.name
            tvChildLevel.textOrNull = requireContext().resources.getString(
                R.string.kid_profile_level,
                data.level.level
            )
            tvChildLevelName.textOrNull = data.level.levelName
            tvChildLevelProgress.textOrNull = requireContext().resources.getString(
                R.string.kid_profile_level_progress,
                100 - data.level.percentageToNextLevel,
                100
            )
            piProfileProgress.progress = 100 - data.level.percentageToNextLevel
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
            ivSetting.setOnClickListener(onClickCallback)
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            (activity as ChildMainAct).showMenu(isShow = true)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                (activity as ChildMainAct).showMenu(isShow = true)
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.ivSetting -> {
                addFragment(ChildSettingsFragment.newInstance())
            }
        }
    }

}