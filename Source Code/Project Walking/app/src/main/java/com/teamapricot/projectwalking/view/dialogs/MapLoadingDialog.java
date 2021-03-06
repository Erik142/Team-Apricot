package com.teamapricot.projectwalking.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.teamapricot.projectwalking.R;

public class MapLoadingDialog extends DialogFragment {
    private final AppCompatActivity activity;
    private final String TITLE = "Loading map";

    private ProgressBar progressBar;
    private int progress = 0;

    public MapLoadingDialog(AppCompatActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(TITLE)
                .setCancelable(false)
                .setView(R.layout.dialog_map_loading);

        dialogBuilder.setOnKeyListener((dialog, keyCode, event) -> {
            if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                return true; // Consumed
            }
            else {
                return false; // Not consumed
            }
        });

        Dialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.progressBar = view.findViewById(R.id.map_loading_progress_bar);
    }
}
