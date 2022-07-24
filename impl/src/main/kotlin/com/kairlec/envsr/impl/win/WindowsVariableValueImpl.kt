package com.kairlec.envsr.impl.win

import com.google.auto.service.AutoService
import com.kairlec.envsr.core.*
import com.kairlec.envsr.internal.VariableValueSupport


@JvmInline
value class WindowsPlaceholderValue @ExperimentalApi constructor(override val refName: String) : PlaceholderValue {
    override fun resolve(): String {
        return windowsEnvironment.getEnvironmentValue(refName)
    }

    override val rawContent: String
        get() = "%${refName}%"
}

@JvmInline
value class WindowsLiteralValue @ExperimentalApi constructor(override val value: String) : LiteralValue {
    override val rawContent: String
        get() = value
}

@AutoService(VariableValueSupport::class)
private class WindowsVariableValueSupportImpl : VariableValueSupport {
    @OptIn(ExperimentalApi::class)
    override fun placeholder(name: String): VariableValue {
        return WindowsPlaceholderValue(name)
    }

    @OptIn(ExperimentalApi::class)
    override fun literal(value: String): VariableValue {
        return WindowsLiteralValue(value)
    }
}