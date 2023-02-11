package com.stellkey.android.util

object Constant {
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

    object CarerMenu {
        const val HOME = "home"
    }

    object KidWidget {
        const val DIALOG_WHEEL_PICKER = "dialog_wheel_picker"
        const val DIALOG_WHEEL_DISMISSED = "dialog_wheel_dismissed"
    }

    object ExtraParams {
        const val MONTH_WHEEL_PICKER = "month_wheel_picker"
        const val YEAR_WHEEL_PICKER = "year_wheel_picker"
    }

    object OnBoarding {
        const val KID_ICON =
            "https://storage.googleapis.com/stellkey-49e4e.appspot.com/ps_ico1662607860983.jpg"
        const val TASK_PUT_DISHES =
            "https://storage.googleapis.com/stellkey-49e4e.appspot.com/c_ico1663146228142.png"
        const val TASK_MAKE_BED =
            "https://storage.googleapis.com/stellkey-49e4e.appspot.com/c_ico1663146618230.png"
        const val TASK_PUT_DIRTY_CLOTHES =
            "https://storage.googleapis.com/stellkey-49e4e.appspot.com/c_ico1663146802223.png"
        const val TASK_EAT_VEGETABLE =
            "https://storage.googleapis.com/stellkey-49e4e.appspot.com/c_ico1663146176165.png"
        const val TASK_BRUSH_TEETH =
            "https://storage.googleapis.com/stellkey-49e4e.appspot.com/c_ico1663146751666.png"
        const val TASK_PRACTICE_INSTRUMENT =
            "https://storage.googleapis.com/stellkey-49e4e.appspot.com/c_ico1663146395335.png"
    }

    object LogType {
        const val CARER_CREATE_ASSIGNMENT = "create assignment"
        const val CARER_CONFIRM_ASSIGNMENT = "confirm assignment"
        const val KID_COMPLETED_TASK = "completed task"
        const val LIFETIME = "com.stellkey.android.LifetimeSubscription"
    }

    object ResponseStatusCode {
        const val OK = 1
        const val VALIDATION_ERROR = -1
        const val FILE_MISSING = -2
        const val S3_ERROR = -4
        const val SOMETHING_WENT_WRONG = -5
        const val EMPTY_TOKEN = -6
        const val UNAUTHORIZED_ACCESS = -7
        const val RELATION_VIOLATION = -8
        const val FILE_TYPE_WRONG = -9
        const val FILE_UPLOAD_ERROR = -10
        const val ICON_NOT_FOUND = -11
        const val FILE_ERROR = -12
        const val TOKEN_EXPIRED = -13
        const val TOKEN_MALFORMED = -14
        const val NOTHING_TO_DECREMENT = -15
        const val ACCOUNT_NOT_FOUND = -16
        const val USER_ALREADY_EXISTS = -1001
        const val TOKEN_NOT_FOUND = -1002
        const val USER_ALREADY_CONFIRMED = -1003
        const val USER_NOT_FOUND = -1004
        const val NO_TOKEN_PROVIDED = -1005
        const val USER_NOT_VERIFIED = -1009
        const val USER_PIN_EXPIRED = -1010
        const val CARER_ALREADY_SETUP = -1011
        const val USER_BANNED = -10012
        const val CARER_NOT_FOUND = -10013
        const val USER_OLD_PASSWORD_WRONG = -10014
        const val CARER_PIN_WRONG = -10015
        const val USER_EMAIL_MISSING = -10016
        const val USER_EXISTS_BUT_NOT_VERIFIED = -10017
        const val WRONG_PASSWORD = -10018
        const val GLOBAL_REWARD_NOT_FOUND = -2001
        const val REWARD_NOT_FOUND = -2002
        const val REWARD_NOT_IN_VALIDITY_PERIOD = -2003
        const val REWARD_COST_ABOVE_STAR_BALANCE = -2004
        const val REWARD_REDEMPTION_ALREADY_PENDING = -2005
        const val MAX_REWARD_COUNT_FOR_KID = -2006
        const val MAX_REWARD_TYPE_COUNT_FOR_KID = -2007
        const val EMAIL_ALREADY_IN_USE = -1010
        const val KID_NOT_FOUND = -3001
        const val AGE_NOT_PROVIDED = -3002
        const val CHALLENGE_NOT_FOUND = -5001
        const val GLOBAL_CHALLENGE_NOT_FOUND = -5002
        const val CHALLENGE_CATEGORY_NOT_FOUND = -6001
        const val CHALLENGE_CATEGORY_RELATION_VIOLATION = -6002
        const val TASK_ALREADY_ASSIGNED = -7001
        const val MISSING_CHALLENGE_OR_GLOBAL_CHALLENGE = -7002
        const val MAX_TASKS_FOR_CHILD_EXCEEDED = -7003
        const val MAX_REMINDER_COUNT_EXCEEDED = -7004
        const val TASK_NOT_FOUND_OR_ALREADY_CONFIRMED = -7005
        const val TASK_NOT_FOUND_OR_ALREADY_DECLINED = -7006
        const val DATE_MUST_BE_TODAY_OR_FUTURE = -7007
        const val CHALLENGE_OR_GLOBAL_CHALLENGE_MUST_BE_NULL = -7008
        const val CANT_REMIND_YET = -7009
        const val PET_STORE_ITEM_NOT_FOUND = -8001
        const val RUBY_COST_ABOVE_RUBY_BALANCE = -8002
        const val PET_NOT_FOUND = -8003
        const val KID_ALREADY_OWNS_SELECTED_ACCESSORY = -8004
        const val ITEM_MUST_BE_ACCESSORY_OR_DECOR = -8005
        const val STATUS_NOT_ALLOWED = -9997
        const val DB_ERROR = -9999
        const val NOT_ALLOWED = -9998
        const val INVALID_TOKEN = -9996
    }
}