package com.neo.hash

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.neo.hash.data.DataStoreRepository
import com.neo.hash.data.DataStoreRepositoryImpl
import timber.log.Timber

lateinit var dataStoreRepository: DataStoreRepository

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        MobileAds.initialize(this) {
            Timber.i("AdMob initialized")
        }

        dataStoreRepository = DataStoreRepositoryImpl(applicationContext)

        //offline mode
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

}