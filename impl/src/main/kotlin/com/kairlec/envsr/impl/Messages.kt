package com.kairlec.envsr.impl

import org.jetbrains.annotations.PropertyKey
import java.text.MessageFormat
import java.util.Locale
import java.util.ResourceBundle

@Suppress("unused")
object Messages {
    operator fun invoke(@PropertyKey(resourceBundle = "strings.messages") key: String, vararg params: Any): String {
        return MessageFormat.format(defaultBundle.getString(key), *params)
    }

    operator fun invoke(
        locale: Locale,
        @PropertyKey(resourceBundle = "strings.messages") key: String,
        vararg params: Any
    ): String {
        return MessageFormat.format(locale(locale).getString(key), *params)
    }

    operator fun invoke(
        bundle: ResourceBundle,
        @PropertyKey(resourceBundle = "strings.messages") key: String,
        vararg params: Any
    ): String {
        return MessageFormat.format(bundle.getString(key), *params)
    }

    var defaultBundle: ResourceBundle = ResourceBundle.getBundle("strings.messages")
        private set

    fun locale(locale: Locale): ResourceBundle = ResourceBundle.getBundle("strings.messages", locale)
    var locale: Locale
        get() = defaultBundle.locale
        set(value) {
            defaultBundle = locale(value)
        }
}

fun main() {
    println(Messages("invalid_env_key_name", 123456))
}