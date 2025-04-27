    package com.ktproject.autoservice

    import android.app.Application
    import com.ktproject.autoservice.di.*
    import org.koin.android.ext.koin.androidContext
    import org.koin.core.context.startKoin

    class App : Application() {
        override fun onCreate() {
            super.onCreate()

            startKoin {
                androidContext(this@App)
                modules(appModule, viewModelModule)
            }
        }
    }
