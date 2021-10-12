package com.teamapricot.projectwalking.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.teamapricot.projectwalking.model.NavigationModel;

import org.osmdroid.bonuspack.routing.RoadManager;

public class ReplaceDestinationDialog extends DialogFragment {
    private final String TITLE = "Replace destination?";
    private final String POSITIVE_BUTTON_TEXT = "OK";
    private final String NEGATIVE_BUTTON_TEXT = "Cancel";

    private final String MESSAGE = "This will replace your current destination.";

    private AppCompatActivity activity;
    private Effect effect;

    public interface Effect {
        void run();
    }

    public ReplaceDestinationDialog(AppCompatActivity activity, Effect effect) {
        this.activity = activity;
        this.effect = effect;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(TITLE)
                .setMessage(MESSAGE)
                .setPositiveButton(POSITIVE_BUTTON_TEXT, (DialogInterface.OnClickListener) (dialog, i) -> effect.run())
                .setNegativeButton(NEGATIVE_BUTTON_TEXT, null);

        return dialogBuilder.create();
    }
}
