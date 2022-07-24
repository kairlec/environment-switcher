package com.kairlec.envsr.impl.win

import com.kairlec.envsr.core.*
import com.kairlec.envsr.impl.win.EnvironmentType.Machine
import com.kairlec.envsr.impl.win.EnvironmentType.User
import kotlin.collections.Map.Entry
import kotlin.reflect.KProperty

sealed class WindowsEnvironmentSource : EnvironmentSource {
    override val runtimeSupport: Boolean get() = windowsEnvironment.runtimeSupport
}

internal class UserEnvironmentSource : WindowsEnvironmentSource() {
    override val readOnly: Boolean = false
    override val variables: Collection<EnvironmentVariable>
        get() = WindowsNativeSupport.getAllEnvironment(User).map { it.asVariable() }
}

internal class MachineEnvironmentSource : WindowsEnvironmentSource() {
    override val readOnly: Boolean = !WindowsNativeSupport.isAdministrator()
    override val variables: Collection<EnvironmentVariable>
        get() = WindowsNativeSupport.getAllEnvironment(Machine).map { it.asVariable() }

}

private class WindowsEnvironmentVariable(private val rawContent: String) : MutableMultiEnvironmentVariable,
    MutableSingleEnvironmentVariable {

    override val size: Int
        get() = TODO("Not yet implemented")

    override fun contains(element: VariableValue): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<VariableValue>): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): MutableIterator<VariableValue> {
        TODO("Not yet implemented")
    }

    override fun add(element: VariableValue): Boolean {
        TODO("Not yet implemented")
    }

    override fun addAll(elements: Collection<VariableValue>): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun remove(element: VariableValue): Boolean {
        TODO("Not yet implemented")
    }

    override fun removeAll(elements: Collection<VariableValue>): Boolean {
        TODO("Not yet implemented")
    }

    override fun retainAll(elements: Collection<VariableValue>): Boolean {
        TODO("Not yet implemented")
    }

    override var value: VariableValue
        get() = TODO("Not yet implemented")
        set(value) {
            TODO(value.toString())
        }

    override fun set(index: Int, value: VariableValue) {
        TODO("Not yet implemented")
    }

    override val delimiter: String
        get() = TODO("Not yet implemented")

    override fun get(index: Int): VariableValue {
        TODO("Not yet implemented")
    }

    override var name: String
        get() = TODO("Not yet implemented")
        set(value) {
            TODO(value)
        }

    override fun valueString(): String {
        TODO("Not yet implemented")
    }

    override fun getValue(receiver: Any?, property: KProperty<*>) {
        TODO("Not yet implemented")
    }

}

fun Entry<String, String>.asVariable(): EnvironmentVariable {
    return WindowsEnvironmentVariable("")
}