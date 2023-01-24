package com.stellkey.android.constant

import androidx.annotation.RawRes
import com.stellkey.android.R


sealed class PetConfig(
    @RawRes val gigglePose: Int,
    @RawRes val hungryPose: Int,
    @RawRes val normalPose: Int,
    @RawRes val yummyPose: Int,
    @RawRes val angryPose: Int,
    @RawRes val happyPose: Int
) {
    object GreenPet : PetConfig(
        R.raw.green_giggle_pose,
        R.raw.green_hungry_pose,
        R.raw.green_normal_pose,
        R.raw.green_yummy_pose,
        R.raw.green_angry_pose,
        R.raw.green_happy_pose
    )

    object OrangePet : PetConfig(
        R.raw.orange_giggle_pose,
        R.raw.orange_hungry_pose,
        R.raw.orange_normal_pose,
        R.raw.orange_yummy_pose,
        R.raw.orange_angry_pose,
        R.raw.orange_happy_pose
    )

    object PurplePet : PetConfig(
        R.raw.purple_giggle_pose,
        R.raw.purple_hungry_pose,
        R.raw.purple_normal_pose,
        R.raw.purple_yummy_pose,
        R.raw.purple_angry_pose,
        R.raw.purple_happy_pose
    )

    object YellowPet : PetConfig(
        R.raw.yellow_giggle_pose,
        R.raw.yellow_hungry_pose,
        R.raw.yellow_normal_pose,
        R.raw.yellow_yummy_pose,
        R.raw.yellow_angry_pose,
        R.raw.yellow_happy_pose
    )

    companion object {
        fun get(
            @PetConfigType petConfigType: String
        ): PetConfig = when (petConfigType) {
            PetConfigType.PET_GREEN_THEME -> GreenPet
            PetConfigType.PET_ORANGE_THEME -> OrangePet
            PetConfigType.PET_PURPLE_THEME -> PurplePet
            PetConfigType.PET_YELLOW_THEME -> YellowPet
            else -> OrangePet
        }
    }
}