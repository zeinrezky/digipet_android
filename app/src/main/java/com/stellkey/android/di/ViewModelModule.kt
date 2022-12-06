package com.stellkey.android.di

import com.stellkey.android.view.carer.account.AccountViewModel
import com.stellkey.android.view.carer.family.FamilyViewModel
import com.stellkey.android.view.carer.home.HomeViewModel
import com.stellkey.android.view.carer.profile.ProfileViewModel
import com.stellkey.android.view.child.ChildViewModel
import com.stellkey.android.view.intro.auth.LoginViewModel
import com.stellkey.android.view.intro.auth.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { FamilyViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { AccountViewModel(get()) }
    viewModel { ChildViewModel(get()) }
}