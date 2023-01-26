package com.stellkey.android.view.child.pet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.airbnb.lottie.LottieDrawable
import com.stellkey.android.R
import com.stellkey.android.constant.PetTheme
import com.stellkey.android.databinding.FragmentChildPetBinding
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.KidInfoModel
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.child.ChildMainAct
import com.stellkey.android.view.child.ChildViewModel
import com.stellkey.android.view.child.pet.dialog.AccessoriesPickerFragment
import com.stellkey.android.view.intro.auth.LoginChooseProfileFragment
import org.koin.android.ext.android.inject

class ChildPetFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentChildPetBinding
    private val viewModel by inject<ChildViewModel>()
    private val petThemeColor = PetTheme(AppPreference.getKidPetColorTheme())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_child_pet, container, false)
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

            kidInfoResponse.observe(viewLifecycleOwner) {
                it?.let { it1 -> setKidView(it1) }
            }

            kidTapThePetResponse.observe(viewLifecycleOwner) {
                updateHappiness(it.happiness)
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
        }
        updateHappiness(data.pet.happiness)
        updateEat(data.pet.hunger)
    }

    private fun setOnClick() {
        dataBinding.cvSwitchUser.setOnClickListener {
            (activity as ChildMainAct).showMenu(isShow = false)
            addFragment(LoginChooseProfileFragment.newInstance())
        }

        dataBinding.lottiePet.setOnClickListener {
            dataBinding.lottiePet.apply {
                setAnimation(petThemeColor.gigglePose)
                repeatCount = LottieDrawable.INFINITE
                playAnimation()
            }
        }

        dataBinding.ivPetAccessories.setOnClickListener {
            AccessoriesPickerFragment().show(childFragmentManager, AccessoriesPickerFragment.TAG)
        }
    }

    /**
     * Giggle-Pose = happiness x% - x%, hungry x% - x% ||  When Clicked Pet
     * Hungry-Pose = happiness > 50, hungry < 50
     * Normal-Pose = happiness >= 50, hungry >= 50
     * Yummy-Pose = happiness >50, hungry >= 50
     * Angry-Pose = happingess < 50, hungry < 50
     * Happy-Pose = happiness >= 75, hungry >= 75
     **/
    private fun updatePetTheme() {

    }

    private fun updateHappiness(happiness: Int) {
        when (happiness) {
            in 0..25 -> {

            }

            in 26..50 -> {

            }

            in 51..75 -> {

            }

            in 76..100 -> {

            }
        }
    }

    private fun updateEat(hunger: Int) {
        when (hunger) {
            in 0..25 -> {

            }

            in 26..50 -> {

            }

            in 51..75 -> {

            }

            in 76..100 -> {

            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ChildPetFragment()
    }
}