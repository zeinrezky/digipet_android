package com.stellkey.android.constant

import androidx.annotation.StringDef


@StringDef(
    PetConfigType.PET_GREEN_THEME,
    PetConfigType.PET_ORANGE_THEME,
    PetConfigType.PET_PURPLE_THEME,
    PetConfigType.PET_YELLOW_THEME
)
annotation class PetConfigType {
    companion object {
        const val PET_GREEN_THEME = "green"
        const val PET_ORANGE_THEME = "orange"
        const val PET_PURPLE_THEME = "blue"
        const val PET_YELLOW_THEME = "yellow"
    }
}
