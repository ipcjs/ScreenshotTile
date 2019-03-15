package com.github.ipcjs.screenshottile.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceFragment
import com.cgutman.androidremotedebugger.AdbUtils
import com.github.ipcjs.screenshottile.R
import com.github.ipcjs.screenshottile.data.AdbManager
import com.github.ipcjs.screenshottile.data.PrefManager
import com.github.ipcjs.screenshottile.ui.activity.SettingActivity
import com.github.ipcjs.screenshottile.util.*
import kotlin.concurrent.thread

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
    lateinit var summaryManager: PreferenceSummaryManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref)
        summaryManager = PreferenceSummaryManager(pref).apply {
            addListPreference(delayPreference)
            addPreference(workModePreference) {
                summary = entry
                title = getText(if (value.isNullOrEmpty()) R.string.title_work_mode_please_select else R.string.title_work_mode)
            }
        }

        workModePreference.setOnPreferenceChangeListener { preference, newValue ->
            when (newValue) {
                getString(R.string.setting_work_mode_value_root) -> {
                    if (!Utils.hasRoot()) {
                        RootPermissionDialogFragment.start(context)
                        return@setOnPreferenceChangeListener false
                    }
                }
                getString(R.string.setting_work_mode_value_wifi_adb) -> {
                    var crypto = AdbUtils.readCryptoConfig(context.filesDir)
                    if (crypto == null) {
                        val dialog = ProgressDialog.show(context, messageId = R.string.dialog_generating_rsa, cancelable = false)
                        thread {
                            crypto = AdbUtils.writeNewCryptoConfig(context.filesDir)

                            runOnUiThread {
                                dialog.dismiss()
                                if (crypto == null) {
                                    prefManager.workMode = PrefManager.WORK_MODE_NONE
                                    AlertDialog.show(context, titleId = R.string.dialog_title_generate_rsa_failed, messageId = R.string.dialog_message_generate_rsa_failed)
                                } else {
                                    AdbManager.sendCmd("echo")
                                }
                            }
                        }
                    }
                }
                else -> pass()
            }
            return@setOnPreferenceChangeListener true
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        summaryManager.close()
    }

}