package com.github.ipcjs.screenshottile

import android.content.Intent
import android.os.IBinder
import android.service.quicksettings.TileService
import com.github.ipcjs.screenshottile.data.PrefManager
import com.github.ipcjs.screenshottile.ui.RootPermissionDialogFragment
import com.github.ipcjs.screenshottile.util.Utils.hasRoot
import com.github.ipcjs.screenshottile.util.Utils.p

class ScreenshotTileService : TileService() {
    private val pref by lazy { PrefManager(this) }

    override fun onTileAdded() {
        super.onTileAdded()
        p("onTileAdded")
        when (pref.workMode) {
            PrefManager.WORK_MODE_ROOT -> {
                if (!hasRoot()) {
                    RootPermissionDialogFragment.start(this)
                }
            }
        }
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        p("onTileRemoved")
    }

    override fun onStartListening() {
        super.onStartListening()
        p("onStartListening")
    }

    override fun onStopListening() {
        super.onStopListening()
        p("onStopListening")
    }

    override fun onClick() {
        super.onClick()
        p("onClick")
        App.getInstance().screenshot(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        p("onBind")
        return super.onBind(intent)
    }

}
