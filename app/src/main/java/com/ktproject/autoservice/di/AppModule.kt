package com.ktproject.autoservice.di

import com.ktproject.autoservice.data.remote.RequestApi
import com.ktproject.autoservice.data.remote.UserApi
import com.ktproject.autoservice.data.local.TokenDataStore
import com.ktproject.autoservice.data.remote.ApiClient
//import com.ktproject.autoservice.data.remote.UserApi
//import com.ktproject.autoservice.data.remote.ServiceApi
//import com.ktproject.autoservice.data.remote.NewsApi
//import com.ktproject.autoservice.data.remote.RequestApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    // DataStore
    single { TokenDataStore(get()) }

    // ApiClient
    single { ApiClient(baseUrl = "https://your-server-url.com") }

    // APIs
    single { UserApi(get()) }
//    single { RequestApi(get()) }
//    single { ServiceApi(get()) }
//    single { NewsApi(get()) }
}
