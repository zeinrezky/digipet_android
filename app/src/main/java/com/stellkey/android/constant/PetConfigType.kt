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
        const val PET_GREEN_THEME = "PET_GREEN_THEME"
        const val PET_ORANGE_THEME = "PET_ORANGE_THEME"
        const val PET_PURPLE_THEME = "PET_PURPLE_THEME"
        const val PET_YELLOW_THEME = "PET_YELLOW_THEME"
    }
}
