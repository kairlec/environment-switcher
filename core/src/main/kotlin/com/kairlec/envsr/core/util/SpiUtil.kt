package com.kairlec.envsr.core.util

import com.kairlec.envsr.internal.RuntimeSupport
import java.util.ServiceLoader
import kotlin.reflect.full.isSubclassOf

/**
 * 从spi中加载单个实现类,如果出现多个实现,则抛出错误
 * @param T 实现类的类型
 * @param useRuntimeSupport 是否使用runtimeSupport支持,如果为true,而T是实现自[RuntimeSupport]接口,则必须要求[RuntimeSupport.runtimeSupport]返回true才加载进来
 * @see [loadSupportSignal]
 */
internal inline fun <reified T : Any> loadSignal(useRuntimeSupport: Boolean = true): T {
    if (useRuntimeSupport && T::class.isSubclassOf(RuntimeSupport::class)) {
        return loadSupportSignal<RuntimeSupport>() as T
    }
    return ServiceLoader.load(T::class.java).single()
}

/**
 * 从spi中加载单个实现类,必须要求[RuntimeSupport.runtimeSupport]返回true才加载进来,如果出现多个实现,则抛出错误
 * @param T 实现类的类型
 */
internal inline fun <reified T : RuntimeSupport> loadSupportSignal(): T {
    return ServiceLoader.load(T::class.java).single { it.runtimeSupport }
}

/**
 * 从spi中加载所有实现类
 * @param T 实现类的类型
 * @param useRuntimeSupport 是否使用runtimeSupport支持,如果为true,而T是实现自[RuntimeSupport]接口,则必须要求[RuntimeSupport.runtimeSupport]返回true才加载进来
 * @see [loadSupportSignal]
 */
internal inline fun <reified T : Any> loadAll(useRuntimeSupport: Boolean = true): Collection<T> {
    if (useRuntimeSupport && T::class.isSubclassOf(RuntimeSupport::class)) {
        @Suppress("UNCHECKED_CAST")
        return loadSupportAll<RuntimeSupport>() as Collection<T>
    }
    return ServiceLoader.load(T::class.java).toList()
}

/**
 * 从spi中加载所有实现类,必须要求[RuntimeSupport.runtimeSupport]返回true才加载进来
 * @param T 实现类的类型
 */
internal inline fun <reified T : RuntimeSupport> loadSupportAll(): Collection<T> {
    return ServiceLoader.load(T::class.java).filter { it.runtimeSupport }
}
