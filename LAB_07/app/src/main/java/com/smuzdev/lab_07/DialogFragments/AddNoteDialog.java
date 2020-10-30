package com.smuzdev.lab_07.DialogFragments;

import android.app.AlertDialog;
import android.app.ApplicationErrorReport;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.smuzdev.lab_07.Activities.MainActivity;
import com.smuzdev.lab_07.R;

public class AddNoteDialog extends AppCompatDialogFragment {
    private EditText addTitle;
    private EditText addDescription;
    private Spinner addNoteCategory;
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
                        String category = addNoteCategory.getSelectedItem().toString();
                        String description = addDescription.getText().toString();
                        listener.applyAddNoteTexts(title, category, description);
                    }
                });

        addTitle = view.findViewById(R.id.addTitle);
        addNoteCategory = view.findViewById(R.id.addNoteCategory);
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
        void applyAddNoteTexts(String title, String category, String description);
    }
}
