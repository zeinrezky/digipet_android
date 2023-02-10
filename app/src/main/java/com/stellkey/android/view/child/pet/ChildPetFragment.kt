package com.stellkey.android.view.child.pet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.airbnb.lottie.LottieDrawable
import com.stellkey.android.R
import com.stellkey.android.constant.PetEmotion
import com.stellkey.android.constant.PetTheme
import com.stellkey.android.databinding.FragmentChildPetBinding
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.helper.extension.loadFromUrl
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.KidInfoModel
import com.stellkey.android.model.PetModel
import com.stellkey.android.model.PetStore
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.child.ChildMainAct
import com.stellkey.android.view.child.ChildViewModel
import com.stellkey.android.view.child.pet.dialog.PetstorePickerFragment
import com.stellkey.android.view.child.pet.dialog.PetStoreData
import com.stellkey.android.view.intro.auth.LoginChooseProfileFragment
import org.koin.android.ext.android.inject

class ChildPetFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentChildPetBinding
    private val viewModel by inject<ChildViewModel>()
    private var petThemeColor = PetTheme(AppPreference.getKidPetColorTheme())
    private var petStoreList = arrayListOf<PetStore>()
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
                dataBinding.viewPetAnimation.lottiePet.apply {
                    setAnimation(petThemeColor.gigglePose)
                    repeatCount = LottieDrawable.INFINITE
                    playAnimation()
                }
            }

            kidPetStore.observe(viewLifecycleOwner) {
                petStoreList.clear()
                petStoreList.addAll(it)
            }
        }

        setView()
        initAnimation()
        setOnClick()
    }

    private fun setView() {
        viewModel.getKidInfo()
        viewModel.getKidPetstoreList()
    }

    private fun setKidView(data: KidInfoModel) {
        dataBinding.apply {
            tvChildLevel.textOrNull = data.level.level.toString()
            piChildLevel.progress = 100 - data.level.percentageToNextLevel
        }

        updateIndicatorBar(data.pet)
    }

    private fun setOnClick() {
        dataBinding.cvSwitchUser.setOnClickListener {
            (activity as ChildMainAct).showMenu(isShow = false)
            addFragment(LoginChooseProfileFragment.newInstance())
        }

        dataBinding.viewPetAnimation.lottiePet.setOnClickListener {
            viewModel.postKidTapThePet()
            dataBinding.viewPetAnimation.lottiePet.apply {
                setAnimation(petThemeColor.gigglePose)
                repeatCount = LottieDrawable.INFINITE
                playAnimation()
            }
        }

        dataBinding.ivPetAccessories.setOnClickListener {
            showPetstorePicker(PetstorePickerFragment.TYPE_ACCESSORIES)
        }

        dataBinding.ivPetEat.setOnClickListener {
            showPetstorePicker(PetstorePickerFragment.TYPE_FOOD)
        }

        dataBinding.ivPetDecor.setOnClickListener {
            showPetstorePicker(PetstorePickerFragment.TYPE_DECOR)
        }
    }

    private fun showPetstorePicker(typeCategory: String) {
        if (petStoreList.filter { it.category == typeCategory }.isNotEmpty()) {
            PetstorePickerFragment(petstoreListener = onPetStoreData).apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(
                        PetstorePickerFragment.LIST_PETSTORE,
                        petStoreList.filter { it.category == typeCategory }
                            .toArrayList()
                    )
                }
            }.show(childFragmentManager, PetstorePickerFragment.TAG)
        }
    }

    private val onPetStoreData = object : PetStoreData {
        override fun onPetstoreSelect(onSelect: PetStore) {
            when (onSelect.category) {
                PetstorePickerFragment.TYPE_ACCESSORIES -> {
                    AppPreference.putKidPetColorTheme(onSelect.color)
                }

                PetstorePickerFragment.TYPE_DECOR -> {
                    AppPreference.putKidPetDecorAssignment(onSelect.icon)
                }

                PetstorePickerFragment.TYPE_FOOD -> {
                    AppPreference.putKidPetFoodAssignment(onSelect.icon)
                    AppPreference.putPetCurrentEmotion(PetEmotion.PET_EMOTION_YUMMY)
                }
            }
            initAnimation()

        }

        override fun onPetstoreBuy(onBuy: PetStore) {
            viewModel.getKidInfo()
            viewModel.getKidPetstoreList()
        }
    }

    private fun initAnimation() {
        dataBinding.viewPetAnimation.ivFoodUsing.loadFromUrl(AppPreference.getKidPetFoodAssignment())
        dataBinding.viewPetAnimation.ivPetDecorUsing.loadFromUrl(AppPreference.getKidPetDecorAssignment())
        dataBinding.viewPetAnimation.lottiePet.apply {
            when (AppPreference.getPetCurrentEmotion()) {
                PetEmotion.PET_EMOTION_ANGRY -> {
                    setAnimation(petThemeColor.angryPose)
                }

                PetEmotion.PET_EMOTION_HAPPY -> {
                    setAnimation(petThemeColor.happyPose)
                }

                PetEmotion.PET_EMOTION_HUNGRY -> {
                    setAnimation(petThemeColor.hungryPose)
                }

                PetEmotion.PET_EMOTION_YUMMY -> {
                    setAnimation(petThemeColor.yummyPose)
                }

                PetEmotion.PET_EMOTION_NORMAL -> {
                    setAnimation(petThemeColor.normalPose)
                }
            }
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
        }
    }

    private fun updateIndicatorBar(petInfo: PetModel) {
        when (petInfo.happiness) {
            in 0..24 -> {
                dataBinding.ivPetEmotion.setImageResource(R.drawable.ic_pet_emotion_0)
            }

            in 25..49 -> {
                dataBinding.ivPetEmotion.setImageResource(R.drawable.ic_pet_emotion_25)
            }

            in 50..74 -> {
                dataBinding.ivPetEmotion.setImageResource(R.drawable.ic_pet_emotion_50)
            }

            in 75..99 -> {
                dataBinding.ivPetEmotion.setImageResource(R.drawable.ic_pet_emotion_75)
            }

            100 -> {
                dataBinding.ivPetEmotion.setImageResource(R.drawable.ic_pet_emotion_100)
            }
        }

        when (petInfo.hunger) {
            in 0..24 -> {
                dataBinding.ivPetEat.setImageResource(R.drawable.ic_pet_eat_0)
            }

            in 25..49 -> {
                dataBinding.ivPetEat.setImageResource(R.drawable.ic_pet_eat_25)
            }

            in 50..74 -> {
                dataBinding.ivPetEat.setImageResource(R.drawable.ic_pet_eat_50)
            }

            in 75..99 -> {
                dataBinding.ivPetEat.setImageResource(R.drawable.ic_pet_eat_75)
            }

            100 -> {
                dataBinding.ivPetEat.setImageResource(R.drawable.ic_pet_eat_100)
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ChildPetFragment()
    }
}