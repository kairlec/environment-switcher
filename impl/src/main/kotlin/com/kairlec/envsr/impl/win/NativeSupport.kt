package com.kairlec.envsr.impl.win

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer

@Suppress("FunctionName")
interface NativeSupport : Library {
    fun NotifyEnvironmentChange(): Int
    fun GetAllEnvironment(type: Int, buffer: Pointer): Long
    fun SetEnvironmentVariable(name: String, value: String, type: Int)
    fun IsAdministrator(): Int
}
