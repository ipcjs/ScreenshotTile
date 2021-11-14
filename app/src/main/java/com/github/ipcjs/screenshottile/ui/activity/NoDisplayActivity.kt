package com.github.ipcjs.screenshottile.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.quicksettings.TileService;

import com.github.ipcjs.screenshottile.util.Utils;

import static com.github.ipcjs.screenshottile.util.Utils.p;

public class NoDisplayActivity extends Activity {

    public static final String EXTRA_SCREENSHOT = "screenshot";

    public static void start(Context context, boolean screenshot) {
        context.startActivity(newIntent(context, screenshot));
    }

    public static void startAndCollapse(TileService ts, boolean screenshot) {
        ts.startActivityAndCollapse(newIntent(ts, screenshot));
    }

    public static Intent newIntent(Context context, boolean screenshot) {
        Intent intent = new Intent(context, NoDisplayActivity.class);
        intent.putExtra(EXTRA_SCREENSHOT, screenshot);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p("NoDisplayActivity.onCreate");
        if (getIntent().getBooleanExtra(EXTRA_SCREENSHOT, false)) {
            Utils.screenshot();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        p("NoDisplayActivity.onDestroy");
    }
}
