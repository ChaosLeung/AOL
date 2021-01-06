package com.chaos.aol.sample

import android.app.Application
import com.chaos.aol.jvmti.JvmtiImpl

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        JvmtiImpl.attach(this)
    }
}