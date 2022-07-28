package com.neo.hash

import android.app.Application
import com.google.android.gms.ads.MobileAds
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        MobileAds.initialize(this) {
            Timber.i("AdMob initialized")
        }
    }

}