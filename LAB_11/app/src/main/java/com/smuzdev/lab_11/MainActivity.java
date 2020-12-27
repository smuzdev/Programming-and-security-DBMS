package com.smuzdev.lab_11;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;

    Calendar selectedStart;
    Calendar selectedFinish;

    Spinner spinner;
    EditText editPar;
    EditText editDay;
    EditText editMonth;
    EditText editYear;
    TextView textStart;
    TextView textFinish;
    String[] wayToSelect = new String[]{"Days", "Months", "Years"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();
        databaseHelper.onCreate(db);

        editPar = findViewById(R.id.editPar);
        editDay = findViewById(R.id.editDay);
        editMonth = findViewById(R.id.editMonth);
        editYear = findViewById(R.id.editYear);

        selectedFinish = Calendar.getInstance();
        selectedStart = Calendar.getInstance();

        textFinish = findViewById(R.id.textFinish);
        textFinish.setText(dateFormat.format(selectedFinish.getTime()));
        textStart = findViewById(R.id.textStart);
        textStart.setText(dateFormat.format(selectedStart.getTime()));

        spinner = ((Spinner) findViewById(R.id.spinner));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wayToSelect);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    public void alterAverangeMark() {
        db.execSQL("drop view if exists " + databaseHelper.VIEW_AVERANGE_MARK);
        db.execSQL("create view " + databaseHelper.VIEW_AVERANGE_MARK +
                " as select " + databaseHelper.TABLE_STUDENT_NAME + "." + databaseHelper.STUDENT_COLUMN_NAME + "," +
                " AVG(" + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_MARK + ") " +
                "from " + databaseHelper.TABLE_STUDENT_NAME + " " +
                "INNER JOIN " + databaseHelper.TABLE_PROGRESS_NAME + " " +
                "on " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_IDSTUDENT + "=" + databaseHelper.TABLE_STUDENT_NAME + "." + databaseHelper.STUDENT_COLUMN_IDSTUDENT + " " +
                " WHERE (strftime('%s', " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) > strftime('%s','" + dateFormat.format(selectedStart.getTime()) + "')) and (" +
                "strftime('%s', " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) < strftime('%s','" + dateFormat.format(selectedFinish.getTime()) + "'))" +
                "GROUP By (" + databaseHelper.TABLE_STUDENT_NAME + "." + databaseHelper.STUDENT_COLUMN_NAME + ")");
    }


    public void AverangeMarkOnClick(View view) {
        alterAverangeMark();
        cursor = db.rawQuery("select * from " + databaseHelper.VIEW_AVERANGE_MARK, new String[]{});
        String result = "";
        if (cursor.moveToFirst())
            do {
                result += cursor.getString(0) + " " + cursor.getDouble(1) + '\n';
            } while (cursor.moveToNext());
        dialogs(result);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        db.close();
        if (cursor != null) cursor.close();
    }


    public void dialogs(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(s);
        builder.setPositiveButton("Ok", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Integer enteredValue;

    public void datePicker(View view) {
        try {
            enteredValue = Integer.parseInt(String.valueOf(editPar.getText()));
        } catch (NumberFormatException o) {
            dialogs("Введите корректные данные");
            return;
        }
        selectedStart = Calendar.getInstance();
        selectedFinish = Calendar.getInstance();
        if (spinner.getSelectedItem() == "Days") {
            selectedStart.set(Calendar.DAY_OF_MONTH, selectedStart.get(Calendar.DAY_OF_MONTH) - enteredValue);
        } else {
            if (spinner.getSelectedItem() == "Months") {
                selectedStart.set(Calendar.MONTH, selectedStart.get(Calendar.MONTH) - enteredValue);
            } else if (spinner.getSelectedItem() == "Years") {
                selectedStart.set(Calendar.YEAR, selectedStart.get(Calendar.YEAR) - enteredValue);
            }
        }
        textStart.setText(dateFormat.format(selectedStart.getTime()));
    }


    public void alterBestStudents() {
        db.execSQL("drop view if exists " + databaseHelper.VIEW_BEST_STUDENT);
        db.execSQL("create view " + databaseHelper.VIEW_BEST_STUDENT +
                " as select " + databaseHelper.TABLE_GROUP_NAME + "." + databaseHelper.GROUP_COLUMN_IDGROUP + "," + databaseHelper.TABLE_STUDENT_NAME + "." + databaseHelper.STUDENT_COLUMN_NAME + ",AVG(" + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_MARK + ")" +
                "from " + databaseHelper.TABLE_STUDENT_NAME + " " +
                "INNER JOIN " + databaseHelper.TABLE_PROGRESS_NAME + " " +
                "on " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_IDSTUDENT + "=" + databaseHelper.TABLE_STUDENT_NAME + "." + databaseHelper.STUDENT_COLUMN_IDSTUDENT + " " +
                "INNER JOIN " + databaseHelper.TABLE_GROUP_NAME + " " +
                "on " + databaseHelper.TABLE_STUDENT_NAME + "." + databaseHelper.STUDENT_COLUMN_IDGROUP + "=" + databaseHelper.TABLE_GROUP_NAME + "." + databaseHelper.GROUP_COLUMN_IDGROUP + " " +
                " GROUP By " + databaseHelper.TABLE_GROUP_NAME + "." + databaseHelper.GROUP_COLUMN_IDGROUP);
    }


    public void BestStudentOnClick(View view) {
        alterBestStudents();
        String result = "";
        cursor = db.rawQuery("select * from " + databaseHelper.VIEW_BEST_STUDENT
                , null);
        if (cursor.moveToFirst())
            do {
                result += cursor.getInt(0) + "  " + cursor.getString(1) + "(" + cursor.getDouble(2) + ")\n";
            } while (cursor.moveToNext());
        dialogs(result);
    }

    int year, month, day;

    public void OnClickFinish(View view) {
        try {
            year = Integer.parseInt(String.valueOf(editYear.getText()));
            month = Integer.parseInt(String.valueOf(editMonth.getText()));
            day = Integer.parseInt(String.valueOf(editDay.getText()));
        } catch (NumberFormatException o) {
            dialogs("Введите корректные данные");
            return;
        }
        selectedFinish.set(Calendar.YEAR, year);
        selectedFinish.set(Calendar.MONTH, month - 1);
        selectedFinish.set(Calendar.DAY_OF_MONTH, day);
        textFinish.setText(dateFormat.format(selectedFinish.getTime()));
    }

    public void OnClickStart(View view) {
        try {
            year = Integer.parseInt(String.valueOf(editYear.getText()));
            month = Integer.parseInt(String.valueOf(editMonth.getText()));
            day = Integer.parseInt(String.valueOf(editDay.getText()));
        } catch (NumberFormatException o) {
            dialogs("Введите корректные данные");
            return;
        }
        selectedStart.set(Calendar.YEAR, year);
        selectedStart.set(Calendar.MONTH, month - 1);
        selectedStart.set(Calendar.DAY_OF_MONTH, day);
        textStart.setText(dateFormat.format(selectedStart.getTime()));
    }

    public void alterBadStudent() {
        db.execSQL("drop view if exists " + databaseHelper.VIEW_BAD_STUDENT);
        db.execSQL("create view " + databaseHelper.VIEW_BAD_STUDENT + " as select " + databaseHelper.TABLE_STUDENT_NAME + "." + databaseHelper.STUDENT_COLUMN_NAME + "," +
                databaseHelper.TABLE_SUBJECT_NAME + "." + databaseHelper.SUBJECT_COLUMN_SUBJECT + "," +
                databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_MARK +
                " from " + databaseHelper.TABLE_STUDENT_NAME + " " +
                "INNER JOIN " + databaseHelper.TABLE_PROGRESS_NAME + " " +
                "on " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_IDSTUDENT + "=" + databaseHelper.TABLE_STUDENT_NAME + "." + databaseHelper.STUDENT_COLUMN_IDSTUDENT + " " +
                "INNER JOIN " + databaseHelper.TABLE_SUBJECT_NAME + " " +
                "on " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_IDSUBJECT + "=" + databaseHelper.TABLE_SUBJECT_NAME + "." + databaseHelper.SUBJECT_COLUMN_IDSUBJECT + " " +
                " WHERE ((strftime('%s', " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) > strftime('%s','" + dateFormat.format(selectedStart.getTime()) + "')) and (" +
                "strftime('%s', " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) < strftime('%s','" + dateFormat.format(selectedFinish.getTime()) + "'))) and(" +
                databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_MARK + "<4)");
    }

    public void BadStudentOnClick(View view) {
        alterBadStudent();
        cursor = db.rawQuery("select * from " + databaseHelper.VIEW_BAD_STUDENT, new String[]{});
        String result = "";
        if (cursor.moveToFirst())
            do {
                result += cursor.getString(0) + " " + cursor.getString(1) + "(" + cursor.getInt(2) + ")\n";
            } while (cursor.moveToNext());
        dialogs(result);
    }


    public void alterGroupsStat() {
        db.execSQL("drop view if exists " + databaseHelper.VIEW_GROUP_STATISTICS);
        db.execSQL("create view " + databaseHelper.VIEW_GROUP_STATISTICS +
                " as select " + databaseHelper.TABLE_SUBJECT_NAME + "." + databaseHelper.SUBJECT_COLUMN_SUBJECT + "," + databaseHelper.TABLE_STUDENT_NAME + "." + databaseHelper.STUDENT_COLUMN_IDGROUP + "," +
                " AVG(" + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_MARK + ") " +
                "from " + databaseHelper.TABLE_STUDENT_NAME + " " +
                "INNER JOIN " + databaseHelper.TABLE_PROGRESS_NAME + " " +
                "on " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_IDSTUDENT + "=" + databaseHelper.TABLE_STUDENT_NAME + "." + databaseHelper.STUDENT_COLUMN_IDSTUDENT + " " +
                "INNER JOIN " + databaseHelper.TABLE_SUBJECT_NAME + " " +
                "on " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_IDSUBJECT + "=" + databaseHelper.TABLE_SUBJECT_NAME + "." + databaseHelper.SUBJECT_COLUMN_IDSUBJECT + " " +
                " WHERE (strftime('%s', " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) > strftime('%s','" + dateFormat.format(selectedStart.getTime()) + "')) and (" +
                "strftime('%s', " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) < strftime('%s','" + dateFormat.format(selectedFinish.getTime()) + "')) " +
                " GROUP By " + databaseHelper.TABLE_SUBJECT_NAME + "." + databaseHelper.SUBJECT_COLUMN_SUBJECT + "," + databaseHelper.TABLE_STUDENT_NAME + "." + databaseHelper.STUDENT_COLUMN_IDGROUP);
    }


    public void groupsStatOnClick(View view) {
        alterGroupsStat();
        String result = "";
        cursor = db.rawQuery("select * from " + databaseHelper.VIEW_GROUP_STATISTICS, new String[]{});

        if (cursor.moveToFirst())
            do {
                result += "\t" + cursor.getString(0) + " " + cursor.getInt(1) + "(" + cursor.getDouble(2) + ")\n";
            } while (cursor.moveToNext());
        dialogs(result);
    }

    public void alterFacultyStat() {
        db.execSQL("drop view if exists " + databaseHelper.VIEW_FACULTY_STATISTICS);
        db.execSQL("create view " + databaseHelper.VIEW_FACULTY_STATISTICS +
                " as select " + databaseHelper.TABLE_FACULTY_NAME + "." + databaseHelper.FACULTY_COLUMN_FACULTY +
                ", AVG(" + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_MARK + ") " +
                "from " + databaseHelper.TABLE_STUDENT_NAME + " " +
                "INNER JOIN " + databaseHelper.TABLE_PROGRESS_NAME + " " +
                "on " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_IDSTUDENT + "=" + databaseHelper.TABLE_STUDENT_NAME + "." + databaseHelper.STUDENT_COLUMN_IDSTUDENT + " " +
                "INNER JOIN " + databaseHelper.TABLE_GROUP_NAME + " " +
                "on " + databaseHelper.TABLE_GROUP_NAME + "." + databaseHelper.GROUP_COLUMN_IDGROUP + "=" + databaseHelper.TABLE_STUDENT_NAME + "." + databaseHelper.STUDENT_COLUMN_IDGROUP + " " +
                "Inner JOIN " + databaseHelper.TABLE_FACULTY_NAME + " " +
                "on " + databaseHelper.TABLE_FACULTY_NAME + "." + databaseHelper.FACULTY_COLUMN_IDFACULTY + "=" + databaseHelper.TABLE_GROUP_NAME + "." + databaseHelper.GROUP_COLUMN_FACULTY + " " +
                " WHERE ((strftime('%s', " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) > strftime('%s','" + dateFormat.format(selectedStart.getTime()) + "')) and (" +
                "strftime('%s', " + databaseHelper.TABLE_PROGRESS_NAME + "." + databaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) < strftime('%s','" + dateFormat.format(selectedFinish.getTime()) + "')))" +
                " Group by (" + databaseHelper.TABLE_FACULTY_NAME + "." + databaseHelper.FACULTY_COLUMN_FACULTY + ")");
    }

    public void facultyStatOnClick(View view) {
        alterFacultyStat();
        String result = "";
        cursor = db.rawQuery("select * from " + databaseHelper.VIEW_FACULTY_STATISTICS +
                        " Order by 2 asc"
                , new String[]{});

        if (cursor.moveToFirst())
            do {
                result += cursor.getString(0) + "\n\t" + "Средний бал: " + cursor.getDouble(1) + '\n';
            } while (cursor.moveToNext());
        dialogs(result);
    }
}