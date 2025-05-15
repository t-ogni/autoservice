package com.ktproject.autoservice.di

import com.ktproject.autoservice.data.local.TokenDataStore
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.component.carList.carListClient
import com.ktproject.autoservice.data.repository.NewsRepository
import com.ktproject.autoservice.data.repository.RequestRepository
import com.ktproject.autoservice.data.repository.ServiceRepository
import com.ktproject.autoservice.data.repository.UserRepository
import com.ktproject.autoservice.data.repository.fake.FakeNewsRepository
import com.ktproject.autoservice.data.repository.fake.FakeRequestRepository
import com.ktproject.autoservice.data.repository.fake.FakeServiceRepository
import com.ktproject.autoservice.data.repository.fake.FakeUserRepository
import com.ktproject.autoservice.component.carList.CarRepository
import com.ktproject.autoservice.component.carList.SelectedMakeStore
import com.ktproject.autoservice.data.repository.runtime.NewRequestDraftRepository
import com.ktproject.autoservice.ui.viewmodel.AdminRequestsViewModel
import com.ktproject.autoservice.ui.viewmodel.AdminUsersViewModel
import com.ktproject.autoservice.ui.viewmodel.AuthViewModel
import com.ktproject.autoservice.ui.viewmodel.EditProfileViewModel
import com.ktproject.autoservice.ui.viewmodel.EditUserViewModel
import com.ktproject.autoservice.ui.viewmodel.HomeViewModel
import com.ktproject.autoservice.ui.viewmodel.MyRequestsViewModel
import com.ktproject.autoservice.ui.viewmodel.NewRequestViewModel
import com.ktproject.autoservice.ui.viewmodel.AdminNewsViewModel
import com.ktproject.autoservice.ui.viewmodel.AdminServiceViewModel
import com.ktproject.autoservice.ui.viewmodel.CarSearchViewModel
import com.ktproject.autoservice.ui.viewmodel.NewsDetailViewModel
import com.ktproject.autoservice.ui.viewmodel.ProfileViewModel
import com.ktproject.autoservice.ui.viewmodel.ServicesViewModel
import com.ktproject.autoservice.ui.viewmodel.SplashViewModel
import com.ktproject.autoservice.ui.viewmodel.UserRequestsViewModel
import com.ktproject.autoservice.ui.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // DataStore
    single { TokenDataStore(get()) }

    // ApiClient
    single { ApiClient(baseUrl = "https://your-server-url.com") }

    // Car Brands Online Api
    single { carListClient }
    single { CarRepository(get()) }
    single { SelectedMakeStore(get()) }
    viewModel { CarSearchViewModel(get(), get()) }

    single<ServiceRepository> { FakeServiceRepository(get()) }
    single<UserRepository> { FakeUserRepository(get()) }
    single<NewsRepository> { FakeNewsRepository(get()) }
    single<RequestRepository> { FakeRequestRepository(get(), get()) }

    // Репозиторий черновиков (один на всё приложение)
    single { NewRequestDraftRepository() }

    viewModel { SplashViewModel(get()) }
    viewModel { AuthViewModel(get()) }

    viewModel { HomeViewModel(get(), get(), get(), get()) }

    viewModel { NewsDetailViewModel(get()) }
    viewModel { AdminNewsViewModel(get()) }

    viewModel { ServicesViewModel(get()) }
    viewModel { AdminServiceViewModel(get()) }

    viewModel { ProfileViewModel(get()) }
    viewModel { EditProfileViewModel(get()) }

    viewModel { UserViewModel(get()) }
    viewModel { EditUserViewModel(get()) }
    viewModel { AdminUsersViewModel(get()) }

    viewModel { MyRequestsViewModel(get()) }
    viewModel { AdminRequestsViewModel(get(), get()) }
    viewModel { UserRequestsViewModel(get()) }

    viewModel { NewRequestViewModel(get(), get(), get(), get()) }

}
