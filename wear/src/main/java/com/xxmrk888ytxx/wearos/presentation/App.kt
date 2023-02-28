package com.xxmrk888ytxx.wearos.presentation

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FakeDI.provideContext(this)
    }
}