package com.github.ipcjs.screenshottile.ui

import android.content.SharedPreferences
import android.preference.ListPreference
import android.preference.Preference

class PreferenceSummaryManager(private val pref: SharedPreferences) {
    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        actionMap[key]?.invoke()
    }

    init {
        pref.registerOnSharedPreferenceChangeListener(listener)
    }

    private val actionMap = mutableMapOf<String, () -> Unit>()
    fun addListPreference(preference: ListPreference) {
        addKey(preference.key) {
            preference.summary = preference.entry
        }
    }

    fun <P : Preference> addPreference(preference: P, block: P.() -> Unit) {
        addKey(preference.key) {
            preference.block()
        }
    }

    fun addKey(key: String, action: () -> Unit) {
        if (actionMap.containsKey(key)) {
            throw IllegalStateException("已经添加过这个key($key)")
        }
        actionMap[key] = action
        action.invoke()
    }

    fun close() {
        pref.unregisterOnSharedPreferenceChangeListener(listener)
    }
}