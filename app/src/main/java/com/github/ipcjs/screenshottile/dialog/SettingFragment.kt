package com.github.ipcjs.screenshottile.dialog

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceFragment
import com.github.ipcjs.screenshottile.PrefManager
import com.github.ipcjs.screenshottile.R
import com.github.ipcjs.screenshottile.util.Utils
import com.github.ipcjs.screenshottile.util.pass

/**
 * Created by ipcjs on 2017/8/17.
 */
class SettingFragment : PreferenceFragment() {
    companion object {
        fun start(context: Context) {
            SettingActivity.start(context)
        }
    }

    val delayPreference by lazy { findPreference(getString(R.string.pref_key_delay)) as ListPreference }
    val workModePreference by lazy { findPreference(getString(R.string.pref_key_work_mode)) as ListPreference }
    val pref: SharedPreferences by lazy { preferenceManager.sharedPreferences }
    val prefManager by lazy { PrefManager(context, pref) }
    lateinit var summaryManager: ListPreferenceSummaryManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref)
        summaryManager = ListPreferenceSummaryManager(pref).apply {
            add(delayPreference)
            add(workModePreference)
        }
        // todo title标红
        // if (workModePreference.value.isNullOrEmpty()) {
//            workModePreference.title = getText(R.string.title_work_mode_please_select)
  //      }
        workModePreference.setOnPreferenceChangeListener { preference, newValue ->
            when (newValue) {
                getString(R.string.setting_work_mode_value_root) -> {
                    if (!Utils.hasRoot()) {
                        RootPermissionDialogFragment.start(context)
                        return@setOnPreferenceChangeListener false
                    }
                }
                getString(R.string.setting_work_mode_value_wifi_adb) -> pass()
                else -> pass()
            }
            return@setOnPreferenceChangeListener true
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        summaryManager.destroy()
    }

    class ListPreferenceSummaryManager(private val pref: SharedPreferences) {
        private val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            preferenceMap[key]?.updateSummary()
        }

        init {
            pref.registerOnSharedPreferenceChangeListener(listener)
        }

        private val preferenceMap = mutableMapOf<String, ListPreference>()
        fun add(preference: ListPreference) {
            preference.updateSummary()
            preferenceMap[preference.key] = preference
        }

        private fun ListPreference.updateSummary() {
            this.summary = this.entry
        }

        fun destroy() {
            pref.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
}