package com.github.ipcjs.screenshottile.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.service.quicksettings.TileService;

import com.github.ipcjs.screenshottile.R;
import com.github.ipcjs.screenshottile.ui.activity.DialogContainerActivity;

import static com.github.ipcjs.screenshottile.util.Utils.hasRoot;
import static com.github.ipcjs.screenshottile.util.Utils.p;

/**
 * Created by ipcjs on 2017/8/16.
 */
public class RootPermissionDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    public static void start(Context context) {
        if (context instanceof TileService) {
            DialogContainerActivity.Companion.startAndCollapse((TileService) context, RootPermissionDialogFragment.class, null);
        } else {
            DialogContainerActivity.Companion.start(context, RootPermissionDialogFragment.class, null);
        }
    }
    public static RootPermissionDialogFragment newInstance() {
        return new RootPermissionDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setTitle(R.string.dialog_obtain_root)
                .setMessage(getString(R.string.dialog_obtain_root_message, getString(R.string.app_name)))
                .setPositiveButton(R.string.dialog_reacquire, this)
                .setNeutralButton(R.string.dialog_i_know, this)
                .setNegativeButton(R.string.dialog_uninstall, this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                if (!hasRoot()) {
                    RootPermissionDialogFragment.start(getActivity());
                }
                break;
            case Dialog.BUTTON_NEUTRAL:
                break;
            case Dialog.BUTTON_NEGATIVE:
                startActivity(new Intent(Intent.ACTION_UNINSTALL_PACKAGE, Uri.parse("package:" + getActivity().getPackageName())));
                break;
        }
        getActivity().finish();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        p("RootDialogFragment.onDismiss");
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        getActivity().finish();
    }
}
