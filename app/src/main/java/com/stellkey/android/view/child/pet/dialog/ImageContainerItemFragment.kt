package com.stellkey.android.view.child.pet.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentImageContainerItemBinding
import com.stellkey.android.helper.extension.loadImage

class ImageContainerItemFragment : Fragment() {

    private lateinit var dataBinding: FragmentImageContainerItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_image_container_item,
            container,
            false
        )
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(IMAGE_KEY) }?.apply {
            dataBinding.ivImageItem.loadImage(getString(IMAGE_KEY).toString())
        }
    }

    companion object {
        const val IMAGE_KEY = "IMAGE_KEY"
    }
}