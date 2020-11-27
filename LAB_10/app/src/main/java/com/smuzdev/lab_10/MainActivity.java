package com.smuzdev.lab_10;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    Cursor cursor;

    String groupsFaculty;
    int groupsID;
    int groupsCourse;
    String groupsName;
    String groupsHead;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.groups_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.addGroups:
                addGroups(0);
                return true;
            case R.id.showStudents:
                Intent intent = new Intent(MainActivity.this, StudentsActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    ArrayList<String> head;

    private void addGroups(final int newID) {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.add_group_dialog, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        mDialogBuilder.setView(dialogView);

        head = new ArrayList<>();
        head.add("Not specified");


        final Spinner spinnerHead = dialogView.findViewById(R.id.spinnerHead);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, head);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHead.setAdapter(adapter);

        final EditText editTextCourse = (EditText) dialogView.findViewById(R.id.editTextCourse);
        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextFaculty = (EditText) dialogView.findViewById(R.id.editTextFaculty);
        if (newID != 0) {
            cursor = databaseHelper.selectGroup(db, newID - 1);
            cursor.moveToFirst();
            groupsID = newID - 1;

            editTextCourse.setText(cursor.getInt(2) + "");
            editTextFaculty.setText(cursor.getString(1));
            editTextName.setText(cursor.getString(3));

            head = new ArrayList<>();
            cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_STUDENTS_NAME, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getInt(0) == groupsID) {
                        head.add(cursor.getString(2));
                    }
                } while (cursor.moveToNext());
            }
            if (head.isEmpty())
                head.add("Not specified");
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, head);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerHead.setAdapter(adapter);

            cursor = databaseHelper.selectGroup(db, newID - 1);
            cursor.moveToFirst();
            for (int i = 0; i < head.size(); i++) {
                if (head.get(i).intern() == cursor.getString(4).intern()) {
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
                        try {
                            groupsCourse = Integer.parseInt(editTextCourse.getText().toString());
                        } catch (NumberFormatException s) {
                            dialogs("Incorrect data at course field");
                            check = false;
                        }

                        if (check) {
                            groupsFaculty = editTextFaculty.getText().toString();
                            groupsName = editTextName.getText().toString();
                            groupsHead = spinnerHead.getSelectedItem().toString();
                            db = databaseHelper.getWritableDatabase();


                            if (newID != 0)
                                databaseHelper.updateGroups(db, groupsID, groupsFaculty, groupsCourse, groupsName, groupsHead);
                            else
                                databaseHelper.insertGroups(db, groupsFaculty, groupsCourse, groupsName, groupsHead);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();
        adapterForTable();
    }

    int groupsSelectedID;

    private void adapterForTable() {
        cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_GROUPS_NAME, null);
        int i = 0;
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        tableLayout.removeAllViews();
        if (cursor.moveToFirst()) {
            do {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        TableRow tableRow1 = (TableRow) view;
                        groupsSelectedID = Integer.parseInt(((TextView) tableRow1.getChildAt(0)).getText().toString());
                        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        mDialogBuilder
                                .setCancelable(false)
                                .setTitle("Select an action for group ID: " + groupsSelectedID)
                                .setPositiveButton("Edit",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                addGroups(groupsSelectedID + 1);
                                                adapterForTable();
                                            }
                                        })
                                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        db = databaseHelper.getWritableDatabase();
                                        databaseHelper.deleteGroups(db, groupsSelectedID);
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
                textView.setMinWidth(300);
                textView.setText("\t" + cursor.getString(1));
                tableRow.addView(textView);

                textView = new TextView((this));
                textView.setTextSize(20);
                textView.setMinWidth(100);
                textView.setText("\t" + cursor.getInt(2));
                tableRow.addView(textView);

                textView = new TextView((this));
                textView.setTextSize(20);
                textView.setMinWidth(500);
                textView.setText("\t" + cursor.getString(3));
                tableRow.addView(textView);

                textView = new TextView((this));
                textView.setTextSize(20);
                textView.setMinWidth(300);
                textView.setText("\t" + cursor.getString(4));
                tableRow.addView(textView);

                tableLayout.addView(tableRow, i);

                i++;
            }
            while (cursor.moveToNext());
        }
        i = 0;

        TableRow titleRow = new TableRow(this);
        titleRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView textView = new TextView((this));
        textView.setTextSize(20);
        textView.setMinWidth(100);
        textView.setText("ID\t\t\t");
        titleRow.addView(textView);


        textView = new TextView((this));
        textView.setTextSize(20);
        textView.setMinWidth(300);
        textView.setText("Faculty\t\t\t");
        titleRow.addView(textView);

        textView = new TextView((this));
        textView.setTextSize(20);
        textView.setMinWidth(100);
        textView.setText("Course\t\t\t");
        titleRow.addView(textView);

        textView = new TextView((this));
        textView.setTextSize(20);
        textView.setMinWidth(500);
        textView.setText("Specialization\t\t\t");
        titleRow.addView(textView);

        textView = new TextView((this));
        textView.setTextSize(20);
        textView.setMinWidth(300);
        textView.setText("Headman\t\t\t");
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
}
