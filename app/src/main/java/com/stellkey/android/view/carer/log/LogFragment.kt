package com.stellkey.android.view.carer.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentLogBinding
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.HomeViewModel
import org.koin.android.ext.android.inject

class LogFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentLogBinding

    //private val binding by viewBinding<FragmentLogBinding>()
    private val viewModel by inject<HomeViewModel>()

    companion object {
        @JvmStatic
        fun newInstance() = LogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_log,
                container,
                false
            )
        return dataBinding.root
    }

}