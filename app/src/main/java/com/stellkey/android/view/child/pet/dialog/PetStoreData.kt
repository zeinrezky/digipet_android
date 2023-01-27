package com.stellkey.android.view.child.pet.dialog

import com.stellkey.android.model.PetStore


/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version PetStoreData, v 0.1 27/01/23 20.47 by Nicolas Manurung
 */
interface PetStoreData {

    fun onPetstoreSelect(onSelect: PetStore)

    fun onPetstoreBuy(onBuy: PetStore)
}