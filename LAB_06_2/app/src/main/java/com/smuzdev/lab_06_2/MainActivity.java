package com.smuzdev.lab_06_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button findContactButton, printButton;
    TextView foundResultTextView;
    AutoCompleteTextView findContactAutoComplete;
    String TAG = "LAB_06";

    ArrayList<Person> contactsList = new ArrayList<Person>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        findContactButton = findViewById(R.id.findContactButton);
        foundResultTextView = findViewById(R.id.foundResultTextView);
        findContactAutoComplete = findViewById(R.id.findContactAutoComlete);
        printButton = findViewById(R.id.printButton);

        contactsList = Json.Deserialize();

        //AutoComplete implementation
        ArrayList<String> birthDateAutoComplete = new ArrayList<String>();

        for (Person person : contactsList) {
            Log.i(TAG, person.toString());
            birthDateAutoComplete.add(person.birthDate);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, birthDateAutoComplete);

        findContactAutoComplete.setThreshold(1); //will start working from first character
        findContactAutoComplete.setAdapter(adapter);

        findContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String autoTextInput = findContactAutoComplete.getText().toString();
                Log.i(TAG, "User try to find person with date:" + autoTextInput);
                contactsList = Json.Deserialize();

                for (Person person : contactsList) {
                    if (person.birthDate.equals(autoTextInput)) {
                        Toast.makeText(getApplicationContext(), "Result has been found", Toast.LENGTH_LONG).show();
                        foundResultTextView.setText(
                                "Name: " + person.name + "\n" +
                                        "Surname: " + person.surname + "\n" +
                                        "Phone: " + person.phone + "\n" +
                                        "Birth date: " + person.phone + "\n \n"
                        );

                    }
                }
            }
        });

        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Json.Deserialize();
            }
        });

    }
}