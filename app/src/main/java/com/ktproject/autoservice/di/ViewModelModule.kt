package com.ktproject.autoservice.di

import com.ktproject.autoservice.ui.viewmodel.ServicesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ServicesViewModel() }
}
