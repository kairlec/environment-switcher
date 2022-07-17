package com.kairlec.envsr.internal

/**
 * 指示该接口实现的类在当前的平台中是否受到支持
 */
interface RuntimeSupport {
    /**
     * 如果平台支持该类,则为true,否则为false
     */
    val runtimeSupport: Boolean
}