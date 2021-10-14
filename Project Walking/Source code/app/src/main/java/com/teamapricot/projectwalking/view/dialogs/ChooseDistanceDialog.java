package com.teamapricot.projectwalking.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.function.IntConsumer;

public class ChooseDistanceDialog extends DialogFragment {
    private final String TITLE = "Choose distance";

    private AppCompatActivity activity;
    private int choice;
    private IntConsumer callback;

    public ChooseDistanceDialog(AppCompatActivity activity, int choice, IntConsumer callback) {
        this.activity = activity;
        this.choice = 0;
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(TITLE)
                     .setItems(
                             new String[] {"Short", "Medium", "Long"},
                             (DialogInterface.OnClickListener) (dialog, i) -> callback.accept(i));

        return dialogBuilder.create();
    }
}
