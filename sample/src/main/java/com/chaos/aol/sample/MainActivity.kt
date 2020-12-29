package com.chaos.aol.sample

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chaos.aol.info.InstanceLayout
import com.chaos.aol.vm.Vm
import com.chaos.library.aol.jvmti.Jvmti

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showObjectLayout(view: View) {
        Log.d(TAG, InstanceLayout.parseInstance(Primitive()).toPrintable())
        Log.d(TAG, InstanceLayout.parseInstance(PrimitiveArray()).toPrintable())
        Log.d(TAG, "PrimitiveArray Size: " + Jvmti.getObjectSize(PrimitiveArray()))
        Log.d(TAG, InstanceLayout.parseInstance(Boxed()).toPrintable())
        Log.d(TAG, InstanceLayout.parseInstance(BoxedArray()).toPrintable())
        Log.d(TAG, InstanceLayout.parseInstance(arrayOf("123")).toPrintable())
        Log.d(TAG, "String array Size: " + Jvmti.getObjectSize(arrayOf("1234567")))

        Log.d(TAG, InstanceLayout.parseInstance(byteArrayOf(123)).toPrintable())
        Log.d(TAG, "Byte array[0] Size: " + Jvmti.getObjectSize(emptyArray<Byte>()))
        Log.d(TAG, "Byte array[1] Size: " + Jvmti.getObjectSize(arrayOf<Byte>(123)))
        Log.d(TAG, "Byte array[2] Size: " + Jvmti.getObjectSize(arrayOf<Byte>(123, 123)))
        Log.d(TAG, "primitive byte array[0] Size: " + Jvmti.getObjectSize(byteArrayOf()))
        Log.d(TAG, "primitive byte array[1] Size: " + Jvmti.getObjectSize(byteArrayOf(123)))
        Log.d(TAG, "primitive byte array[2] Size: " + Jvmti.getObjectSize(byteArrayOf(123, 123)))

        Log.d(TAG, InstanceLayout.parseInstance(shortArrayOf(123)).toPrintable())
        Log.d(TAG, "Short array[0] Size: " + Jvmti.getObjectSize(emptyArray<Int>()))
        Log.d(TAG, "Short array[1] Size: " + Jvmti.getObjectSize(arrayOf<Short>(123)))
        Log.d(TAG, "Short array[2] Size: " + Jvmti.getObjectSize(arrayOf<Short>(123, 123)))
        Log.d(TAG, "primitive short array[0] Size: " + Jvmti.getObjectSize(shortArrayOf()))
        Log.d(TAG, "primitive short array[1] Size: " + Jvmti.getObjectSize(shortArrayOf(123)))
        Log.d(TAG, "primitive short array[2] Size: " + Jvmti.getObjectSize(shortArrayOf(123, 123)))

        Log.d(TAG, InstanceLayout.parseInstance(intArrayOf(123)).toPrintable())
        Log.d(TAG, "Integer array[0] Size: " + Jvmti.getObjectSize(emptyArray<Int>()))
        Log.d(TAG, "Integer array[1] Size: " + Jvmti.getObjectSize(arrayOf(123)))
        Log.d(TAG, "Integer array[2] Size: " + Jvmti.getObjectSize(arrayOf(123, 123)))
        Log.d(TAG, "primitive int array[0] Size: " + Jvmti.getObjectSize(intArrayOf()))
        Log.d(TAG, "primitive int array[1] Size: " + Jvmti.getObjectSize(intArrayOf(123)))
        Log.d(TAG, "primitive int array[2] Size: " + Jvmti.getObjectSize(intArrayOf(123, 123)))

        Log.d(TAG, InstanceLayout.parseInstance(longArrayOf(123L)).toPrintable())
        Log.d(TAG, "Long array[0] size: " + Jvmti.getObjectSize(emptyArray<Long>()))
        Log.d(TAG, "Long array[1] size: " + Jvmti.getObjectSize(arrayOf(123L)))
        Log.d(TAG, "Long array[2] size: " + Jvmti.getObjectSize(arrayOf(123L, 123L)))
        Log.d(TAG, "primitive long array[0] size: " + Jvmti.getObjectSize(longArrayOf()))
        Log.d(TAG, "primitive long array[1] size: " + Jvmti.getObjectSize(longArrayOf(123L)))
        Log.d(TAG, "primitive long array[2] size: " + Jvmti.getObjectSize(longArrayOf(123L, 123L)))

        Log.d(TAG, InstanceLayout.parseInstance(floatArrayOf(123f)).toPrintable())
        Log.d(TAG, "Float array[0] size: " + Jvmti.getObjectSize(emptyArray<Long>()))
        Log.d(TAG, "Float array[1] size: " + Jvmti.getObjectSize(arrayOf(123f)))
        Log.d(TAG, "Float array[2] size: " + Jvmti.getObjectSize(arrayOf(123f, 123f)))
        Log.d(TAG, "primitive float array[0] size: " + Jvmti.getObjectSize(floatArrayOf()))
        Log.d(TAG, "primitive float array[1] size: " + Jvmti.getObjectSize(floatArrayOf(123f)))
        Log.d(TAG, "primitive float array[2] size: " + Jvmti.getObjectSize(floatArrayOf(123f, 123f)))

        Log.d(TAG, InstanceLayout.parseInstance(doubleArrayOf()).toPrintable())
        Log.d(TAG, InstanceLayout.parseInstance(emptyArray<Double>()).toPrintable())
        Log.d(TAG, "Double array[0] size: " + Jvmti.getObjectSize(emptyArray<Double>()))
        Log.d(TAG, "Double array[1] size: " + Jvmti.getObjectSize(arrayOf(123.0)))
        Log.d(TAG, "Double array[2] size: " + Jvmti.getObjectSize(arrayOf(123.0, 123.0)))
        Log.d(TAG, "primitive double array[0] size: " + Jvmti.getObjectSize(doubleArrayOf()))
        Log.d(TAG, "primitive double array[1] size: " + Jvmti.getObjectSize(doubleArrayOf(123.0)))
        Log.d(TAG, "primitive double array[2] size: " + Jvmti.getObjectSize(doubleArrayOf(123.0, 123.0)))

        Log.d(TAG, InstanceLayout.parseInstance(charArrayOf('a')).toPrintable())
        Log.d(TAG, "Char array[0] Size: " + Jvmti.getObjectSize(emptyArray<Char>()))
        Log.d(TAG, "Char array[1] Size: " + Jvmti.getObjectSize(arrayOf('a')))
        Log.d(TAG, "Char array[2] Size: " + Jvmti.getObjectSize(arrayOf('a', 'a')))
        Log.d(TAG, "primitive char array[0] Size: " + Jvmti.getObjectSize(charArrayOf()))
        Log.d(TAG, "primitive char array[1] Size: " + Jvmti.getObjectSize(charArrayOf('a')))
        Log.d(TAG, "primitive char array[2] Size: " + Jvmti.getObjectSize(charArrayOf('a', 'a')))

        Log.d(TAG, InstanceLayout.parseInstance(booleanArrayOf()).toPrintable())
        Log.d(TAG, InstanceLayout.parseInstance(emptyArray<Boolean>()).toPrintable())
        Log.d(TAG, "Boolean array[0] Size: " + Jvmti.getObjectSize(emptyArray<Boolean>()))
        Log.d(TAG, "Boolean array[1] Size: " + Jvmti.getObjectSize(arrayOf(true)))
        Log.d(TAG, "Boolean array[2] Size: " + Jvmti.getObjectSize(arrayOf(true, true)))
        Log.d(TAG, "primitive boolean array[0] Size: " + Jvmti.getObjectSize(booleanArrayOf()))
        Log.d(TAG, "primitive boolean array[1] Size: " + Jvmti.getObjectSize(booleanArrayOf(true)))
        Log.d(TAG, "primitive boolean array[2] Size: " + Jvmti.getObjectSize(booleanArrayOf(true, true)))

        Log.d(TAG, "====================")
        Log.d(TAG, "primitive boolean array header: " + Vm.get().arrayHeaderSize(booleanArrayOf()::class.java))
        Log.d(TAG, "primitive byte array header: " + Vm.get().arrayHeaderSize(byteArrayOf()::class.java))
        Log.d(TAG, "primitive char array header: " + Vm.get().arrayHeaderSize(charArrayOf()::class.java))
        Log.d(TAG, "primitive short array header: " + Vm.get().arrayHeaderSize(shortArrayOf()::class.java))
        Log.d(TAG, "primitive int array header: " + Vm.get().arrayHeaderSize(intArrayOf()::class.java))
        Log.d(TAG, "primitive float array header: " + Vm.get().arrayHeaderSize(floatArrayOf()::class.java))
        Log.d(TAG, "primitive long array header: " + Vm.get().arrayHeaderSize(longArrayOf()::class.java))
        Log.d(TAG, "primitive double array header: " + Vm.get().arrayHeaderSize(doubleArrayOf()::class.java))
        Log.d(TAG, "String array header: " + Vm.get().arrayHeaderSize(emptyArray<String>()::class.java))
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}