package com.kairlec.envsr.impl.win

import com.google.auto.service.AutoService
import com.kairlec.envsr.core.DangerousApi
import com.kairlec.envsr.core.Environment
import com.kairlec.envsr.core.EnvironmentSource
import kotlin.DeprecationLevel.ERROR

@AutoService(Environment::class)
class WindowsEnvironment @Deprecated("不应该直接调用构造函数", level = ERROR) constructor() : Environment {
    @DangerousApi
    override val allSources: Collection<EnvironmentSource>
        get() = TODO("Not yet implemented")

    override val runtimeSupport: Boolean
        get() = TODO("Not yet implemented")

    fun getEnvironmentValue(name: String): String {
        TODO("Not yet implemented to get $name")
    }
}

internal val windowsEnvironment get() = Environment.current as WindowsEnvironment
