package com.smuzdev.lab_07.DialogFragments;

import android.app.AlertDialog;
import android.app.ApplicationErrorReport;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.smuzdev.lab_07.Activities.MainActivity;
import com.smuzdev.lab_07.Helper.Json;
import com.smuzdev.lab_07.Helper.Notes;
import com.smuzdev.lab_07.R;

import java.util.ArrayList;

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

        addTitle = view.findViewById(R.id.addTitle);
        addNoteCategory = view.findViewById(R.id.addNoteCategorySpiner);
        addDescription = view.findViewById(R.id.addDescription);

        Notes notes = Json.Deserialize();

        if (addNoteCategory != null) {
            // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, notes.categoriesArrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Применяем адаптер к элементу spinner
            addNoteCategory.setAdapter(adapter);
        }

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
