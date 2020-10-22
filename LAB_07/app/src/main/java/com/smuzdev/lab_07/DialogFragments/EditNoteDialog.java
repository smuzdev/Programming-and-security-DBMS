package com.smuzdev.lab_07.DialogFragments;

import android.app.AlertDialog;
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

import com.smuzdev.lab_07.Helper.Json;
import com.smuzdev.lab_07.Helper.Notes;
import com.smuzdev.lab_07.Models.Note;
import com.smuzdev.lab_07.R;

public class EditNoteDialog extends AppCompatDialogFragment {
    private EditText editTitle;
    private EditText editDescription;
    private EditNoteDialogListener listener;
    Integer selectedNotePositon;
    Notes notes;

    public EditNoteDialog(Integer selectedNotePositon) {
        this.selectedNotePositon = selectedNotePositon;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        notes = new Notes();
        notes = Json.Deserialize();
        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_note, null);

        builder.setView(view)
                .setTitle("Edit note")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newTitle = editTitle.getText().toString();
                        String newDescription = editDescription.getText().toString();
                        listener.applyEditNoteTexts(selectedNotePositon, newTitle, newDescription);
                    }
                })
                .setNeutralButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notes.notesArrayList.remove(selectedNotePositon);
                        listener.applyNotes(notes);
                    }
                });

        editTitle = view.findViewById(R.id.editTitle);
        editDescription = view.findViewById(R.id.editDescription);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditNoteDialog.EditNoteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement EditNoteDialogListener");
        }

    }

    public interface EditNoteDialogListener {
        void applyEditNoteTexts(Integer id, String newTitle, String newDescription);
        void applyNotes(Notes notes);
    }
}
