package com.stellkey.android.view.child.pet.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentImageContainerItemBinding
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.model.PetStore

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
        arguments?.takeIf { it.containsKey(PETSTORE_KEY) }?.apply {
            val userData = if (Build.VERSION.SDK_INT >= 33) {
                getParcelable(PETSTORE_KEY, PetStore::class.java)
            } else {
                getParcelable(PETSTORE_KEY)
            }
            userData?.let { mapData(it) }
        }
    }

    private fun mapData(petStore: PetStore) {
        dataBinding.ivImageItem.loadImage(petStore.icon)
        dataBinding.tvItem.text = petStore.title
    }

    companion object {
        const val PETSTORE_KEY = "PETSTORE_KEY"
    }
}