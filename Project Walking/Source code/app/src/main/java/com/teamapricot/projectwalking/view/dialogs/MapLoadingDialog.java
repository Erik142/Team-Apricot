package com.teamapricot.projectwalking.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
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

    public void updateProgress(int progress) {
        this.progress = progress;

        if (progressBar != null) {
            progressBar.setProgress(progress);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(TITLE)
                .setView(R.layout.dialog_map_loading);

        return dialogBuilder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.progressBar = view.findViewById(R.id.map_loading_progress_bar);
    }
}
