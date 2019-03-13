package com.github.ipcjs.screenshottile.util

inline fun Any?.returnUnit(): Unit = Unit

inline fun <reified T> Any?.returnValue(value: T): T = value

inline fun pass() {}