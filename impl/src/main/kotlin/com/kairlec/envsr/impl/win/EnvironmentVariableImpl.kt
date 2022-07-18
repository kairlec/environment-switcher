package com.kairlec.envsr.impl.win

import com.google.auto.service.AutoService
import com.kairlec.envsr.core.*
import com.kairlec.envsr.internal.VariableValueSupport
import kotlin.DeprecationLevel.ERROR


@JvmInline
value class WindowsPlaceholderValue @ExperimentalApi constructor(override val refName: String) : PlaceholderValue {
    override fun resolve(): String {
        return windowsEnvironment.getEnvironmentValue(refName)
    }
}

@JvmInline
value class WindowsLiteralValue @ExperimentalApi constructor(override val value: String) : LiteralValue

@AutoService(VariableValueSupport::class)
class VariableValueSupportImpl @Deprecated("不应该直接调用构造函数", level = ERROR) constructor() :
    VariableValueSupport {
    @OptIn(ExperimentalApi::class)
    override fun placeholder(name: String): VariableValue {
        return WindowsPlaceholderValue(name)
    }

    @OptIn(ExperimentalApi::class)
    override fun literal(value: String): VariableValue {
        return WindowsLiteralValue(value)
    }
}