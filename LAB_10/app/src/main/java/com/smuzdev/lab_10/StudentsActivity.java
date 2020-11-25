package com.smuzdev.lab_10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentsActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    Cursor cursor;

    int studentIDG;
    String studentName;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.students_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.addStudents:
                addStudents(-1, 0, "");
                return true;
            case R.id.showGroups:
                Intent intent = new Intent(StudentsActivity.this, MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();
        adapterForTable();
    }

    int studentSelectedIDGroup;
    int studentSelectedIDStudent;
    String studentSelectedName;

    private void adapterForTable() {
        cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_STUDENTS_NAME, null);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        tableLayout.removeAllViews();
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        TableRow tableRow1 = (TableRow) view;
                        studentSelectedIDGroup = Integer.parseInt(((TextView) tableRow1.getChildAt(0)).getText().toString());
                        studentSelectedIDStudent = Integer.parseInt(((TextView) tableRow1.getChildAt(1)).getText().toString());
                        studentSelectedName = ((TextView) tableRow1.getChildAt(2)).getText().toString();
                        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(StudentsActivity.this);
                        mDialogBuilder
                                .setCancelable(false)
                                .setTitle("Select an action for student: " + studentSelectedName)
                                .setPositiveButton("Edit",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                addStudents(studentSelectedIDGroup, studentSelectedIDStudent, studentSelectedName);
                                                adapterForTable();
                                            }
                                        })
                                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        db = databaseHelper.getWritableDatabase();
                                        databaseHelper.deleteStudent(db, studentSelectedIDGroup, studentSelectedIDStudent, studentSelectedName);
                                        adapterForTable();
                                    }
                                })
                                .setNeutralButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                        AlertDialog alertDialog = mDialogBuilder.create();
                        alertDialog.show();

                    }
                });


                TextView textView = new TextView((this));
                textView.setTextSize(20);
                textView.setMinWidth(100);
                textView.setText("" + cursor.getInt(0));
                tableRow.addView(textView);


                textView = new TextView((this));
                textView.setTextSize(20);
                textView.setMinWidth(100);
                textView.setText("" + cursor.getInt(1));
                tableRow.addView(textView);

                textView = new TextView((this));
                textView.setTextSize(20);
                textView.setText(cursor.getString(2));
                tableRow.addView(textView);

                tableLayout.addView(tableRow, i);

                i++;
            }
            while (cursor.moveToNext());
        }
        int i = 0;

        TableRow titleRow = new TableRow(this);
        titleRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView textView = new TextView((this));
        textView.setTextSize(20);
        textView.setMinWidth(100);
        textView.setText("Group ID\t\t\t");
        titleRow.addView(textView);


        textView = new TextView((this));
        textView.setTextSize(20);
        textView.setMinWidth(100);
        textView.setText("Student ID\t\t\t");
        titleRow.addView(textView);

        textView = new TextView((this));
        textView.setTextSize(20);
        textView.setText("Name\t\t\t");
        titleRow.addView(textView);

        tableLayout.addView(titleRow, i);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        db.close();
        cursor.close();
    }

    public void dialogs(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(s);
        builder.setPositiveButton("Ok", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addStudents(final int startIDG, final int startIDS, final String startName) {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.add_student_dialog, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        mDialogBuilder.setView(dialogView);


        cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_GROUPS_NAME, null);
        ArrayList<Integer> groups = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                groups.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        final Spinner spinnerHead = dialogView.findViewById(R.id.spinnerGroups);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHead.setAdapter(adapter);
        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        if (startIDG != -1) {
            cursor = databaseHelper.selectStudent(db, startIDG, startIDS, startName);
            cursor.moveToFirst();
            editTextName.setText(cursor.getString(2));
            for (int i = 0; i < groups.size(); i++) {
                if (groups.get(i) == cursor.getInt(0)) {
                    spinnerHead.setSelection(i);
                    break;
                }
            }
        }
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        final AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        boolean check = true;
//

                        if (check) {
                            studentName = editTextName.getText().toString();
                            studentIDG = Integer.parseInt(spinnerHead.getSelectedItem().toString());
                            db = databaseHelper.getWritableDatabase();

                            if (startIDG != -1)
                                databaseHelper.updateStudent(db, startIDS, studentIDG, studentName);
                            else
                                databaseHelper.insertStudent(db, studentIDG, studentName);
                            db = databaseHelper.getReadableDatabase();
                            adapterForTable();
                            alertDialog.dismiss();
                        }
                    }

                });
            }
        });
        alertDialog.show();


    }
}