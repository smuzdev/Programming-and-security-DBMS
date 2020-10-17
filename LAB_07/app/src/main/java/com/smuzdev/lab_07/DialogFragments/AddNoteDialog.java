package com.smuzdev.lab_07.DialogFragments;

import android.app.AlertDialog;
import android.app.ApplicationErrorReport;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.smuzdev.lab_07.R;

public class AddNoteDialog extends AppCompatDialogFragment {
    private EditText addTitle;
    private EditText addDescription;
    private AddNoteDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_note, null);

        builder.setView(view)
                .setTitle("New note")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = addTitle.getText().toString();
                        String description = addDescription.getText().toString();
                        listener.applyTexts(title, description);
                    }
                });

        addTitle = view.findViewById(R.id.addTitle);
        addDescription = view.findViewById(R.id.addDescription);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddNoteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement AddNoteDialogListener");
        }

    }

    public interface AddNoteDialogListener {
        void applyTexts(String title, String description);
    }
}
