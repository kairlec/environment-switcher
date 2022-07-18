package com.kairlec.envsr.impl

import com.kairlec.envsr.core.VariableValue

inline val String.placeholder: VariableValue get() = VariableValue.placeholder(this)

inline val String.literal: VariableValue get() = VariableValue.literal(this)
