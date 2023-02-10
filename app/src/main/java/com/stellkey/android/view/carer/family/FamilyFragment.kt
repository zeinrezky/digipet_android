package com.stellkey.android.view.carer.family

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogDeleteProfileBinding
import com.stellkey.android.databinding.DialogInfoBinding
import com.stellkey.android.databinding.FragmentFamilyBinding
import com.stellkey.android.helper.extension.*
import com.stellkey.android.model.AllCarersModel
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.model.FamilyTypeModel
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.util.SwipeToDeleteCallback
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.family.adapter.AdminListAdapter
import com.stellkey.android.view.carer.family.adapter.CarerListAdapter
import com.stellkey.android.view.carer.family.adapter.FamilyTypeAdapter
import com.stellkey.android.view.carer.family.adapter.KidListAdapter
import com.stellkey.android.view.carer.family.qrlogin.QrLoginFamilyFragment
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.carer.home.HomeViewModel
import com.stellkey.android.view.carer.profile.CarerProfileFragment
import com.stellkey.android.view.carer.profile.KidProfileFragment
import org.koin.android.ext.android.inject

class FamilyFragment : BaseFragment(), FamilyTypeAdapter.Listener, KidListAdapter.Listener {

    private lateinit var dataBinding: FragmentFamilyBinding

    //private val binding by viewBinding<FragmentFamilyBinding>()
    private val viewModel by inject<HomeViewModel>()

    private lateinit var familyTypeAdapter: FamilyTypeAdapter
    private lateinit var kidListAdapter: KidListAdapter
    private lateinit var carerListAdapter: CarerListAdapter
    private lateinit var adminListAdapter: AdminListAdapter

    private lateinit var dialogInfoBinding: DialogInfoBinding
    private lateinit var dialogDeleteProfileBinding: DialogDeleteProfileBinding
    private lateinit var deleteProfileDialog: AlertDialog

    private var selectedPosition = emptyInt

    companion object {
        @JvmStatic
        fun newInstance() = FamilyFragment()
        var isAddKid = emptyBoolean
        var isAddCarer = emptyBoolean
        var addedCarerTempName = emptyString
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_family,
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
            listAllKids.observe(viewLifecycleOwner) {
                setAllKidsList(it)
            }
            listAllCarers.observe(viewLifecycleOwner) {
                setAllCarersList(it)
            }
            deleteKidSuccess.observe(viewLifecycleOwner) {
                kidListAdapter.removeAt(selectedPosition)
                selectedPosition = emptyInt
                AppPreference.deleteTempCreateMemberData()
            }
            deleteCarerSuccess.observe(viewLifecycleOwner) {
                carerListAdapter.removeAt(selectedPosition)
                selectedPosition = emptyInt
                AppPreference.deleteTempCreateMemberData()
            }

        }

        (activity as HomeAct).showMenu(isShow = true)
        setView()
        setOnClick()
    }

    private fun setView() {
        if (isAddKid) {
            initInfoDialog("Success!", "Great – you've set up your kid's profile!")
            isAddKid = emptyBoolean
        }

        if (isAddCarer) {
            initInfoDialog("Success!", "Great – you've set up ${addedCarerTempName}'s profile!")
            isAddCarer = emptyBoolean
        }

        viewModel.getListAllKids()
        viewModel.getListAllCarers()

        dataBinding.apply {
            val familyTypeList = arrayListOf<FamilyTypeModel>()
            familyTypeList.add(FamilyTypeModel(getString(R.string.kids)))
            familyTypeList.add(FamilyTypeModel(getString(R.string.carers)))
            familyTypeList.add(FamilyTypeModel(getString(R.string.admin)))

            familyTypeAdapter = FamilyTypeAdapter(
                requireContext(),
                familyTypeList,
                this@FamilyFragment
            )

            rvFilterFamilyType.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = familyTypeAdapter
            }
        }

        onBackPressed()
    }

    private fun setAllKidsList(allKidsList: ArrayList<AllKidsModel>) {
        dataBinding.apply {
            kidListAdapter = KidListAdapter(
                requireContext(),
                allKidsList,
                this@FamilyFragment
            )

            rvKid.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = kidListAdapter
            }

            val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    selectedPosition = viewHolder.adapterPosition
                    kidListAdapter.selectKid(selectedPosition)

                    AppPreference.putProfileType(Constant.FamilyType.KIDS)

                    if (AppPreference.getTempChildId() != emptyInt)
                        initDeleteProfileDialog(
                            profileId = AppPreference.getTempChildId(),
                            profileName = AppPreference.getTempChildName()
                        )
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(rvKid)
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

            carerListAdapter = CarerListAdapter(
                requireContext(),
                carerList
            )
            rvCarer.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = carerListAdapter
            }
            val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    selectedPosition = viewHolder.adapterPosition
                    carerListAdapter.selectCarer(selectedPosition)

                    AppPreference.putProfileType(Constant.FamilyType.CARERS)

                    if (AppPreference.getSelectedCarerId() != emptyInt)
                        initDeleteProfileDialog(
                            profileId = AppPreference.getSelectedCarerId(),
                            profileName = AppPreference.getTempCarerName()
                        )
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(rvCarer)

            adminListAdapter = AdminListAdapter(
                requireContext(),
                adminList
            )
            rvAdmin.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = adminListAdapter
            }
        }
    }

    private fun initInfoDialog(textTitle: String, textDesc: String) {
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
                when (AppPreference.getProfileType()) {
                    Constant.FamilyType.CARERS -> {
                        carerListAdapter.notifyDataSetChanged()
                    }

                    Constant.FamilyType.KIDS -> {
                        kidListAdapter.notifyDataSetChanged()
                    }
                }
            }
        }


        deleteProfileDialog.show()
    }

    //Family Type Filter Items
    override fun onItemClicked(data: FamilyTypeModel) {
        dataBinding.apply {
            when (data.type) {
                Constant.FamilyType.KIDS -> {
                    rvKid.isVisible = true
                    rvCarer.isVisible = false
                    rvAdmin.isVisible = false
                }

                Constant.FamilyType.CARERS -> {
                    rvKid.isVisible = false
                    rvCarer.isVisible = true
                    rvAdmin.isVisible = false
                }

                Constant.FamilyType.ADMIN -> {
                    rvKid.isVisible = false
                    rvCarer.isVisible = false
                    rvAdmin.isVisible = true
                }

                else -> {
                    rvKid.isVisible = true
                    rvCarer.isVisible = true
                    rvAdmin.isVisible = true
                }
            }

            cvAllFamilyType.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            tvAllFamilyType.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        }
    }

    //Kid List Items
    override fun onItemClicked(data: AllKidsModel) {
        AppPreference.putTempChildId(data.id)
        addFragment(KidProfileFragment.newInstance())
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            cvAllFamilyType.setOnClickListener(onClickCallback)
            cvAddFamily.setOnClickListener(onClickCallback)
            ivQrLogin.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.cvAllFamilyType -> {
                dataBinding.apply {
                    cvAllFamilyType.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    tvAllFamilyType.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    rvKid.isVisible = true
                    rvCarer.isVisible = true
                    rvAdmin.isVisible = true
                }
                familyTypeAdapter.resetSelectedItem()
            }

            dataBinding.cvAddFamily -> {
                addFragment(AddMemberOptionsFragment.newInstance())
            }

            dataBinding.ivQrLogin -> {
                addFragment(QrLoginFamilyFragment.newInstance())
            }
        }
    }
}