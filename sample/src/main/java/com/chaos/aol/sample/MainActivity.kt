package com.chaos.aol.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chaos.aol.info.ObjectLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showObjectLayout(view: View) {
        ObjectLayout.parse(HashMap::class.java).dumpToLog(classInfo = false)
        ObjectLayout.parse(HashMap::class.java).dumpToLog(classInfo = true)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}