package com.ktproject.autoservice.di

import com.ktproject.autoservice.data.repository.NewsRepository
import com.ktproject.autoservice.data.repository.RequestRepository
import com.ktproject.autoservice.data.repository.ServiceRepository
import com.ktproject.autoservice.data.repository.UserRepository
import com.ktproject.autoservice.data.repository.fake.FakeNewsRepository
import com.ktproject.autoservice.data.repository.fake.FakeRequestRepository
import com.ktproject.autoservice.data.repository.fake.FakeServiceRepository
import com.ktproject.autoservice.data.repository.fake.FakeUserRepository
import com.ktproject.autoservice.ui.viewmodel.AuthViewModel
import com.ktproject.autoservice.ui.viewmodel.HomeViewModel
import com.ktproject.autoservice.ui.viewmodel.ServicesViewModel
import com.ktproject.autoservice.ui.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single<ServiceRepository> { FakeServiceRepository() }
    single<UserRepository> { FakeUserRepository(get()) }
    single<NewsRepository> { FakeNewsRepository(get()) }
    single<RequestRepository> { FakeRequestRepository(get(), get()) }

//    viewModel { ServicesViewModel() }
    viewModel { SplashViewModel(get(), get()) }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
}
