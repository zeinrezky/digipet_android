package com.stellkey.android.constant


/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version PetTheme, v 0.1 24/01/23 22.55 by Nicolas Manurung
 */
class PetTheme(@PetConfigType petConfigType: String) {
    private val petThemeType: PetConfig = PetConfig.get(petConfigType)

    val gigglePose: Int get() = petThemeType.gigglePose
    val hungryPose: Int get() = petThemeType.hungryPose
    val normalPose: Int get() = petThemeType.normalPose
    val yummyPose: Int get() = petThemeType.yummyPose
    val angryPose: Int get() = petThemeType.angryPose
    val happyPose: Int get() = petThemeType.happyPose
}