package com.chaos.aol.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chaos.aol.info.InstanceLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showObjectLayout(view: View) {
        InstanceLayout.parseInstance("123").dumpToLog()
        InstanceLayout.parseInstance("1234").dumpToLog()
        InstanceLayout.parseInstance("123456").dumpToLog()
        InstanceLayout.parseInstance("12345678").dumpToLog()
        InstanceLayout.parseInstance("1234567890").dumpToLog()

        InstanceLayout.parseInstance(Primitive()).dumpToLog()

        InstanceLayout.parseInstance(Class::class.java).dumpToLog()
        InstanceLayout.parseInstance(Int::class.java).dumpToLog()

        InstanceLayout.parseInstance(PrimitiveArray()).dumpToLog()
        InstanceLayout.parseInstance(Boxed()).dumpToLog()
        InstanceLayout.parseInstance(BoxedArray()).dumpToLog()
        InstanceLayout.parseInstance(arrayOf("123")).dumpToLog()

        InstanceLayout.parseInstance(byteArrayOf(123)).dumpToLog()

        InstanceLayout.parseInstance(shortArrayOf(123)).dumpToLog()

        InstanceLayout.parseInstance(intArrayOf(123)).dumpToLog()

        InstanceLayout.parseInstance(longArrayOf(123L)).dumpToLog()

        InstanceLayout.parseInstance(floatArrayOf(123f)).dumpToLog()

        InstanceLayout.parseInstance(doubleArrayOf()).dumpToLog()
        InstanceLayout.parseInstance(emptyArray<Double>()).dumpToLog()

        InstanceLayout.parseInstance(charArrayOf('a', 'b')).dumpToLog()

        InstanceLayout.parseInstance(booleanArrayOf()).dumpToLog()
        InstanceLayout.parseInstance(emptyArray<Boolean>()).dumpToLog()

        InstanceLayout.parseInstance(Foo()).dumpToLog()

        InstanceLayout.parseClass(Class.forName("java.util.HashMap")).dumpToLog()
        InstanceLayout.parseClass(Class.forName("java.lang.reflect.Method")).dumpToLog()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}