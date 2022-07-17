package com.kairlec.envsr.internal

import com.kairlec.envsr.core.VariableValue

interface VariableValueSupport {
    /**
     * 来自一个占位符的(可以解析其他环境变量值的)
     */
    fun placeholder(name: String): VariableValue

    /**
     * 一个纯文本的环境变量(不需要解析环境变量的,纯粹的值)
     */
    fun literal(value: String): VariableValue
}