package com.neo.hash

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import timber.log.Timber


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        //timber init
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.plant(CrashReportingTree())

        //ads init
        MobileAds.initialize(this) {
            Timber.i("AdMob initialized")
        }

        if (Firebase.auth.currentUser == null) {
            //anonymous firebase login
            Firebase.auth.signInAnonymously()
        }
    }
}

class CrashReportingTree : Timber.Tree() {

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    ) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        if (t != null) {
            Firebase.crashlytics.recordException(t)
        } else {
            Firebase.crashlytics.log(tag?.let { "$it : $message" } ?: message)
        }
    }
}