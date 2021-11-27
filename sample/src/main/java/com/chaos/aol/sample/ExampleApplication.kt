package com.chaos.aol.sample

import android.app.Application
import com.chaos.aol.vm.Vm

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Vm.init(this)
    }
}