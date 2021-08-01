package com.example.Notes_App.ui.DialogFragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.Notes_App.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class RemoverDialogFragment extends DialogFragment {

    public static final String TAG = "RemoveDialogFragment";
    public static final String YES = "yes";

    public static RemoverDialogFragment newInstance() {
        return new RemoverDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setTitle("Are you sure about your decision?")
                .setMessage("This note will be deleted.\nProceed?")
                .setPositiveButton(YES, (dialog, which) -> {
                    getParentFragmentManager().setFragmentResult(YES, null);
                })
                .setNegativeButton("No", null);

        return builder.create();
    }
}
