package com.github.ipcjs.screenshottile.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.service.quicksettings.TileService
import com.github.ipcjs.screenshottile.util.Utils
import com.github.ipcjs.screenshottile.util.Utils.p

class NoDisplayActivity : Activity() {
    companion object {
        const val ACTION_SCREENSHOT = "screenshot"
        private const val EXTRA_ACTION = "action"

        fun start(context: Context, action: String?) {
            context.startActivity(newIntent(context, action))
        }

        @JvmStatic
        fun startAndCollapse(ts: TileService, action: String?) {
            ts.startActivityAndCollapse(newIntent(ts, action))
        }

        @JvmStatic
        fun newIntent(context: Context, action: String?): Intent {
            val intent = Intent(context, NoDisplayActivity::class.java)
            intent.putExtra(EXTRA_ACTION, action)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        val action = intent.getStringExtra(EXTRA_ACTION)
        p("NoDisplayActivity.onCreate: $action")
        when (action) {
            ACTION_SCREENSHOT -> Utils.screenshot()
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        p("NoDisplayActivity.onDestroy")
    }

}