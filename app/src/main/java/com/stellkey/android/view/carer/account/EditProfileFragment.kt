package com.stellkey.android.view.carer.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogDeleteProfileBinding
import com.stellkey.android.databinding.FragmentEditProfileBinding
import com.stellkey.android.helper.extension.*
import com.stellkey.android.model.AllCarersModel
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.model.request.EditCarerRequest
import com.stellkey.android.model.request.EditKidRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.intro.auth.AllProfileIconsFragment
import org.koin.android.ext.android.inject
import java.util.*

class EditProfileFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentEditProfileBinding

    //private val binding by viewBinding<FragmentEditProfileBinding>()
    private val viewModel by inject<AccountViewModel>()

    private lateinit var dialogDeleteProfileBinding: DialogDeleteProfileBinding
    private lateinit var deleteProfileDialog: AlertDialog

    companion object {
        @JvmStatic
        fun newInstance() = EditProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_edit_profile,
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
            detailCarer.observe(viewLifecycleOwner) {
                it?.let { it1 -> setCarerDetailData(it1) }
            }
            updateCarerSuccess.observe(viewLifecycleOwner) {
                AppPreference.putEditProfile(emptyBoolean)
                AppPreference.deleteTempCreateMemberData()

                (activity as HomeAct).showMenu(isShow = true)
                requireActivity().supportFragmentManager.popBackStack()
            }
            deleteCarerSuccess.observe(viewLifecycleOwner) {
                AppPreference.putEditProfile(emptyBoolean)
                AppPreference.deleteTempCreateMemberData()

                (activity as HomeAct).showMenu(isShow = true)
                requireActivity().supportFragmentManager.popBackStack()
            }
            detailKid.observe(viewLifecycleOwner) {
                it?.let { it1 -> setKidDetailData(it1) }
            }
            updateKidSuccess.observe(viewLifecycleOwner) {
                AppPreference.putEditProfile(emptyBoolean)
                AppPreference.deleteTempCreateMemberData()

                (activity as HomeAct).showMenu(isShow = true)
                requireActivity().supportFragmentManager.popBackStack()
            }
            deleteKidSuccess.observe(viewLifecycleOwner) {
                AppPreference.putEditProfile(emptyBoolean)
                AppPreference.deleteTempCreateMemberData()

                (activity as HomeAct).showMenu(isShow = true)
                requireActivity().supportFragmentManager.popBackStack()
            }

        }

        (activity as HomeAct).showMenu(isShow = false)
        setView()
        setOnClick()
    }

    override fun onResume() {
        super.onResume()
        if (AppPreference.isEditProfile()) {
            if (AppPreference.getProfileIconId() != emptyInt)
                dataBinding.ivAvatar.loadImage(
                    AppPreference.getProfileIcon(),
                    ImageCornerOptions.ROUNDED,
                    100
                )
        }
    }

    private fun setView() {
        onBackPressed()

        dataBinding.apply {
            when (AppPreference.getProfileType()) {
                Constant.FamilyType.ADMIN -> {
                    tvEditPinPassword.textOrNull =
                        requireContext().resources.getString(R.string.edit_password)
                    btnRemoveProfile.isVisible = false

                    if (!AppPreference.isEditProfile())
                        viewModel.getDetailCarer(profileId = AppPreference.getSelectedCarerId())
                    else
                        etProfileName.apply {
                            setText(AppPreference.getTempCarerName())
                        }

                    etProfileName.afterTextChanged { AppPreference.putTempCarerName(it) }

                    btnSave.setOnClickListener {
                        viewModel.putUpdateCarer(
                            profileId = AppPreference.getSelectedCarerId(),
                            updateCarerRequest = EditCarerRequest(
                                name = etProfileName.text.toString(),
                                pin = AppPreference.getTempCarerPIN(),
                                isMain = AppPreference.getProfileType() == Constant.FamilyType.ADMIN,
                                canCreate = true,
                                iconId = AppPreference.getProfileIconId()
                            )
                        )
                    }
                }
                Constant.FamilyType.CARERS -> {
                    tvEditPinPassword.textOrNull =
                        requireContext().resources.getString(R.string.edit_pin)
                    btnRemoveProfile.isVisible = true

                    if (!AppPreference.isEditProfile())
                        viewModel.getDetailCarer(profileId = AppPreference.getSelectedCarerId())
                    else
                        etProfileName.apply {
                            setText(AppPreference.getTempCarerName())
                        }

                    etProfileName.afterTextChanged { AppPreference.putTempCarerName(it) }

                    tvEditPinPassword.setOnClickListener{
                        addFragment(EditPINFragment.newInstance())
                        EditPINFragment.currentProfilePIN = AppPreference.getTempCarerPIN()
                        EditPINFragment.isFromEditProfile = true

                        AppPreference.putProfileType(Constant.FamilyType.CARERS)
                    }

                    btnRemoveProfile.setOnClickListener {
                        initDeleteProfileDialog(
                            profileId = AppPreference.getSelectedCarerId(),
                            profileName = AppPreference.getTempCarerName()
                        )
                    }

                    btnSave.setOnClickListener {
                        viewModel.putUpdateCarer(
                            profileId = AppPreference.getSelectedCarerId(),
                            updateCarerRequest = EditCarerRequest(
                                name = etProfileName.text.toString(),
                                pin = AppPreference.getTempCarerPIN(),
                                isMain = AppPreference.getProfileType() == Constant.FamilyType.ADMIN,
                                canCreate = true,
                                iconId = AppPreference.getProfileIconId()
                            )
                        )
                    }
                }
                Constant.FamilyType.KIDS -> {
                    tvEditPinPassword.textOrNull =
                        requireContext().resources.getString(R.string.edit_pin)
                    btnRemoveProfile.isVisible = true

                    if (!AppPreference.isEditProfile())
                        viewModel.getDetailKid(profileId = AppPreference.getTempChildId())
                    else
                        etProfileName.apply {
                            setText(AppPreference.getTempChildName())
                        }

                    etProfileName.afterTextChanged { AppPreference.putTempChildName(it) }

                    tvEditPinPassword.setOnClickListener{
                        addFragment(EditPINFragment.newInstance())
                        EditPINFragment.currentProfilePIN = AppPreference.getTempChildProfilePIN()
                        EditPINFragment.isFromEditProfile = true

                        AppPreference.putProfileType(Constant.FamilyType.KIDS)
                    }

                    btnRemoveProfile.setOnClickListener {
                        initDeleteProfileDialog(
                            profileId = AppPreference.getTempChildId(),
                            profileName = AppPreference.getTempChildName()
                        )
                    }

                    btnSave.setOnClickListener {
                        val tz: TimeZone = TimeZone.getDefault()
                        val now = Date()
                        val offsetTz: Int = tz.getOffset(now.time) / 3600000

                        viewModel.putUpdateKid(
                            profileId = AppPreference.getTempChildId(),
                            updateKidRequest = EditKidRequest(
                                name = etProfileName.text.toString(),
                                pin = AppPreference.getTempChildProfilePIN(),
                                age = AppPreference.getTempChildAge(),
                                iconId = AppPreference.getProfileIconId(),
                                settingTimezone = offsetTz,
                                settingLocale = "en"
                            )
                        )
                    }
                }
            }
        }
    }

    private fun initDeleteProfileDialog(profileId: Int, profileName: String) {
        dialogDeleteProfileBinding = DialogDeleteProfileBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        deleteProfileDialog = requireActivity().alertDialog(
            view = dialogDeleteProfileBinding.root,
            isCancelable = true,
            fullScreen = true
        )
        deleteProfileDialog.window?.setBackgroundDrawableResource(R.color.transparent)

        dialogDeleteProfileBinding.apply {
            tvDesc.textOrNull = requireContext().resources.getString(
                R.string.dialog_delete_profile_desc,
                profileName
            )
            btnYes.setOnClickListener {
                deleteProfileDialog.dismiss()
                when (AppPreference.getProfileType()) {
                    Constant.FamilyType.CARERS -> { viewModel.deleteCarer(carerId = profileId) }
                    Constant.FamilyType.KIDS -> { viewModel.deleteKid(kidId = profileId) }
                }
            }
            btnNo.setOnClickListener {
                deleteProfileDialog.dismiss()
            }
        }

        deleteProfileDialog.show()
    }

    private fun setCarerDetailData(carerData: AllCarersModel) {
        dataBinding.apply {
            if (AppPreference.isEditProfile()) {
                if (AppPreference.getProfileIconId() != emptyInt)
                    ivAvatar.loadImage(
                        AppPreference.getProfileIconId(),
                        ImageCornerOptions.ROUNDED,
                        100
                    )
                else
                    ivAvatar.loadImage(
                        carerData.profileIcon.icon,
                        ImageCornerOptions.ROUNDED,
                        100
                    )
                etProfileName.setText(AppPreference.getTempCarerName())
            } else {
                ivAvatar.loadImage(
                    carerData.profileIcon.icon,
                    ImageCornerOptions.ROUNDED,
                    100
                )
                etProfileName.setText(carerData.name)
            }

            ivEdit.setOnClickListener {
                AllProfileIconsFragment.profileIconType =
                    Constant.ProfileIconType.PROFILE_ICON_CARER
                addFragment(AllProfileIconsFragment.newInstance())
            }

            if (!AppPreference.isEditProfile())
                AppPreference.putEditProfile(true)
        }
    }

    private fun setKidDetailData(kidData: AllKidsModel) {
        dataBinding.apply {
            if (AppPreference.isEditProfile()) {
                if (AppPreference.getProfileIconId() != emptyInt)
                    ivAvatar.loadImage(
                        AppPreference.getProfileIconId(),
                        ImageCornerOptions.ROUNDED,
                        100
                    )
                else
                    ivAvatar.loadImage(
                        kidData.profileIcon.icon,
                        ImageCornerOptions.ROUNDED,
                        100
                    )
                etProfileName.setText(AppPreference.getTempChildName())
            } else {
                ivAvatar.loadImage(
                    kidData.profileIcon.icon,
                    ImageCornerOptions.ROUNDED,
                    100
                )
                etProfileName.setText(kidData.name)
                AppPreference.putProfileIconId(kidData.iconId)
            }

            ivEdit.setOnClickListener {
                AllProfileIconsFragment.profileIconType =
                    Constant.ProfileIconType.PROFILE_ICON_KID
                addFragment(AllProfileIconsFragment.newInstance())
            }

            if (!AppPreference.isEditProfile())
                AppPreference.putEditProfile(true)
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            AppPreference.putEditProfile(emptyBoolean)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                AppPreference.putEditProfile(emptyBoolean)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

}