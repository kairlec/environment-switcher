package com.kairlec.envsr.core

import com.kairlec.envsr.internal.RuntimeSupport

/**
 * 环境变量源,在一个环境内,可能有多个环境源
 * 比如
 *  - Windows的当前用户环境变量与系统环境变量
 *  - linux/unix的profile文件
 */
interface EnvironmentSource : RuntimeSupport {
    /**
     * 是否是只读的环境源
     */
    val readOnly: Boolean

    /**
     * 源下所拥有的环境变量
     */
    val variables: Collection<EnvironmentVariable>
}