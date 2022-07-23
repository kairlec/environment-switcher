package com.kairlec.envsr.impl.win

import com.kairlec.envsr.impl.win.EnvironmentType.Machine
import com.kairlec.envsr.impl.win.NativeSupportJna.Companion.EnvironmentTargetMachine
import com.kairlec.envsr.impl.win.NativeSupportJna.Companion.EnvironmentTargetProcess
import com.kairlec.envsr.impl.win.NativeSupportJna.Companion.EnvironmentTargetUser
import com.sun.jna.Library
import com.sun.jna.Memory
import com.sun.jna.Native
import com.sun.jna.Pointer
import java.io.File

private interface NativeSupportJna : Library {
    fun notifyEnvironmentChange(): Boolean
    fun getAllEnvironment(type: Int, buffer: Pointer): Long
    fun setEnvironmentVariable(name: String, value: String, type: Int)
    fun isAdministrator(): Boolean

    companion object {
        const val EnvironmentTargetProcess = 0
        const val EnvironmentTargetMachine = 1
        const val EnvironmentTargetUser = 2
    }
}

object NativeSupport {
    private val instance: NativeSupportJna by lazy {
        Native.load("EnvsrWinNativeCaller", NativeSupportJna::class.java)
    }

    fun notifyEnvironmentChange(): Boolean {
        return instance.notifyEnvironmentChange()
    }

    fun getAllEnvironment(type: EnvironmentType): Map<String, String> {
        val point = Memory(Native.BOOL_SIZE * 65535L)
        val length = instance.getAllEnvironment(type.typeCode, point)

        val bytes = point.getByteArray(0, length.toInt())

        val result = HashMap<String, String>()
        var key: String? = null

        var last = 0
        var current = 0
        while (current < length) {
            val b = bytes[current]
            if (b == 0.toByte()) {
                val v = String(bytes, last, current - last)
                if (key == null) {
                    key = v
                } else {
                    result[key] = v
                    key = null
                }
                last = current + 1
            }
            current++
        }
        return result
    }

    fun setEnvironmentVariable(name: String, value: String, type: EnvironmentType) {
        instance.setEnvironmentVariable(name, value, type.typeCode)
    }

    fun isAdministrator(): Boolean {
        return instance.isAdministrator()
    }
}

enum class EnvironmentType(internal val typeCode: Int) {
    Process(EnvironmentTargetProcess),
    Machine(EnvironmentTargetMachine),
    User(EnvironmentTargetUser),
}

fun main() {
    println(NativeSupport.getAllEnvironment(Machine))
}