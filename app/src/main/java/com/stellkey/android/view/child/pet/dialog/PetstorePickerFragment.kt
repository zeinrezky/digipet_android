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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentPetstorePickerBinding
import com.stellkey.android.helper.extension.removeClickListener
import com.stellkey.android.model.PetStore
import com.stellkey.android.model.request.ActivatedItemRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.child.ChildViewModel
import org.koin.android.ext.android.inject
import kotlin.math.abs


class PetstorePickerFragment(var petstoreListener: PetStoreData) : DialogFragment() {

    private lateinit var dataBinding: FragmentPetstorePickerBinding
    private lateinit var imageAccessoriesAdapter: AccessoriesImageAdapter
    private val listOfAccessories = arrayListOf<PetStore>()
    private val viewModel by inject<ChildViewModel>()
    private var selectedPetStore = PetStore()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_petstore_picker,
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

        with(viewModel) {
            kidActivatedItemRequest.observe(viewLifecycleOwner) {
                petstoreListener.onPetstoreSelect(selectedPetStore)
                dismiss()
            }

            kidPurchaseItemPetstore.observe(viewLifecycleOwner) {
                petstoreListener.onPetstoreBuy(it)
                dismiss()
            }

            networkError.observe(viewLifecycleOwner) {
                when (it.toString()) {
                    "RUBY_COST_ABOVE_RUBY_BALANCE" -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.not_enough_gem),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        arguments?.takeIf { it.containsKey(LIST_PETSTORE) }?.apply {
            val userData = if (Build.VERSION.SDK_INT >= 33) {
                getParcelableArrayList(LIST_PETSTORE, PetStore::class.java)
            } else {
                getParcelableArrayList(LIST_PETSTORE)
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
        imageAccessoriesAdapter = AccessoriesImageAdapter(this).apply {
            imageSum = listOfAccessories
        }
        dataBinding.vpItemImage.adapter = imageAccessoriesAdapter
        dataBinding.vpItemImage.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButton(listOfAccessories[position])
            }
        })

        when (listOfAccessories.firstOrNull()?.category) {
            TYPE_ACCESSORIES -> {
                dataBinding.tvPicker.text = getString(R.string.title_accessories)
            }

            TYPE_FOOD -> {
                dataBinding.tvPicker.text = getString(R.string.title_grocery_store)
            }

            TYPE_DECOR -> {
                dataBinding.tvPicker.text = getString(R.string.title_decor)
            }
        }
    }

    private fun updateButton(petStore: PetStore) {
        selectedPetStore = petStore
        if (petStore.owned) {
            isAlreadyOwned(true)
            val currentThemePet = AppPreference.getKidPetColorTheme()
            val currentFoodPet = AppPreference.getKidPetFoodAssignment()
            val currentDecorPet = AppPreference.getKidPetDecorAssignment()
            //checking for anything that currently has attach to pet
            if (currentThemePet == petStore.color || currentFoodPet == petStore.icon || currentDecorPet == petStore.icon) {
                disabledButtonView()
            } else {
                enableButtonView()
                registerListenerButton(petStore, isSelected = true, isBuy = false)
            }
            dataBinding.tvSelect.text = getString(R.string.select)
        } else {
            isAlreadyOwned(false)
            enableButtonView()
            dataBinding.tvSelect.text = "${petStore.ruby_cost}"
            registerListenerButton(petStore, isSelected = false, isBuy = true)
        }
    }

    private fun registerListenerButton(petStore: PetStore, isSelected: Boolean, isBuy: Boolean) {
        dataBinding.ivDoneBtn.setOnClickListener {
            if (isSelected) {
                if(petStore.category == TYPE_FOOD){
                    petstoreListener.onPetstoreSelect(petStore)
                    dismiss()
                }else {
                    viewModel.postKidActivatedItem(ActivatedItemRequest(itemId = petStore.id))
                }
            }
            if (isBuy) {
                viewModel.postKidPurchasePetstoreItem(petStore.id)
            }
        }
    }

    private fun isAlreadyOwned(hasOwned: Boolean) {
        dataBinding.ivDiamond.isGone = hasOwned
    }


    private fun disabledButtonView() {
        dataBinding.ivDoneBtn.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.transparent_black
            )
        )
        dataBinding.tvSelect.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.transparent_black
            )
        )
        dataBinding.ivDoneBtn.removeClickListener()
    }

    private fun enableButtonView() {
        dataBinding.ivDoneBtn.clearColorFilter()
        dataBinding.tvSelect.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
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
        const val LIST_PETSTORE = "LIST_PETSTORE"

        const val TYPE_ACCESSORIES = "accessory"
        const val TYPE_FOOD = "Food"
        const val TYPE_DECOR = "decor"
    }
}