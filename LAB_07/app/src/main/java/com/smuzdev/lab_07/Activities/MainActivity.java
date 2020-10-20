package com.smuzdev.lab_07.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.smuzdev.lab_07.DialogFragments.AddNoteDialog;
import com.smuzdev.lab_07.DialogFragments.DatePickerFragment;
import com.smuzdev.lab_07.Helper.Json;
import com.smuzdev.lab_07.Helper.Notes;
import com.smuzdev.lab_07.Helper.RequestPermissions;
import com.smuzdev.lab_07.Models.Note;
import com.smuzdev.lab_07.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AddNoteDialog.AddNoteDialogListener {

    TextView date;
    TextView addedTitle, addedDescription;
    Button selectDateButton;
    Context context = this;
    Activity activity = this;

    ListView listNotes;
    Notes notes;
    SimpleAdapter listViewAdapter;
    String TAG = "LAB_07_D";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        listNotes = findViewById(R.id.listNotes);

        notes = new Notes();
        notes = Json.Deserialize();

        printAllNotes();

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

    }

    public void openDialog() {
        AddNoteDialog addNoteDialog = new AddNoteDialog();
        addNoteDialog.show(getSupportFragmentManager(), "add note dialog");
    }

    public void printAllNotes() {
        final ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        HashMap<String,String> map;

        for (Note note: notes.notesArrayList) {
            map = new HashMap<>();
            map.put("Title", note.title);
            map.put("Description", note.description);
            arrayList.add(map);
        }

        Log.d(TAG, "printAllNotes: " + arrayList.size() + notes.notesArrayList.size());
        listViewAdapter = new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
                new String[]{"Title", "Description"},
                new int[]{android.R.id.text1, android.R.id.text2});
        listNotes.setAdapter(listViewAdapter);

       listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                try {
                    arrayList.get(position);
                    notes.notesArrayList.remove(position);
                }
                catch (Exception e) {

                }

            }
        });
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
                openDialog();
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
        String currentDateString = sdf.format(calendar.getTime());
        TextView selectedDate = findViewById(R.id.date);
        selectedDate.setText(currentDateString);
    }

    @Override
    public void applyTexts(String title, String description) {
        addedTitle.setText(title);
        addedDescription.setText(description);
    }
}