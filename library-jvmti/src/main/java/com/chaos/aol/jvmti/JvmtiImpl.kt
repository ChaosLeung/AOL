package com.chaos.aol.jvmti

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Debug
import android.util.Log
import com.chaos.aol.vm.VirtualMachine
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

object JvmtiImpl : Jvmti {

    private const val TAG = "Jvmti"

    private const val AGENT_NAME = "aol-jvmti-agent"

    private var attached: Boolean = false

    private fun isJvmtiSupport(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    @SuppressLint("SoonBlockedPrivateApi")
    fun attach(context: Context) {
        if (attached || !isJvmtiSupport()) {
            return
        }

        attached = true

        val originPath = context.applicationInfo.nativeLibraryDir + File.separatorChar + "lib" + AGENT_NAME + ".so"
        val targetDir = File(context.filesDir, "jvmti")
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }
        val targetFile = File(targetDir, "$AGENT_NAME.so")
        if (targetFile.exists()) {
            targetFile.delete()
        }
        val agentLibPath = targetFile.absolutePath
        Files.copy(Paths.get(originPath), Paths.get(agentLibPath))

        // TODO: 当前版本已经 copy 过后面不需要再 copy

        Log.d(TAG, "attach: $agentLibPath")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Debug.attachJvmtiAgent(agentLibPath, null, context.classLoader)
        } else {
            val vmDebugClass = Class.forName("dalvik.system.VMDebug")
            val attachAgentMethod = vmDebugClass.getDeclaredMethod("attachAgent", String::class.java)
                .apply { isAccessible = true }
            attachAgentMethod.invoke(null, agentLibPath)
        }

        System.loadLibrary(AGENT_NAME)

        JvmtiProvider.attach(this)
    }

    override fun getObjectSize(o: Any): Int {
        if (!isJvmtiSupport()) {
            return VirtualMachine.UNKNOWN_SIZE
        }
        return nativeGetObjectSize(o).toInt()
    }

    @JvmStatic
    private external fun nativeGetObjectSize(o: Any): Long
}