package com.smuzdev.lab_10;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME = "LAB_10.db";
    static int SCHEMA = 1;
    static String TABLE_GROUPS_NAME = "Groups";
    static String TABLE_STUDENTS_NAME = "Students";

    static String GROUPS_COLUMN_IDGROUP = "IDGroup";
    static String GROUPS_COLUMN_FACULTY = "Faculty";
    static String GROUPS_COLUMN_COURSE = "Course";
    static String GROUPS_COLUMN_NAME = "Name";
    static String GROUPS_COLUMN_HEAD = "Head";

    static String STUDENTS_COLUMN_IDGROUP = "IDGroup";
    static String STUDENTS_COLUMN_IDSTUDENT = "IDStudent";
    static String STUDENTS_COLUMN_NAME = "Name";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.execSQL("pragma foreign_keys = on");
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createGroupsTable(db);
        createStudentsTable(db);
    }

    private void createStudentsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_STUDENTS_NAME + " ("
                + STUDENTS_COLUMN_IDGROUP + " INTEGER,"
                + STUDENTS_COLUMN_IDSTUDENT + " INTEGER primary key autoincrement, "
                + STUDENTS_COLUMN_NAME + " TEXT,"
                + "  FOREIGN KEY(" + STUDENTS_COLUMN_IDGROUP + ") REFERENCES " + TABLE_GROUPS_NAME + "(" + GROUPS_COLUMN_IDGROUP + ") ON UPDATE CASCADE ON DELETE CASCADE"
                + ");");
        db.execSQL("INSERT INTO " + TABLE_STUDENTS_NAME +
                " (" + STUDENTS_COLUMN_IDGROUP + ", " + STUDENTS_COLUMN_NAME + ") " +
                " VALUES (1, 'Vladislav');");
        db.execSQL("INSERT INTO " + TABLE_STUDENTS_NAME +
                " (" + STUDENTS_COLUMN_IDGROUP + ", " + STUDENTS_COLUMN_NAME + ") " +
                " VALUES (1, 'Mark');");
    }

    private void createGroupsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_GROUPS_NAME + " ("
                + GROUPS_COLUMN_IDGROUP + " INTEGER PRIMARY KEY autoincrement,"
                + GROUPS_COLUMN_FACULTY + " TEXT, "
                + GROUPS_COLUMN_COURSE + " INTEGER,"
                + GROUPS_COLUMN_NAME + " TEXT,"
                + GROUPS_COLUMN_HEAD + " TEXT"
                + ");");
        db.execSQL("INSERT INTO " + TABLE_GROUPS_NAME
                + " ("
                + GROUPS_COLUMN_FACULTY + ", "
                + GROUPS_COLUMN_COURSE + " ,"
                + GROUPS_COLUMN_NAME + " ,"
                + GROUPS_COLUMN_HEAD + " " +
                ") VALUES ( 'FIT', 3,'POIBMS','Not specified');");
        db.execSQL("INSERT INTO " + TABLE_GROUPS_NAME
                + " ("
                + GROUPS_COLUMN_FACULTY + ", "
                + GROUPS_COLUMN_COURSE + " ,"
                + GROUPS_COLUMN_NAME + " ,"
                + GROUPS_COLUMN_HEAD + " " +
                ") VALUES ( 'FIT', 1,'POIT','Not specified');");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void dropTableGroups(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS_NAME);
        createGroupsTable(db);
    }

    public void dropTableStudents(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS_NAME);
        createStudentsTable(db);
    }

    public void insertGroups(SQLiteDatabase db, String fac, int course, String name, String head) {
        db.execSQL("INSERT INTO " + TABLE_GROUPS_NAME
                + " ("
                + GROUPS_COLUMN_FACULTY + ", "
                + GROUPS_COLUMN_COURSE + " ,"
                + GROUPS_COLUMN_NAME + " ,"
                + GROUPS_COLUMN_HEAD + " )" +
                " VALUES ('" + fac + "', " + course + ", '" + name + "', '" + head + "');");
    }

    public Cursor selectGroup(SQLiteDatabase db, int id) {
        return db.rawQuery("select * from " + TABLE_GROUPS_NAME + " where IDGroup=?", new String[]{String.valueOf(id)});
    }

    public void deleteGroups(SQLiteDatabase db, int id) {
        db.execSQL("Delete FROM " + TABLE_GROUPS_NAME +
                " Where IDGroup=" + id + ";");
    }

    public void updateGroups(SQLiteDatabase db, int id, String fac, int course, String name, String head) {
        db.execSQL("Update " + TABLE_GROUPS_NAME +
                " Set " + GROUPS_COLUMN_FACULTY + "='" + fac +
                "', " + GROUPS_COLUMN_COURSE + "=" + course +
                ", " + GROUPS_COLUMN_NAME + "='" + name +
                "', " + GROUPS_COLUMN_HEAD + "='" + head +
                "' Where " + GROUPS_COLUMN_IDGROUP + "=" + id + ";");
        ;
    }

    public void insertStudent(SQLiteDatabase db, int idg, String name) {
        db.execSQL("INSERT INTO " + TABLE_STUDENTS_NAME +
                "(" + STUDENTS_COLUMN_IDGROUP + "," + STUDENTS_COLUMN_NAME + ")" +
                " VALUES (" + idg + ", '" + name + "');");
    }

    public Cursor selectStudent(SQLiteDatabase db, int idg, int ids, String name) {
        return db.rawQuery("select * from " + TABLE_STUDENTS_NAME + " where ((IDGROUP=? AND IDSTUDENT=?) AND NAME=?)", new String[]{String.valueOf(idg), String.valueOf(ids), name});
    }

    public void deleteStudent(SQLiteDatabase db, int idg, int ids, String name) {
        db.execSQL("Delete FROM " + TABLE_STUDENTS_NAME +
                " Where ((IDGROUP=" + idg + " AND IDSTUDENT=" + ids + ") AND NAME='" + name + "')");
    }

    public void updateStudent(SQLiteDatabase db, int ids, int idg, String name) {
        db.execSQL("Update " + TABLE_STUDENTS_NAME
                + " Set " + STUDENTS_COLUMN_IDGROUP + "=" + idg
                + ", " + STUDENTS_COLUMN_NAME + "='" + name
                + "' where " + STUDENTS_COLUMN_IDSTUDENT + "=" + ids);
    }
}
