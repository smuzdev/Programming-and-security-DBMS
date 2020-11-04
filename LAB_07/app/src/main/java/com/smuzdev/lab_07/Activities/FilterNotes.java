package com.smuzdev.lab_07.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.smuzdev.lab_07.Helper.Notes;
import com.smuzdev.lab_07.Helper.XsltTransform;
import com.smuzdev.lab_07.Models.Note;
import com.smuzdev.lab_07.R;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.transform.TransformerException;

public class FilterNotes extends AppCompatActivity {

    Notes notes;
    Spinner filter_activity_spinner_categories;
    ListView filter_activity_lv_notes;
    ArrayAdapter<String> categoryForSpinnerAdapter;
    SimpleAdapter adapterForListView;
    String selectedByUserCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_notes);
    }

    @Override
    protected void onStart() {
        super.onStart();

        notes = new Notes();
        Intent intent = getIntent();
        notes = (Notes)intent.getSerializableExtra("notes");

        //fill spinner
        filter_activity_spinner_categories = findViewById(R.id.filter_activity_spinner_categories);
        categoryForSpinnerAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, notes.categoriesArrayList);
        categoryForSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter_activity_spinner_categories.setAdapter(categoryForSpinnerAdapter);

        //ListView
        filter_activity_lv_notes = findViewById(R.id.filter_activity_lv_notes);
        fillListView();

        filter_activity_spinner_categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedByUserCategory = notes.categoriesArrayList.get(position);
                if(selectedByUserCategory != null) {
                    FilterAndFillListView(selectedByUserCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fillListView() {
        final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

        HashMap<String, String> map;

        for (Note note: notes.notesArrayList) {
            map = new HashMap<>();
            map.put("Category", note.category);
            map.put("Title", note.title);
            map.put("Note", note.description);
            arrayList.add(map);
        }

        adapterForListView = new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
                new String[]{"Title", "Note"},
                new int[]{android.R.id.text1, android.R.id.text2});
        filter_activity_lv_notes.setAdapter(adapterForListView);

    }

    private void FilterAndFillListView(String selectedCategory) {
        final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

        HashMap<String, String> map;

        for (Note note: notes.notesArrayList) {
            if(selectedCategory.equals(note.category)) {
                map = new HashMap<>();
                map.put("Category", note.category);
                map.put("Title", note.title);
                map.put("Note", note.description);
                arrayList.add(map);
            }

        }

        adapterForListView = new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
                new String[]{"Title", "Note"},
                new int[]{android.R.id.text1, android.R.id.text2});
        filter_activity_lv_notes.setAdapter(adapterForListView);
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_notes_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.XSLT:
                if(selectedByUserCategory != null) {
                    Notes filteredNotes = new Notes();

                    //only user Picked category items
                    for (Note note:notes.notesArrayList) {
                        if(note.category == selectedByUserCategory)
                            filteredNotes.notesArrayList.add(note);
                    }

                    //XML SERIALIZATION on user filtered notes
                    File File_XML_MARKUP = new File(Environment.getExternalStorageDirectory(), "File_XML_MARKUP.xml");
                    XStream xs = new XStream();
                    try {
                        FileOutputStream fs = new FileOutputStream(File_XML_MARKUP);
                        xs.toXML(filteredNotes, fs);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }

                    //XSLT Transformantion
                    XsltTransform xslt_transform = new XsltTransform(File_XML_MARKUP);
                    try {
                        xslt_transform.Transformation();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(this, "HTML успешно сформирован!", Toast.LENGTH_SHORT).show();


                    Intent openHTMLWebViewActivityIntent = new Intent(this, HtmlWebView.class);
                    openHTMLWebViewActivityIntent.putExtra("filePath", new File(Environment.getExternalStorageDirectory(), "RESULT.html").getAbsolutePath());
                    startActivity(openHTMLWebViewActivityIntent);

                }

        }
        return super.onOptionsItemSelected(item);
    }
}