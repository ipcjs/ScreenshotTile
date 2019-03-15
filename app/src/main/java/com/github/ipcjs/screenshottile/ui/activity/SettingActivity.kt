package com.github.ipcjs.screenshottile.ui.activity

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import com.github.ipcjs.screenshottile.ui.SettingFragment

class SettingActivity : Activity() {
    companion object : ActivityStarter(SettingActivity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val fname = SettingFragment::class.java.name
            fragmentManager.beginTransaction()
                    .add(android.R.id.content, Fragment.instantiate(this, fname), fname)
                    .commit()
        }
    }

}