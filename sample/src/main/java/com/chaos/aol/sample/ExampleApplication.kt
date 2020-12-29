package com.chaos.aol.sample

import android.app.Application
import com.chaos.library.aol.jvmti.Jvmti

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Jvmti.attach(this)
    }
}