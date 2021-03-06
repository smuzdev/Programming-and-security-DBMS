package com.smuzdev.lab_07.DialogFragments;

import android.Manifest;
import android.app.AlertDialog;
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

import com.smuzdev.lab_07.Helper.Json;
import com.smuzdev.lab_07.Helper.Notes;
import com.smuzdev.lab_07.Helper.XmlSerialization;
import com.smuzdev.lab_07.Models.Note;
import com.smuzdev.lab_07.R;

import java.io.File;

import permissions.dispatcher.NeedsPermission;

public class EditNoteDialog extends AppCompatDialogFragment {
    private EditText editTitle;
    private EditText editDescription;
    private Spinner editNoteCategory;
    private EditNoteDialogListener listener;
    Integer selectedNotePositon;
    XmlSerialization xmlSerialization;
    Notes notes;

    public EditNoteDialog(Integer selectedNotePositon, Notes notes) {
        this.selectedNotePositon = selectedNotePositon;
        this.notes = notes;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //notes = new Notes();
        //notes = Json.Deserialize();
        //notes = xmlSerialization.Deserialize();

        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_note, null);

        editTitle = view.findViewById(R.id.editTitle);
        editNoteCategory = view.findViewById(R.id.editNoteCategorySpiner);
        editDescription = view.findViewById(R.id.editDescription);
        editTitle.setText(notes.notesArrayList.get(selectedNotePositon).title);
        editDescription.setText(notes.notesArrayList.get(selectedNotePositon).description);

        if (editNoteCategory != null) {
            // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, notes.categoriesArrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Применяем адаптер к элементу spinner
            editNoteCategory.setAdapter(adapter);
        }

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
                        String newCategory = editNoteCategory.getSelectedItem().toString();
                        String newDescription = editDescription.getText().toString();
                        listener.applyEditNoteTexts(selectedNotePositon, newTitle, newCategory, newDescription);
                    }
                })
                .setNeutralButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Note note = new Note();
                        note = notes.notesArrayList.get(selectedNotePositon);
                        notes.notesArrayList.remove(note);
                        listener.applyNotes(notes);
                    }
                });

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
        void applyEditNoteTexts(Integer id, String newTitle, String newCategory, String newDescription);
        void applyNotes(Notes notes);
    }
}
