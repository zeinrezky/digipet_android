package com.stellkey.android.view.intro.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogKidAgeBinding
import com.stellkey.android.databinding.FragmentRegisterChildAgeBinding
import com.stellkey.android.helper.extension.alertDialog
import com.stellkey.android.helper.extension.makeLinks
import com.stellkey.android.model.ChildAgeModel
import com.stellkey.android.model.request.CreateChildRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.intro.auth.adapter.ChildAgeAdapter
import com.stellkey.android.view.base.BaseFragment
import org.koin.android.ext.android.inject
import java.util.*

class RegisterChildAgeFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentRegisterChildAgeBinding

    //private val binding by viewBinding<FragmentRegisterChildAgeBinding>()
    private val viewModel by inject<RegisterViewModel>()

    lateinit var childAgeAdapter: ChildAgeAdapter
    lateinit var ageList: MutableList<ChildAgeModel>

    var tempChildAge = 1
    val tz: TimeZone = TimeZone.getDefault()
    val now = Date()
    val offsetTz: Int = tz.getOffset(now.time) / 3600000

    private lateinit var dialogAgeInformationPopupBinding: DialogKidAgeBinding
    private lateinit var ageInformationPopupDialog: AlertDialog

    companion object {
        @JvmStatic
        fun newInstance() = RegisterChildAgeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_register_child_age,
                container,
                false
            )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            isLoading.observe(viewLifecycleOwner) { bool ->
                bool.let { loading ->
                    if (loading) {
                        showWaitingDialog()
                    } else {
                        hideWaitingDialog()
                    }
                }
                createKidSuccess.observe(viewLifecycleOwner) {
                    addFragment(RegisterKidsChallengeFragment.newInstance())
                }
            }
        }

        setView()
        setOnClick()
    }

    private fun setView() {
        ageList = mutableListOf()
        for (i in 4..14) {
            ageList.add(ChildAgeModel(index = i, isSelected = false))
        }

        dataBinding.apply {
            childAgeAdapter = ChildAgeAdapter(ageList)
            vpChildAge.apply {
                adapter = childAgeAdapter
                clipToPadding = false
                clipChildren = false
                offscreenPageLimit = 3

                val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.space_x3)
                val offsetPx = resources.getDimensionPixelOffset(R.dimen.space_x4)
                vpChildAge.setPageTransformer { page, position ->
                    val viewPager = page.parent.parent as ViewPager2
                    val offset = position * -(2 * offsetPx + pageMarginPx)
                    if (viewPager.orientation == ORIENTATION_HORIZONTAL) {
                        if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                            page.translationX = -offset
                        } else {
                            page.translationX = offset
                        }
                    } else {
                        page.translationY = offset
                    }
                }

            }
        }
        setViewPager()
    }

    private fun setViewPager() {
        dataBinding.vpChildAge.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tempChildAge = position + 4
                    childAgeAdapter.updateSelectedItem(position)
                }
            })
    }

    private fun initAgeInformationDialog() {
        dialogAgeInformationPopupBinding = DialogKidAgeBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        ageInformationPopupDialog = requireActivity().alertDialog(
            view = dialogAgeInformationPopupBinding.root,
            isCancelable = true,
            fullScreen = true
        )
        ageInformationPopupDialog.window?.setBackgroundDrawableResource(R.color.transparent)

        dialogAgeInformationPopupBinding.tvDesc.makeLinks(
            Pair("Privacy Policy", View.OnClickListener {
                //TODO: Add Privacy Policy page
            })
        )

        dialogAgeInformationPopupBinding.btnOk.setOnClickListener {
            ageInformationPopupDialog.dismiss()
        }

        ageInformationPopupDialog.show()
    }

    private fun setOnClick() {
        dataBinding.apply {
            tvDesc.setOnClickListener(onClickCallback)
            btnNext.setOnClickListener(onClickCallback)
            btnSkip.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.tvDesc -> {
                initAgeInformationDialog()
            }
            dataBinding.btnNext -> {
                viewModel.postCreateKid(
                    CreateChildRequest(
                        name = AppPreference.getTempChildName(),
                        pin = AppPreference.getTempChildProfilePIN(),
                        iconId = AppPreference.getProfileIconId(),
                        age = tempChildAge,
                        settingLocale = "en",
                        settingTimezone = offsetTz
                    )
                )
            }
            dataBinding.btnSkip -> {
                tempChildAge = 5
                viewModel.postCreateKid(
                    CreateChildRequest(
                        name = AppPreference.getTempChildName(),
                        pin = AppPreference.getTempChildProfilePIN(),
                        iconId = AppPreference.getProfileIconId(),
                        age = tempChildAge,
                        settingLocale = "en",
                        settingTimezone = offsetTz
                    )
                )
            }
        }
    }
}