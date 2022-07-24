package com.kairlec.envsr.impl.win

import com.google.auto.service.AutoService
import com.kairlec.envsr.core.DangerousApi
import com.kairlec.envsr.core.Environment
import com.kairlec.envsr.core.EnvironmentSource
import com.kairlec.envsr.core.EnvironmentVariable
import com.kairlec.envsr.impl.win.EnvironmentType.Machine
import org.apache.commons.lang3.SystemUtils

@AutoService(Environment::class)
class WindowsEnvironment private constructor() : Environment {
    @DangerousApi
    override val allSources: Collection<EnvironmentSource> by lazy {
        listOf(UserEnvironmentSource(), MachineEnvironmentSource())
    }

    override val runtimeSupport: Boolean = SystemUtils.IS_OS_WINDOWS

    fun getEnvironmentValue(name: String): String {
        return WindowsNativeSupport.expandEnvironmentVariable(name)
    }
}

internal val windowsEnvironment get() = Environment.current as WindowsEnvironment
