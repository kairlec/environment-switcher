package com.kairlec.envsr.core

import com.kairlec.envsr.core.util.loadSupportSignal
import com.kairlec.envsr.internal.RuntimeSupport

internal val currentEnvironment = loadSupportSignal<Environment>()

/**
 * 一个环境支持,对于不同的平台来说,平台是[RuntimeSupport]有一个唯一实现支持的
 */
interface Environment : RuntimeSupport {
    @DangerousApi
    val allSources: Collection<EnvironmentSource>

    @OptIn(DangerousApi::class)
    val environmentSources: Collection<EnvironmentSource> get() = allSources.filter { it.runtimeSupport }

    companion object : Environment by currentEnvironment {
        val current = currentEnvironment
    }
}
