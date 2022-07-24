package com.kairlec.envsr.impl.win

import com.kairlec.envsr.impl.win.EnvironmentType.Machine
import com.kairlec.envsr.impl.win.EnvironmentType.User
import com.kairlec.envsr.impl.win.NativeSupportJna.Companion.EnvironmentTargetMachine
import com.kairlec.envsr.impl.win.NativeSupportJna.Companion.EnvironmentTargetProcess
import com.kairlec.envsr.impl.win.NativeSupportJna.Companion.EnvironmentTargetUser
import com.sun.jna.Library
import com.sun.jna.Memory
import com.sun.jna.Native
import com.sun.jna.Pointer

private interface NativeSupportJna : Library {
    fun notifyEnvironmentChange(): Boolean
    fun getAllEnvironment(type: Int, buffer: Pointer): Long
    fun setEnvironmentVariable(name: String, value: String, type: Int)
    fun isAdministrator(): Boolean
    fun getEnvironmentVariable(name: String, type: Int, buffer: Pointer): Long
    fun expandEnvironmentVariable(path: String, buffer: Pointer): Long

    companion object {
        const val EnvironmentTargetProcess = 0
        const val EnvironmentTargetMachine = 1
        const val EnvironmentTargetUser = 2
    }
}

object WindowsNativeSupport {
    private val instance: NativeSupportJna by lazy {
        Native.load("EnvsrWinNativeCaller", NativeSupportJna::class.java)
    }

    fun getEnvironmentVariable(name: String, type: EnvironmentType): String? {
        val buffer = Memory(Native.BOOL_SIZE * type.valueBufferSize)
        val length = instance.getEnvironmentVariable(name, type.typeCode, buffer)
        if (length == -1L) {
            return null
        }
        return String(buffer.getByteArray(0, length.toInt()))
    }

    fun expandEnvironmentVariable(name: String): String {
        val buffer = Memory(Native.BOOL_SIZE * 32767L)
        val length = instance.expandEnvironmentVariable(name, buffer)
        return String(buffer.getByteArray(0, length.toInt()))
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
        require(value.isNotEmpty()) { "value must not be empty, if you want to delete a environment value, please use deleteEnvironmentVariable instead of setEnvironmentVariable." }
        type.keyCheck(name)
        type.valueCheck(value)
        instance.setEnvironmentVariable(name, value, type.typeCode)
    }

    fun deleteEnvironmentVariable(name: String, type: EnvironmentType) {
        instance.setEnvironmentVariable(name, "", type.typeCode)
    }

    fun isAdministrator(): Boolean {
        return instance.isAdministrator()
    }

    val log = mu.KotlinLogging.logger { }
}

enum class EnvironmentType(internal val typeCode: Int) {
    Process(EnvironmentTargetProcess) {
        override val keyBufferSize: Long
            get() = 32767

        override val valueBufferSize: Long
            get() = 32767

        override fun keyCheck(key: String) {
            if (key.contains('=')) {
                // https://docs.microsoft.com/en-us/windows/win32/procthread/environment-variables
                throw IllegalArgumentException("Process environment variable name must not contain '='")
            }
            if (key.length >= 32767) {
                // https://docs.microsoft.com/en-us/dotnet/api/system.environment.setenvironmentvariable
                throw IllegalArgumentException("key length must less than 32767 characters in process environment.")
            }
        }

        override fun valueCheck(value: String) {
            if (value.length >= 32767) {
                // https://docs.microsoft.com/en-us/dotnet/api/system.environment.setenvironmentvariable
                throw IllegalArgumentException("value length must less than 32767 characters in process environment.")
            }
        }

    },
    Machine(EnvironmentTargetMachine) {
        override val keyBufferSize: Long
            get() = 255

        override val valueBufferSize: Long
            get() = 32767

        override fun keyCheck(key: String) {
            if (key.contains('=')) {
                // https://docs.microsoft.com/en-us/windows/win32/procthread/environment-variables
                throw IllegalArgumentException("Machine environment variable name must not contain '='")
            }
            if (key.length >= 255) {
                // https://docs.microsoft.com/en-us/dotnet/api/system.environment.setenvironmentvariable
                throw IllegalArgumentException("key length must less than 255 characters in machine environment.")
            }
        }

        override fun valueCheck(value: String) {
            if (value.length >= 32767) {
                // https://docs.microsoft.com/en-us/dotnet/api/system.environment.setenvironmentvariable
                throw IllegalArgumentException("value length must less than 32767 characters in machine environment.")
            }
            if (value.length >= 2048) {
                // https://docs.microsoft.com/en-us/dotnet/api/system.environment.setenvironmentvariable
                WindowsNativeSupport.log.warn { "value length is too long , it may be truncated. we recommend that the length of value be less than 2048 characters." }
            }
        }
    },
    User(EnvironmentTargetUser) {
        override val keyBufferSize: Long
            get() = 255

        override val valueBufferSize: Long
            get() = 32767

        override fun keyCheck(key: String) {
            if (key.contains('=')) {
                // https://docs.microsoft.com/en-us/windows/win32/procthread/environment-variables
                throw IllegalArgumentException("User environment variable name must not contain '='")
            }
            if (key.length >= 255) {
                // https://docs.microsoft.com/en-us/dotnet/api/system.environment.setenvironmentvariable
                throw IllegalArgumentException("key length must less than 255 characters in user environment.")
            }
        }

        override fun valueCheck(value: String) {
            if (value.length >= 32767) {
                // https://docs.microsoft.com/en-us/dotnet/api/system.environment.setenvironmentvariable
                throw IllegalArgumentException("value length must less than 32767 characters in user environment.")
            }
            if (value.length >= 2048) {
                // https://docs.microsoft.com/en-us/dotnet/api/system.environment.setenvironmentvariable
                WindowsNativeSupport.log.warn { "value length is too long , it may be truncated. we recommend that the length of value be less than 2048 characters." }
            }
        }
    },
    ;

    abstract val keyBufferSize: Long

    abstract val valueBufferSize: Long

    internal abstract fun keyCheck(key: String)

    internal abstract fun valueCheck(value: String)
}

fun main() {
    println(WindowsNativeSupport.setEnvironmentVariable("asd", "asd;fq%fw", User))
}