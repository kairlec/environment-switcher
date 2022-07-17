package com.kairlec.envsr.core

import com.kairlec.envsr.core.util.loadSignal
import com.kairlec.envsr.internal.VariableValueSupport
import kotlin.reflect.KProperty

/**
 * 一个环境变量值
 */
sealed interface EnvironmentVariable {
    /**
     * 环境变量的名称
     */
    val name: String

    /**
     * 环境变量的值(以字符串表示形式)
     */
    fun valueString(): String

    /**
     * 委托(获取值)
     */
    operator fun getValue(receiver: Any?, property: KProperty<*>)
}

/**
 * 可更改的环境变量值
 */
sealed interface MutableEnvironmentVariable : EnvironmentVariable {
    override var name: String
}

/**
 * 环境变量变量值
 */
sealed interface VariableValue {
    companion object : VariableValueSupport by loadSignal()
}

interface PlaceholderValue : VariableValue {
    val refName: String
    fun resolve(): String
}

interface LiteralValue : VariableValue {
    val value: String
}

/**
 * 只读单个的环境变量(key = value)的对应
 */
interface SingleEnvironmentVariable : EnvironmentVariable {
    val value: VariableValue
}

/**
 * 可更改的单个的环境变量(key = value)的对应
 */
interface MutableSingleEnvironmentVariable : SingleEnvironmentVariable, MutableEnvironmentVariable {
    override var value: VariableValue
}

/**
 * 只读的多组的环境变量值,以[delimiter]为分隔符,比如(key = value1[delimiter]value2[delimiter]value3)
 */
interface MultiEnvironmentVariable : EnvironmentVariable, Collection<VariableValue> {
    val delimiter: String
    operator fun get(index: Int): VariableValue
}

/**
 * 可更改的多组的环境变量值,以[delimiter]为分隔符,比如(key = value1[delimiter]value2[delimiter]value3)
 */
interface MutableMultiEnvironmentVariable : MultiEnvironmentVariable, MutableEnvironmentVariable,
    MutableCollection<VariableValue> {
    operator fun set(index: Int, value: VariableValue)
}