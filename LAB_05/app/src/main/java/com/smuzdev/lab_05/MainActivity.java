package com.smuzdev.lab_05;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public String filename = "Lab.txt";
    public String TAG = "Events";
    Button saveButton;
    Button getValueButton;

    EditText inputKey;
    EditText inputValue;
    EditText findByKey;
    EditText outputValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveButton = findViewById(R.id.saveButton);
        getValueButton = findViewById(R.id.getValueButton);
        inputKey = findViewById(R.id.inputKey);
        inputValue = findViewById(R.id.inputValue);
        findByKey = findViewById(R.id.findByKey);
        outputValue = findViewById(R.id.outputValue);

        if(!IsFileExist(filename)) {
            ShowDialog(filename, this, this);
        }
    }

    private boolean IsFileExist(String filename) {
        boolean isFileExist = false;
        File file = new File (Environment.getExternalStorageDirectory(), filename);
        if(isFileExist = file.exists()) {
            Log.d(TAG, "File " + filename + " " + "exists.");
        } else {
            Log.d(TAG, "File " + filename + " " + "not found.");
        }
        return isFileExist;
    }

    public void ShowDialog(final String filename, final Context context, final Activity activity)  {
        final RequestPermission requestPermission = new RequestPermission();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("File " + filename + " does not exist. Do you want to create one?").setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!requestPermission.permissionGranted){
                            requestPermission.checkPermissions(context, activity);
                        }
                        CreateFile(filename);
                    }
                });
        AlertDialog ad = builder.create();
        ad.show();
    }

    public void CreateFile(String filename) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            file.createNewFile();
            Toast.makeText(this, "New file has been created.", Toast.LENGTH_LONG).show();
            Log.d("Log_02", "File " + filename + " has been successfully created.");
        } catch (IOException e) {
            Log.d("Log_02", "Error: " + e.getMessage() + ". File " + filename + " was not created.");
        }
    }



}
