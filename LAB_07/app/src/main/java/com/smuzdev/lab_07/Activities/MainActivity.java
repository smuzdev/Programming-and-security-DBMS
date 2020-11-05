package com.smuzdev.lab_07.Activities;

import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smuzdev.lab_07.DialogFragments.AddNoteCategoryDialog;
import com.smuzdev.lab_07.DialogFragments.AddNoteDialog;
import com.smuzdev.lab_07.DialogFragments.DatePickerFragment;
import com.smuzdev.lab_07.DialogFragments.EditNoteDialog;
import com.smuzdev.lab_07.Helper.Json;
import com.smuzdev.lab_07.Helper.Notes;
import com.smuzdev.lab_07.Helper.RequestPermissions;
import com.smuzdev.lab_07.Helper.XmlSerialization;
import com.smuzdev.lab_07.Models.Note;
import com.smuzdev.lab_07.R;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        EditNoteDialog.EditNoteDialogListener, AddNoteDialog.AddNoteDialogListener,
        AddNoteCategoryDialog.AddNoteDialogListener {

    TextView date;
    Button selectDateButton, clearDateButton;
    Context context = this;
    Activity activity = this;
    String currentDateString;

    ListView listNotes;
    Notes notes;
    SimpleAdapter listViewAdapter;
    String TAG = "LAB07_D";
    XmlSerialization xmlSerialization;

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) //annotation of PermissionsDispatcher library
    void DeserializeNotes() {
        xmlSerialization = new XmlSerialization(new File(getApplicationContext().getFilesDir(), "Notes.xml"));
        notes = xmlSerialization.Deserialize();
    }

    //if user denied permission to external storage
    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void DeserializeNotesDenied() {
        Toast.makeText(this, "You should allow access to File storage for work with APP. After close app Notes will be DELETED;", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivityPermissionsDispatcher.DeserializeNotesWithPermissionCheck(this);
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final RequestPermissions requestPermission = new RequestPermissions();
        if(!requestPermission.permissionGranted) {
            requestPermission.checkPermissions(context, activity);
        }

        date = findViewById(R.id.date);
        selectDateButton = findViewById(R.id.selectDateButton);
        clearDateButton = findViewById(R.id.clearDateButton);
        listNotes = findViewById(R.id.listNotes);

        notes = new Notes();
        notes = xmlSerialization.Deserialize();

        printAllNotes();

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        clearDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               date.setText("DD.MM.YYYY");
               printAllNotes();
            }
        });


    }

    @Override
    protected  void onStop() {
        super.onStop();
        xmlSerialization.Serialize(notes);
    }

    public void openAddNoteDialog(Notes notes) {
        AddNoteDialog addNoteDialog = new AddNoteDialog(notes);
        addNoteDialog.show(getSupportFragmentManager(), "add note dialog");
    }

    public void openAddCategoryDialog() {
        AddNoteCategoryDialog addNoteCategoryDialog = new AddNoteCategoryDialog();
        addNoteCategoryDialog.show(getSupportFragmentManager(), "add note category dialog");
    }


    public void openEditNoteDialog(Integer id, Notes notes) {
        EditNoteDialog editNoteDialog = new EditNoteDialog(id, notes);
        editNoteDialog.show(getSupportFragmentManager(), "edit note dialog");
    }

    public void printAllNotes() {

        if (date.getText().toString().equals("DD.MM.YYYY")) {

            final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
            HashMap<String, String> map;

            for (Note note : notes.notesArrayList) {
                map = new HashMap<>();
                map.put("Title", note.title);
                map.put("Description", note.description);
                arrayList.add(map);
            }

            Log.d(TAG, "printAllNotes: " + arrayList.size() + notes.notesArrayList.size());
            listViewAdapter = new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
                    new String[]{"Title", "Description"},
                    new int[]{android.R.id.text1, android.R.id.text2}) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                    text1.setTextColor(Color.WHITE);
                    text1.setTextSize(20);
                    text2.setTextColor(Color.WHITE);
                    text1.setTextSize(20);
                    return view;
                }
            };
            listNotes.setAdapter(listViewAdapter);


            listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    openEditNoteDialog(position, notes);
                }
            });
        } else {
            final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
            HashMap<String, String> map;

            for (Note note : notes.notesArrayList) {
                if(note.date.equals(date.getText().toString())) {
                    map = new HashMap<>();
                    map.put("Title", note.title);
                    map.put("Description", note.description);
                    arrayList.add(map);
                }
            }

            Log.d(TAG, "printAllNotes: " + arrayList.size() + notes.notesArrayList.size());
            listViewAdapter = new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
                    new String[]{"Title", "Description"},
                    new int[]{android.R.id.text1, android.R.id.text2}) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                    text1.setTextColor(Color.WHITE);
                    text1.setTextSize(20);
                    text2.setTextColor(Color.WHITE);
                    text1.setTextSize(20);
                    return view;
                }
            };
            listNotes.setAdapter(listViewAdapter);


            listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    openEditNoteDialog(position, notes);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_note:
                if (currentDateString != null) {
                    openAddNoteDialog(notes);
                } else {
                    Toast.makeText(this, "Date should be selected!", Toast.LENGTH_LONG).show();
                } break;
            case R.id.add_category:
                    openAddCategoryDialog();
                    break;
            case R.id.filter_notes:
                Intent intent = new Intent(this, FilterNotes.class);
                intent.putExtra("notes", notes);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDateString = sdf.format(calendar.getTime());
        TextView selectedDate = findViewById(R.id.date);
        selectedDate.setText(currentDateString);

        Notes matchedNotes = new Notes();

        //show only notes with selected date
        if(notes != null) {
            for (Note note:notes.notesArrayList) {
                if(note.date.equals(selectedDate)) {
                    matchedNotes.notesArrayList.add(note);
                }
            }
            printAllNotes();
        }
    }


    @Override
    public void applyAddNoteCategoryTexts(String category) {
        if (notes.categoriesArrayList.size() < 10) {
            notes.categoriesArrayList.add(category);
        } else {
            Toast.makeText(this, "Maximum count of categories is 5", Toast.LENGTH_SHORT).show();
        }
        //Json.Serialize(notes);
        xmlSerialization.Serialize(notes);
    }


    @Override
    public void applyEditNoteTexts(Integer id, String newTitle, String newCategory, String newDescription) {
        Note note = notes.notesArrayList.get(id);
        note.title = newTitle;
        note.category = newCategory;
        note.description = newDescription;
        xmlSerialization.Serialize(notes);
        //Json.Serialize(notes);
        printAllNotes();
        listViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void applyNotes(Notes notes) {
        this.notes = notes;
        //Json.Serialize(notes);
        xmlSerialization.Serialize(this.notes);
        printAllNotes();
        listViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void applyAddNoteTexts(String title, String category, String description) {
        if (notes.notesArrayList.size() < 20) {
            notes.notesArrayList.add(new Note(notes.notesArrayList.size(), currentDateString, title, category, description));
        } else {
            Toast.makeText(this, "There is too much notes. Delete one and try again", Toast.LENGTH_LONG).show();
        }
        xmlSerialization.Serialize(notes);
        //Json.Serialize(notes);
        printAllNotes();
        listViewAdapter.notifyDataSetChanged();
    }
}
