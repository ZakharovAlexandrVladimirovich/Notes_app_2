package com.example.Notes_App.ui.DialogFragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.Notes_App.R;
import com.google.android.material.textfield.TextInputEditText;

public class EditorDialogFragment extends DialogFragment {

    public static final String TAG = "EditorDialogFragment";
    public static final String FIELD_TEXT = "FIELD_TEXT";
    public static final String FIELD_NAME = "FIELD_NAME";
    public static final String CONFIRM_TEXT = "CONFIRM_TEXT";
    public static final String CONFIRM = "CONFIRM";

    public static EditorDialogFragment newInstance(String fieldText, String fieldName) {
        EditorDialogFragment fragment = new EditorDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FIELD_TEXT, fieldText);
        bundle.putString(FIELD_NAME, fieldName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getTheme() {
        return R.style.AlertDialogStyle;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editor_dialog, container, false);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String text = null;
        String fieldName = null;
        if (getArguments() != null) {
            text = getArguments().getString(FIELD_TEXT);
            fieldName = getArguments().getString(FIELD_NAME);
        }

        TextView title = view.findViewById(R.id.edit_note_dialog_title);
        title.setText(String.format("Edit %s", fieldName));

        TextInputEditText textField = view.findViewById(R.id.edit_note_dialog_text_field);
        textField.setText(text);

        view.findViewById(R.id.confirm_changes).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(CONFIRM_TEXT, textField.getText().toString());
            getParentFragmentManager().setFragmentResult(CONFIRM, bundle);
            dismiss();
        });

        view.findViewById(R.id.cancel_changes).setOnClickListener(v -> dismiss());

    }
}