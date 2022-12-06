package com.stellkey.android.di

import com.stellkey.android.helper.PermissionHelper
import com.stellkey.android.helper.UtilityHelper
import org.koin.dsl.module

val HelperModule = module {
    single { PermissionHelper() }
    single { UtilityHelper() }
}