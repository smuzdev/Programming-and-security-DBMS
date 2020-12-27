package com.smuzdev.lab_09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etId, etFloat, etText;
    Button insertButton, deleteButton, updateButton, selectRawButton, selectButton;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        etId = findViewById(R.id.etId);
        etFloat = findViewById(R.id.etFloat);
        etText = findViewById(R.id.etText);

        selectButton = findViewById(R.id.btnSelect);
        selectRawButton = findViewById(R.id.btnSelectRaw);
        insertButton = findViewById(R.id.btnInsert);
        updateButton = findViewById(R.id.btnUpdate);
        deleteButton = findViewById(R.id.btnDelete);

        db = getBaseContext().openOrCreateDatabase("Lab_DB.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS SimpleTable (ID INTEGER PRIMARY KEY,F REAL,T TEXT)");


        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String str_ID = etId.getText().toString();
                    String str_Float = etFloat.getText().toString();
                    String str_text = etText.getText().toString();

                    db.execSQL("INSERT INTO SimpleTable (ID,F,T) " + "VALUES ('" + Integer.parseInt(str_ID) + "','" + Float.parseFloat(str_Float) + "','" + str_text + "')");

                    etId.getText().clear();
                    etFloat.getText().clear();
                    etText.getText().clear();

                    Toast.makeText(MainActivity.this, "Insert successful", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Incorrect input data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_ID = etId.getText().toString();

                if (!str_ID.isEmpty()) {
                    Cursor query = db.query("SimpleTable", null, "ID=" + Integer.parseInt(str_ID), null,
                            null, null, null);

                    if (query.moveToFirst()) {
                        do {
                            float fl = query.getFloat(1);
                            String str = query.getString(2);

                            etFloat.append("" + fl);
                            etText.append("" + str);
                        }

                        while (query.moveToNext());
                    }
                    query.close();
                } else {
                    Toast.makeText(MainActivity.this, "Input ID", Toast.LENGTH_SHORT).show();
                }

            }
        });

        selectRawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_ID = etId.getText().toString();

                clearAllEditText();
                if (!str_ID.isEmpty()) {
                    try {
                        Cursor query = db.rawQuery("SELECT * FROM SimpleTable Where ID=" + Integer.parseInt(str_ID) + " ;", null);
                        if (query.moveToFirst()) {
                            do {

                                Float fl = query.getFloat(1);
                                String str = query.getString(2);
                                etId.append("" + str_ID);
                                etFloat.append("" + fl);
                                etText.append("" + str);
                            }
                            while (query.moveToNext());
                        }
                        query.close();
                    } catch (Exception ex) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_ID = etId.getText().toString();
                String str_Float = etFloat.getText().toString();
                String str_text = etText.getText().toString();

                if (!str_ID.isEmpty()) {
                    try {
                        db.execSQL("UPDATE SimpleTable set F=" + Float.parseFloat(str_Float) + ",T='" + str_text + "' Where ID=" + Integer.parseInt(str_ID) + ";");
                        clearAllEditText();
                        Toast.makeText(MainActivity.this, "Update successful", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_ID = etId.getText().toString();
                int int_ID = Integer.parseInt(str_ID);

                if (!str_ID.isEmpty()) {
                    try {
                        db.execSQL("DELETE from SimpleTable where ID=" + int_ID + ";");
                        clearAllEditText();
                        Toast.makeText(MainActivity.this, "Delete successful", Toast.LENGTH_SHORT).show();

                    } catch (Exception ex) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });
    }


    public boolean validate() {
        String str_ID = etId.getText().toString();
        String str_Float = etFloat.getText().toString();
        String str_text = etText.getText().toString();


        if (str_Float.isEmpty()) {
            etFloat.setError("Float should not be empty");
            return false;
        }

        if (str_text.isEmpty()) {
            etFloat.setError("Str_text should not be empty");
            return false;
        }

        return true;
    }

    public void clearAllEditText() {

        etId.getText().clear();
        etFloat.getText().clear();
        etText.getText().clear();
    }
}