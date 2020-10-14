package com.smuzdev.lab_06.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.smuzdev.lab_06.R;
import com.smuzdev.lab_06.auxiliary.DatePickerFragment;
import com.smuzdev.lab_06.auxiliary.Json;
import com.smuzdev.lab_06.auxiliary.Person;
import com.smuzdev.lab_06.auxiliary.RequestPermissions;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener {
    String TAG = "LAB_06";
    TextInputEditText inputName, inputSurname;
    EditText inputPhone;
    TextView birthDateTextView;
    Button createButton, selectDateButton;
    Context context = this;
    Activity activity = this;

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

        inputName = findViewById(R.id.inputName);
        inputSurname = findViewById(R.id.inputSurname);
        inputPhone = findViewById(R.id.inputPhone);
        birthDateTextView = findViewById(R.id.birthDate);
        selectDateButton = findViewById(R.id.selectDateButton);
        createButton = findViewById(R.id.createButton);

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString();
                String surname = inputSurname.getText().toString();
                String phone = inputPhone.getText().toString();
                String birthDate = birthDateTextView.getText().toString();

                // Public (external storage) serialization
                Json.Serialize(new Person(name, surname, phone, birthDate));

                // Private (internal storage) serialization
                // data/com.smuzdev.lab6/
                String path = getExternalFilesDir(null).getAbsolutePath() + "/json.json";
                File file = new File(path);
                Json.Serialize(file, new Person(name, surname, phone, birthDate));

                Toast.makeText(getBaseContext() , "Successfully created", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(calendar.getTime());

        TextView selectedDate = findViewById(R.id.birthDate);
        selectedDate.setText(currentDateString);
    }

}