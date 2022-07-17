package com.kairlec.envsr.core

@RequiresOptIn(message = "这个接口是非常危险的,请明确你知道自己在做什么", level = RequiresOptIn.Level.ERROR)
annotation class DangerousApi

@RequiresOptIn(message = "实验性接口,将来可能会改动", level = RequiresOptIn.Level.WARNING)
annotation class ExperimentalApi