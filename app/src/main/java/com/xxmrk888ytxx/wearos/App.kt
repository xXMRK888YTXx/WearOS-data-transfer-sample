package com.xxmrk888ytxx.wearos

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FakeDI.provideContext(this)
    }
}