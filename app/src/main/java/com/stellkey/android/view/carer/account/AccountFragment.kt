package com.stellkey.android.view.carer.account

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogDeleteAllLogBinding
import com.stellkey.android.databinding.DialogDeleteProfileBinding
import com.stellkey.android.databinding.DialogInfoBinding
import com.stellkey.android.databinding.FragmentAccountBinding
import com.stellkey.android.helper.extension.alertDialog
import com.stellkey.android.helper.extension.color
import com.stellkey.android.helper.extension.emptyBoolean
import com.stellkey.android.helper.extension.openUrl
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AllCarersModel
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.model.NewSubscriptionModel
import com.stellkey.android.model.SubscriptionModel
import com.stellkey.android.model.request.DeleteLogRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.account.adapter.PPCAdminListAdapter
import com.stellkey.android.view.carer.account.adapter.PPCCarerListAdapter
import com.stellkey.android.view.carer.account.adapter.PPCKidListAdapter
import com.stellkey.android.view.carer.account.adapter.SubscriptionAdapter
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.intro.intro.IntroAct
import kotlinx.android.synthetic.main.fragment_all_profile_icons.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class AccountFragment : BaseFragment(), SubscriptionAdapter.Listener,
    PPCAdminListAdapter.Listener, PPCCarerListAdapter.Listener, PPCKidListAdapter.Listener {

    private lateinit var dataBinding: FragmentAccountBinding
    private val viewModel by inject<AccountViewModel>()

    private lateinit var subscriptionAdapter: SubscriptionAdapter
    private lateinit var ppcAdminListAdapter: PPCAdminListAdapter
    private lateinit var ppcCarerListAdapter: PPCCarerListAdapter
    private lateinit var ppcKidListAdapter: PPCKidListAdapter

    private var isExpandPremiumUpgradeLayout = emptyBoolean
    private var isExpandPPCLayout = emptyBoolean
    private var isExpandMembership = emptyBoolean
    private var isExpandBillingInformation = emptyBoolean

    private lateinit var dialogDeleteAllLogBinding: DialogDeleteAllLogBinding
    private lateinit var deleteAllLogDialog: AlertDialog
    private lateinit var dialogDeleteProfileBinding: DialogDeleteProfileBinding
    private lateinit var deleteProfileDialog: AlertDialog

    companion object {
        @JvmStatic
        fun newInstance() = AccountFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_account,
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
            deleteLogSuccess.observe(viewLifecycleOwner) {
                deleteAllLogDialog.dismiss()
            }
            deleteCarerSuccess.observe(viewLifecycleOwner) {
                viewModel.getListAllCarers()
                AppPreference.deleteTempCreateMemberData()
            }
            deleteKidSuccess.observe(viewLifecycleOwner) {
                viewModel.getListAllKids()
                AppPreference.deleteTempCreateMemberData()
            }
            listAllCarers.observe(viewLifecycleOwner) {
                setAllCarersList(it)
            }
            listAllKids.observe(viewLifecycleOwner) {
                setAllKidsList(it)
            }
            subscriptionResponse.observe(viewLifecycleOwner){
                it?.let { it1 -> setSubscriptionData(it1) }
            }

        }

        (activity as HomeAct).showMenu(isShow = true)
        setView()
        setOnClick()
    }

    private fun setView() {
        viewModel.getSubscription()

        dataBinding.apply {
            val subscriptionList = arrayListOf<NewSubscriptionModel>()
            subscriptionList.add(
                NewSubscriptionModel(
                    expiresAt = "",
                    startAt = "",
                    subscriptionId = "",
                    status = 1,
                    subscriptionDuration = "1",
                    subscriptionDurationType = "Month",
                    subscriptionCost = "€4.99/mo",
                    subscriptionBillDesc = "Billed Monthly"
                )
            )
            subscriptionList.add(
                NewSubscriptionModel(
                    expiresAt = "",
                    startAt = "",
                    subscriptionId = "",
                    status = 1,
                    subscriptionDuration = "12",
                    subscriptionDurationType = "Months",
                    subscriptionCost = "€2.99/mo",
                    subscriptionBillDesc = "Billed annually\n€35.99/y"
                )
            )
            subscriptionList.add(
                NewSubscriptionModel(
                    expiresAt = "",
                    startAt = "",
                    subscriptionId = "",
                    status = 1,
                    subscriptionDuration = requireContext().getString(R.string.infinity),
                    subscriptionDurationType = "Lifetime",
                    subscriptionCost = "€49.99",
                    subscriptionBillDesc = "Billed once"
                )
            )

            subscriptionAdapter = SubscriptionAdapter(
                requireContext(),
                subscriptionList,
                this@AccountFragment
            )

            rvPremiumSubscription.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = subscriptionAdapter
            }
        }
        onBackPressed()
    }

    private fun setSubscriptionData(subscriptionResponse: ArrayList<SubscriptionModel>) {
        dataBinding.apply {
            val subscriptionExpiresAt = subscriptionResponse.firstOrNull()?.expiresAt
            val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val formatter = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
            val subscriptionExpiresAtFormatted = subscriptionExpiresAt?.let { parser.parse(it)?.let { formatter.format(it) } }

            when(subscriptionResponse.firstOrNull()?.status){
                0 -> {
                    tvPremiumUpgrade.textOrNull = requireContext().resources.getString(R.string.upgrade_to_premium)
                    tvMembershipType.textOrNull = requireContext().resources.getString(R.string.membership_type_trial)
                }
                1 -> {
                    when(subscriptionResponse[0].subscriptionId){
                        Constant.SubscriptionType.TRIAL -> {
                            tvPremiumUpgrade.textOrNull = requireContext().resources.getString(R.string.upgrade_to_premium)
                            tvMembershipType.textOrNull = requireContext().resources.getString(R.string.membership_type_trial)
                        }
                        Constant.SubscriptionType.MONTHLY -> {
                            tvPremiumUpgrade.textOrNull = requireContext().resources.getString(R.string.your_membership_type_monthly)
                            tvMembershipType.textOrNull = requireContext().resources.getString(R.string.membership_type_monthly)
                        }
                        Constant.SubscriptionType.YEARLY -> {
                            tvPremiumUpgrade.textOrNull = requireContext().resources.getString(R.string.your_membership_type_yearly)
                            tvMembershipType.textOrNull = requireContext().resources.getString(R.string.membership_type_yearly)
                        }
                        Constant.SubscriptionType.LIFETIME -> {
                            tvPremiumUpgrade.textOrNull = requireContext().resources.getString(R.string.your_membership_type_lifetime)
                            tvMembershipType.textOrNull = requireContext().resources.getString(R.string.membership_type_lifetime)
                        }
                    }
                }
            }
            tvRenewalSchedule.textOrNull = requireContext().resources.getString(
                R.string.next_renews,
                subscriptionExpiresAtFormatted
            )
        }
    }

    private fun setAllCarersList(allCarersList: ArrayList<AllCarersModel>) {
        dataBinding.apply {
            val carerList = arrayListOf<AllCarersModel>()
            val adminList = arrayListOf<AllCarersModel>()

            allCarersList.forEach {
                if (it.isMain)
                    adminList.add(it)
                else
                    carerList.add(it)
            }

            ppcAdminListAdapter = PPCAdminListAdapter(
                requireContext(),
                adminList,
                this@AccountFragment
            )
            rvPPCAdmin.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = ppcAdminListAdapter
            }

            ppcCarerListAdapter = PPCCarerListAdapter(
                requireContext(),
                carerList,
                this@AccountFragment
            )
            rvPPCCarer.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = ppcCarerListAdapter
            }
        }
    }

    private fun setAllKidsList(allKidsList: ArrayList<AllKidsModel>) {
        dataBinding.apply {
            ppcKidListAdapter = PPCKidListAdapter(
                requireContext(),
                allKidsList,
                this@AccountFragment
            )

            rvPPCKid.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = ppcKidListAdapter
            }
        }
    }

    //Subscription Items
    override fun onItemClicked(data: NewSubscriptionModel) {
        dataBinding.apply {
            if (data.isSelected)
                btnUpgrade.apply {
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
            else
                btnUpgrade.apply {
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

    override fun onAdminEditProfileClicked(data: AllCarersModel) {
        addFragment(EditProfileFragment.newInstance())
        AppPreference.putProfileType(Constant.FamilyType.ADMIN)
        AppPreference.putSelectedCarerId(data.id)
        AppPreference.putTempCarerPIN(data.pin)
    }

    override fun onAdminEditPasswordClicked(data: AllCarersModel) {
        addFragment(EditPasswordFragment.newInstance())
    }

    override fun onCarerEditProfileClicked(data: AllCarersModel) {
        AppPreference.deleteTempCreateMemberData()

        addFragment(EditProfileFragment.newInstance())
        AppPreference.putProfileType(Constant.FamilyType.CARERS)
        AppPreference.putSelectedCarerId(data.id)
        AppPreference.putTempCarerPIN(data.pin)
    }

    override fun onCarerEditPINClicked(data: AllCarersModel) {
        addFragment(EditPINFragment.newInstance())
        EditPINFragment.currentProfilePIN = data.pin
        EditPINFragment.isFromEditProfile = false

        AppPreference.putProfileType(Constant.FamilyType.CARERS)
        AppPreference.putSelectedCarerId(data.id)
        AppPreference.putTempCarerPIN(data.pin)
    }

    override fun onCarerItemDeleteClicked(data: AllCarersModel) {
        AppPreference.putProfileType(Constant.FamilyType.CARERS)
        initDeleteProfileDialog(profileId = data.id, profileName = data.name)
    }

    override fun onKidEditPINClicked(data: AllKidsModel) {
        addFragment(EditPINFragment.newInstance())
        EditPINFragment.currentProfilePIN = data.pin
        EditPINFragment.isFromEditProfile = false

        AppPreference.putProfileType(Constant.FamilyType.KIDS)
        AppPreference.putTempChildId(data.id)
    }

    override fun onKidEditProfileClicked(data: AllKidsModel) {
        AppPreference.deleteTempCreateMemberData()

        addFragment(EditProfileFragment.newInstance())
        AppPreference.putProfileType(Constant.FamilyType.KIDS)
        AppPreference.putTempChildId(data.id)
        AppPreference.putTempChildProfilePIN(data.pin)
        AppPreference.putTempChildAge(data.age)
    }

    override fun onKidItemDeleteClicked(data: AllKidsModel) {
        AppPreference.putProfileType(Constant.FamilyType.KIDS)
        initDeleteProfileDialog(profileId = data.id, profileName = data.name)
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

    private fun initDeleteAllLogDialog() {
        dialogDeleteAllLogBinding = DialogDeleteAllLogBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        deleteAllLogDialog = requireActivity().alertDialog(
            view = dialogDeleteAllLogBinding.root,
            isCancelable = true,
            fullScreen = true
        )
        deleteAllLogDialog.window?.setBackgroundDrawableResource(R.color.transparent)

        dialogDeleteAllLogBinding.btnYes.setOnClickListener {
            viewModel.deleteLog(DeleteLogRequest(days = 0))
        }

        dialogDeleteAllLogBinding.btnNo.setOnClickListener {
            deleteAllLogDialog.dismiss()
        }

        deleteAllLogDialog.show()
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
                    Constant.FamilyType.CARERS -> {
                        viewModel.deleteCarer(carerId = profileId)
                    }
                    Constant.FamilyType.KIDS -> {
                        viewModel.deleteKid(kidId = profileId)
                    }
                }
            }
            btnNo.setOnClickListener {
                deleteProfileDialog.dismiss()
            }
        }

        deleteProfileDialog.show()
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivExpandPremiumUpgrade.setOnClickListener(onClickCallback)
            clSetting.setOnClickListener(onClickCallback)
            tvLogActivity.setOnClickListener(onClickCallback)
            tvClearLogActivity.setOnClickListener(onClickCallback)
            clCustomTask.setOnClickListener(onClickCallback)
            ivExpandProfileParentalControl.setOnClickListener(onClickCallback)
            ivExpandMembership.setOnClickListener(onClickCallback)
            ivExpandBillingInformation.setOnClickListener(onClickCallback)
            tvLogout.setOnClickListener(onClickCallback)
            tvFAQ.setOnClickListener {
                context?.openUrl("https://stellkey.com/help/")
            }
            tvPrivacyPolicy.setOnClickListener {
                context?.openUrl("https://stellkey.com/privacy/")
            }
            tvTNC.setOnClickListener {
                context?.openUrl("https://stellkey.com/terms-and-conditions/")
            }


        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivExpandPremiumUpgrade -> {
                dataBinding.apply {
                    if (isExpandPremiumUpgradeLayout) {
                        ivExpandPremiumUpgrade.setImageResource(R.drawable.ic_arrow_right_primary)
                        isExpandPremiumUpgradeLayout = false
                        clExpandPremiumUpgrade.isVisible = false

                        subscriptionAdapter.resetSelectedItem()
                        btnUpgrade.apply {
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
                    } else {
                        ivExpandPremiumUpgrade.setImageResource(R.drawable.ic_arrow_down_primary)
                        isExpandPremiumUpgradeLayout = true
                        clExpandPremiumUpgrade.isVisible = true
                    }
                }
            }
            dataBinding.clSetting -> {
                addFragment(SettingFragment.newInstance())
            }
            dataBinding.tvLogActivity -> {
                addFragment(LogActivityFragment.newInstance())
            }
            dataBinding.tvClearLogActivity -> {
                initDeleteAllLogDialog()
            }
            dataBinding.clCustomTask -> {
                addFragment(CustomisedTaskFragment.newInstance())
            }
            dataBinding.ivExpandProfileParentalControl -> {
                dataBinding.apply {
                    if (isExpandPPCLayout) {
                        ivExpandProfileParentalControl.setImageResource(R.drawable.ic_arrow_right)
                        isExpandPPCLayout = false
                        clExpandPPC.isVisible = false
                    } else {
                        ivExpandProfileParentalControl.setImageResource(R.drawable.ic_arrow_down)
                        isExpandPPCLayout = true
                        clExpandPPC.isVisible = true

                        viewModel.getListAllCarers()
                        viewModel.getListAllKids()
                    }
                }
            }
            dataBinding.ivExpandMembership -> {
                dataBinding.apply {
                    if (isExpandMembership) {
                        ivExpandMembership.setImageResource(R.drawable.ic_arrow_right)
                        isExpandMembership = false
                        clExpandMembership.isVisible = false
                    } else {
                        ivExpandMembership.setImageResource(R.drawable.ic_arrow_down)
                        isExpandMembership = true
                        clExpandMembership.isVisible = true
                    }
                }
            }
            dataBinding.ivExpandBillingInformation -> {
                dataBinding.apply {
                    if (isExpandBillingInformation) {
                        ivExpandBillingInformation.setImageResource(R.drawable.ic_arrow_right)
                        isExpandBillingInformation = false
                        clExpandBillingInformation.isVisible = false
                    } else {
                        ivExpandBillingInformation.setImageResource(R.drawable.ic_arrow_down)
                        isExpandBillingInformation = true
                        clExpandBillingInformation.isVisible = true
                    }
                }
            }
            dataBinding.tvLogout -> {
                AppPreference.deleteAll()
                val intent = Intent(context, IntroAct::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }
}