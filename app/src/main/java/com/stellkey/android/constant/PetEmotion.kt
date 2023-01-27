package com.stellkey.android.constant

import androidx.annotation.StringDef


@StringDef(
    PetEmotion.PET_EMOTION_HUNGRY,
    PetEmotion.PET_EMOTION_NORMAL,
    PetEmotion.PET_EMOTION_YUMMY,
    PetEmotion.PET_EMOTION_ANGRY,
    PetEmotion.PET_EMOTION_HAPPY,
    PetEmotion.PET_EMOTION_GIGGLE
)
annotation class PetEmotion {
    companion object{
        const val PET_EMOTION_HUNGRY = "hungry"
        const val PET_EMOTION_NORMAL = "normal"
        const val PET_EMOTION_YUMMY = "yummy"
        const val PET_EMOTION_ANGRY = "angry"
        const val PET_EMOTION_HAPPY = "happy"
        const val PET_EMOTION_GIGGLE = "giggle"
    }
}