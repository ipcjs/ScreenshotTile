package com.github.ipcjs.screenshottile.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.github.ipcjs.screenshottile.R

/**
 * Created by ipcjs on 2017/8/17.
 */

class PrefManager(val context: Context, private val pref: SharedPreferences) {
    companion object {
        const val WORK_MODE_NONE = 0
        const val WORK_MODE_ROOT = 1
        const val WORK_MODE_WIFI_ADB = 2
    }

    private val KEY_DELAY = context.getString(R.string.pref_key_delay)
    private val KEY_SHOW_COUNT_DOWN = context.getString(R.string.pref_key_show_count_down)
    private val KEY_WORK_MODE = context.getString(R.string.pref_key_work_mode)
    private val KEY_ADB_PORT = context.getString(R.string.pref_key_adb_port)
    private val VALUE_WORK_MODE_ROOT = context.getString(R.string.setting_work_mode_value_root)
    private val VALUE_WORK_MODE_WIFI_ADB = context.getString(R.string.setting_work_mode_value_wifi_adb)

    constructor(context: Context) : this(context, PreferenceManager.getDefaultSharedPreferences(context))

    var delay: Int
        get() = pref.getString(KEY_DELAY, "0").toIntOrNull() ?: 0
        set(value) = pref.edit().putString(KEY_DELAY, value.toString()).apply()

    var showCountDown: Boolean
        get() = pref.getBoolean(KEY_SHOW_COUNT_DOWN, true)
        set(value) = pref.edit().putBoolean(KEY_SHOW_COUNT_DOWN, value).apply()

    var workMode: Int
        get() = when (pref.getString(KEY_WORK_MODE, "")) {
            VALUE_WORK_MODE_ROOT -> WORK_MODE_ROOT
            VALUE_WORK_MODE_WIFI_ADB -> WORK_MODE_WIFI_ADB
            else -> WORK_MODE_NONE
        }
        set(value) = pref.edit().putString(KEY_WORK_MODE, when (value) {
            WORK_MODE_NONE -> ""
            WORK_MODE_ROOT -> VALUE_WORK_MODE_ROOT
            WORK_MODE_WIFI_ADB -> VALUE_WORK_MODE_WIFI_ADB
            else -> error("unsupported work mode $value")
        }).apply()

    var adbPort: Int
        get() = pref.getInt(KEY_ADB_PORT, 5555)
        set(value) = pref.edit().putInt(KEY_ADB_PORT, value).apply()

}
