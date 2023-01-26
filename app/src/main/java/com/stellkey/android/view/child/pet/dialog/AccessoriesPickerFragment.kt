package com.stellkey.android.view.child.pet.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentAccessoriesPickerBinding
import com.stellkey.android.model.PetStore
import kotlin.math.abs


class AccessoriesPickerFragment : DialogFragment() {

    private lateinit var dataBinding: FragmentAccessoriesPickerBinding
    private lateinit var imageAccessoriesAdapter: AccessoriesImageAdapter

    private val listOfAccessories = arrayListOf<PetStore>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_accessories_picker,
            container,
            false
        )

        dialog?.let {
            it.window?.requestFeature(Window.FEATURE_NO_TITLE)
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(LIST_ACCESSORIES) }?.apply {
            val userData = if (Build.VERSION.SDK_INT >= 33) {
                getParcelableArrayList(LIST_ACCESSORIES, PetStore::class.java)
            } else {
                getParcelableArrayList(LIST_ACCESSORIES)
            }
            userData?.let {
                listOfAccessories.addAll(it)
            }
        }
        setView()
        setOnClick()


    }

    @SuppressLint("SetTextI18n")
    private fun setView() {
        dataBinding.tvPicker.text = "Accessories"

        imageAccessoriesAdapter = AccessoriesImageAdapter(this).apply {
            imageSum = listOfAccessories
        }

        dataBinding.vpItemImage.adapter = imageAccessoriesAdapter

        TabLayoutMediator(dataBinding.tlItemImage, dataBinding.vpItemImage) { tab, position ->

        }.attach()
    }

    private fun setOnClick() {
        dataBinding.ivClose.setOnClickListener {
            dismiss()
        }

        dataBinding.btnLeftSwipe.setOnClickListener {
            dataBinding.vpItemImage.setCurrentItem(getNextPossibleItemIndex(-1), true)
        }

        dataBinding.btnRightSwipe.setOnClickListener {
            dataBinding.vpItemImage.setCurrentItem(getNextPossibleItemIndex(1), true)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog ?: return
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)

    }

    private fun getNextPossibleItemIndex(change: Int): Int {
        val currentIndex: Int = dataBinding.vpItemImage.currentItem
        val total: Int = dataBinding.vpItemImage.adapter?.itemCount ?: 0
        return if (currentIndex + change < 0) {
            0
        } else abs((currentIndex + change) % total)
    }

    inner class AccessoriesImageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        var imageSum = arrayListOf<PetStore>()
        override fun getItemCount(): Int = imageSum.size

        override fun createFragment(position: Int): Fragment {
            val fragment = ImageContainerItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ImageContainerItemFragment.PETSTORE_KEY, imageSum[position])
                }
            }

            return fragment
        }

    }

    companion object {
        const val TAG = "AccessoriesPickerFragment"
        const val LIST_ACCESSORIES = "LIST_ACCESSORIES"
    }
}