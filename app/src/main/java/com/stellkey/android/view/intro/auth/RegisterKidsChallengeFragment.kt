package com.stellkey.android.view.intro.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentRegisterKidsChallengeBinding
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.helper.extension.emptyInt
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.KidGlobalChallengeModel
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.intro.auth.adapter.GlobalChallengeAdapter
import com.stellkey.android.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_register_kids_challenge.*
import org.koin.android.ext.android.inject

class RegisterKidsChallengeFragment : BaseFragment(), GlobalChallengeAdapter.Listener {

    private lateinit var dataBinding: FragmentRegisterKidsChallengeBinding
    private val viewModel by inject<RegisterViewModel>()

    private var selectedChallengeId = emptyInt

    companion object {
        @JvmStatic
        fun newInstance() = RegisterKidsChallengeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_register_kids_challenge,
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
                this@RegisterKidsChallengeFragment
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
            dataBinding.btnNext -> {
                AppPreference.putTempSelectedGlobalId(selectedChallengeId)
                addFragment(RegisterChallengeStartDateFragment.newInstance())
            }
        }
    }
}