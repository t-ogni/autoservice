package com.ktproject.autoservice.di

import com.ktproject.autoservice.data.local.TokenDataStore
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.data.remote.UserApi
import com.ktproject.autoservice.data.repository.NewsRepository
import com.ktproject.autoservice.data.repository.RequestRepository
import com.ktproject.autoservice.data.repository.ServiceRepository
import com.ktproject.autoservice.data.repository.UserRepository
import com.ktproject.autoservice.data.repository.fake.FakeNewsRepository
import com.ktproject.autoservice.data.repository.fake.FakeRequestRepository
import com.ktproject.autoservice.data.repository.fake.FakeServiceRepository
import com.ktproject.autoservice.data.repository.fake.FakeUserRepository
import com.ktproject.autoservice.ui.viewmodel.AdminRequestsViewModel
import com.ktproject.autoservice.ui.viewmodel.AdminUsersViewModel
import com.ktproject.autoservice.ui.viewmodel.AuthViewModel
import com.ktproject.autoservice.ui.viewmodel.EditProfileViewModel
import com.ktproject.autoservice.ui.viewmodel.EditUserViewModel
import com.ktproject.autoservice.ui.viewmodel.HomeViewModel
import com.ktproject.autoservice.ui.viewmodel.MyRequestsViewModel
import com.ktproject.autoservice.ui.viewmodel.NewRequestViewModel
import com.ktproject.autoservice.ui.viewmodel.NewsViewModel
import com.ktproject.autoservice.ui.viewmodel.ProfileViewModel
import com.ktproject.autoservice.ui.viewmodel.ServicesViewModel
import com.ktproject.autoservice.ui.viewmodel.SplashViewModel
import com.ktproject.autoservice.ui.viewmodel.UserRequestsViewModel
import com.ktproject.autoservice.ui.viewmodel.UserViewModel
import com.ktproject.autoservice.ui.views.admin.requests.AdminRequestDetailScreen
import com.ktproject.autoservice.ui.views.admin.user.AdminUserDetailsScreen
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // DataStore
    single { TokenDataStore(get()) }

    // ApiClient
    single { ApiClient(baseUrl = "https://your-server-url.com") }

    // APIs
    single { UserApi(get()) }

    single<ServiceRepository> { FakeServiceRepository(get()) }
    single<UserRepository> { FakeUserRepository(get()) }
    single<NewsRepository> { FakeNewsRepository(get()) }
    single<RequestRepository> { FakeRequestRepository(get(), get()) }

    viewModel { SplashViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { HomeViewModel(get(), get(), get()) }

    viewModel { ServicesViewModel(get()) }
    viewModel { NewsViewModel(get()) }

    viewModel { ProfileViewModel(get()) }
    viewModel { EditProfileViewModel(get()) }

    viewModel { UserViewModel(get()) }
    viewModel { EditUserViewModel(get()) }
    viewModel { AdminUsersViewModel(get()) }

    viewModel { MyRequestsViewModel(get()) }
    viewModel { AdminRequestsViewModel(get()) }
    viewModel { UserRequestsViewModel(get()) }

    viewModel { NewRequestViewModel(get(), get(), get()) }

}
