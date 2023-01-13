package com.stellkey.android.view.child.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stellkey.android.R

class ChildLogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_log, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ChildLogFragment()
    }
}