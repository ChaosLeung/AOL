package com.chaos.aol.jvmti

interface Jvmti {
    fun getObjectSize(o: Any): Int
}

object JvmtiProvider {
    private lateinit var jvmti: Jvmti

    fun get(): Jvmti? = if (JvmtiProvider::jvmti.isInitialized) jvmti else null

    fun attach(jvmti: Jvmti) {
        this.jvmti = jvmti
    }
}