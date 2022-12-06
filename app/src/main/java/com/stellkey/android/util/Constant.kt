package com.stellkey.android.util

object Constant {
    const val BASE_URL = "https://stellkey-api.mediatropy.com/"

    object Header {
        const val AUTH = "Authorization"
        const val CACHE = "Cache-Control"
    }

    object ProfileIconType {
        const val PROFILE_ICON_CARER = "carer"
        const val PROFILE_ICON_KID = "kid"
    }

    object SortBy {
        const val ASC = "asc"
        const val DESC = "desc"
    }

    object FamilyType {
        const val KIDS = "Kids"
        const val CARERS = "Carers"
        const val ADMIN = "Admin"
    }

    object SubscriptionType {
        const val TRIAL = "com.stelkey.android.TrialSubscription"
        const val MONTHLY = "com.stellkey.android.MonthlySubscription"
        const val YEARLY = "com.stellkey.android.YearlySubscription"
        const val LIFETIME = "com.stellkey.android.LifetimeSubscription"
    }

    object KidMenu {
        const val REWARD = "reward"
        const val HOME = "home"
        const val LOG = "log"
    }

    object OnBoarding {
        const val KID_ICON = "https://storage.googleapis.com/stellkey-49e4e.appspot.com/ps_ico1662607860983.jpg"
        const val TASK_PUT_DISHES = "https://storage.googleapis.com/stellkey-49e4e.appspot.com/c_ico1663146228142.png"
        const val TASK_MAKE_BED = "https://storage.googleapis.com/stellkey-49e4e.appspot.com/c_ico1663146618230.png"
        const val TASK_PUT_DIRTY_CLOTHES = "https://storage.googleapis.com/stellkey-49e4e.appspot.com/c_ico1663146802223.png"
        const val TASK_EAT_VEGETABLE = "https://storage.googleapis.com/stellkey-49e4e.appspot.com/c_ico1663146176165.png"
        const val TASK_BRUSH_TEETH = "https://storage.googleapis.com/stellkey-49e4e.appspot.com/c_ico1663146751666.png"
        const val TASK_PRACTICE_INSTRUMENT = "https://storage.googleapis.com/stellkey-49e4e.appspot.com/c_ico1663146395335.png"
    }
}