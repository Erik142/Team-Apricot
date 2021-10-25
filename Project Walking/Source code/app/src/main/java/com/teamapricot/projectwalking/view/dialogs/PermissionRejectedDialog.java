package com.teamapricot.projectwalking.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

/**
 * @author Erik Wahlberger
 * @version 2021-09-18
 *
 * A {@code DialogFragment} used to display errors when a permission has been rejected by the user
 */
public class PermissionRejectedDialog extends DialogFragment {
    private final String TITLE = "Permission rejected";
    private final String POSITIVE_BUTTON_TEXT = "Close";
    private final String NEGATIVE_BUTTON_TEXT = "Settings";

    private String message;

    private AppCompatActivity activity;

    /**
     * Creates a new instance of the {@code PermissionRejectedDialog} class using the specified activity and message {@code String}
     * @param activity The {@code AppCompatActivity} used to switch to the application settings as well as retrieving the application package name
     * @param message The descriptive message that will appear in the dialog
     */
    public PermissionRejectedDialog(AppCompatActivity activity, String message) {
        this.activity = activity;
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(TITLE)
                .setMessage(message)
                .setPositiveButton(POSITIVE_BUTTON_TEXT, getOnClickListener(() -> System.exit(0)))
                .setNegativeButton(NEGATIVE_BUTTON_TEXT, getOnClickListener(() -> openApplicationSettings()));

        return dialogBuilder.create();
    }

    /**
     * Creates an {@OnClickListener} using a {@code Runnable}
     * @param runnable The {@code Runnable} that will run on a button click
     * @return An {@code OnClickListener} using the provided {@code Runnable}
     */
    private DialogInterface.OnClickListener getOnClickListener(Runnable runnable) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (runnable != null) {
                    runnable.run();
                }
            }
        };
    }

    /**
     * Opens this app's application settings in the Settings app
     */
    private void openApplicationSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);

        activity.startActivity(intent);
        System.exit(0);
    }
}
