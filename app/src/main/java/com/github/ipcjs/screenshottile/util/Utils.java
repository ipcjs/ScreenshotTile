package com.github.ipcjs.screenshottile.util;

import android.text.TextUtils;
import android.util.Log;

import com.github.ipcjs.screenshottile.App;
import com.github.ipcjs.screenshottile.BuildConfig;
import com.github.ipcjs.screenshottile.data.AdbManager;
import com.github.ipcjs.screenshottile.data.PrefManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Utils {
    private static final String CMD_SCREENSHOT = "input keyevent 120";

    public static void p(Object... args) {
        if (!BuildConfig.DEBUG) return;
        p(TextUtils.join(", ", args));
    }

    public static void p(String msg) {
        if (!BuildConfig.DEBUG) return;
        Log.i("Screenshot", msg);
    }

    public static void p(String format, Object... args) {
        if (!BuildConfig.DEBUG) return;
        p(String.format(format, args));
    }

    public static int runOneCmdByRoot(String cmd, boolean isWait) {
        int returnCode = -1;
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});
            if (isWait) {
                returnCode = process.waitFor();
            }
            p("rumCmd: %s", cmd);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return returnCode;
    }

    public static int screenshot() {
        switch (App.getInstance().getPrefManager().getWorkMode()) {
            case PrefManager.WORK_MODE_ROOT:
                return runOneCmdByRoot(CMD_SCREENSHOT, false);
            case PrefManager.WORK_MODE_WIFI_ADB:
                // TODO: 2021/11/14 ipcjs process connect failed...
                AdbManager.INSTANCE.sendCmd(CMD_SCREENSHOT);
                return 0;
            default:
            case PrefManager.WORK_MODE_NONE:
                throw new IllegalStateException("unsupported work mode");
        }
    }

    public static boolean hasRoot() {
        return runOneCmdByRoot("echo", true) == 0;
    }

    public static int runCmd(String cmd, boolean isRoot, boolean isWait) {
        Process process = null;
        int returnCode = -1;
        try {
            process = Runtime.getRuntime().exec(isRoot ? "su" : "sh");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            bw.write(cmd);
            bw.write("\nexit\n");
            bw.flush();
            if (isWait) {
                returnCode = process.waitFor();
                if (BuildConfig.DEBUG) {
                    p("runCmd: %s => %d, %s, %s", cmd, returnCode, is2String(process.getInputStream()), is2String(process.getErrorStream()));
                }
            } else {
                p("runCmd: %s", cmd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (process != null && isWait) {
                process.destroy();
            }
        }
        return returnCode;
    }

    public static String is2String(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String tmp = null;
        while ((tmp = br.readLine()) != null) {
            sb.append(tmp).append('\n');
        }
        return sb.toString();
    }
}
