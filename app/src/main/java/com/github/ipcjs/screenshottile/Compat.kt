package com.github.ipcjs.screenshottile

import android.content.Intent
import android.service.quicksettings.TileService


fun TileService.startActivityAndCollapseCompat(intent: Intent) {
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivityAndCollapse(intent)
}