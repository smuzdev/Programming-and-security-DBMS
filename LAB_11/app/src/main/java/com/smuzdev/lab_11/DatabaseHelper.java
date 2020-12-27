package com.smuzdev.lab_11;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME = "Lab11_DB.db";
    static int SCHEMA = 1;
    static String TABLE_GROUP_NAME = "GROUPS";
    static String TABLE_STUDENT_NAME = "STUDENT";
    static String TABLE_FACULTY_NAME = "FACULTY ";
    static String TABLE_SUBJECT_NAME = "SUBJECT";
    static String TABLE_PROGRESS_NAME = "PROGRESS";

    static String FACULTY_COLUMN_IDFACULTY = "IDFACULTY";
    static String FACULTY_COLUMN_FACULTY = "FACULTY";
    static String FACULTY_COLUMN_DEAN = "DEAN";
    static String FACULTY_COLUMN_OFFICETIMETABLE = "OFFICETIMETABLE";

    static String GROUP_COLUMN_IDGROUP = "IDGROUP ";
    static String GROUP_COLUMN_FACULTY = "FACULTY";
    static String GROUP_COLUMN_COURSE = "COURSE";
    static String GROUP_COLUMN_NAME = "NAME";
    static String GROUP_COLUMN_HEAD = "HEAD";

    static String STUDENT_COLUMN_IDSTUDENT = "IDSTUDENT";
    static String STUDENT_COLUMN_IDGROUP = "IDGROUP";
    static String STUDENT_COLUMN_NAME = "NAME";
    static String STUDENT_COLUMN_BIRTHDATE = "BIRTHDATE";
    static String STUDENT_COLUMN_ADDRESS = "ADDRESS";

    static String SUBJECT_COLUMN_IDSUBJECT = "IDSUBJECT";
    static String SUBJECT_COLUMN_SUBJECT = "SUBJECT";

    static String PROGRESS_COLUMN_IDSTUDENT = "IDSTUDENT";
    static String PROGRESS_COLUMN_IDSUBJECT = "IDSUBJECT";
    static String PROGRESS_COLUMN_EXAMDATE = "EXAMDATE";
    static String PROGRESS_COLUMN_MARK = "MARK";
    static String PROGRESS_COLUMN_TEACHER = "TEACHER";

    static String VIEW_AVERANGE_MARK = "AVERANGE_MARK";
    static String VIEW_BEST_STUDENT = "BEST_STUDENT";
    static String VIEW_BAD_STUDENT = "BAD_STUDENT";
    static String VIEW_GROUP_STATISTICS = "GROUP_STATISTICS";
    static String VIEW_FACULTY_STATISTICS = "FACULTY_STATISTICS";

    static String INDEX_PROGRESS_EXAMDATE = TABLE_PROGRESS_NAME + "_" + PROGRESS_COLUMN_EXAMDATE;
    static String INDEX_GROUP_FACULTY = TABLE_GROUP_NAME + "_" + GROUP_COLUMN_FACULTY;

    static String TRIGGER_MORE_SIX_STUDENTS = "MORE_SIX_STUDENTS";
    static String TRIGGER_LESS_THREE_STUDENTS = "LESS_THREE_STUDENTS";
    static String TRIGGER_FOR_CREATE_VIEW= "UPDATE_DATA";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    private static DatabaseHelper instance = null;

    static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS_NAME);
        db.execSQL("DROP TRIGGER IF EXISTS " + TRIGGER_LESS_THREE_STUDENTS);
        createTableSubject(db);
        createTableFaculty(db);
        createTableStudent(db);
        createTableGroup(db);
        createTableProgress(db);
        createIndex(db);
        createTrigger(db);
    }

    private void createTableSubject(SQLiteDatabase db) {
        try {
            db.rawQuery("select * from " + TABLE_SUBJECT_NAME, null);
        } catch (SQLException r) {
            db.execSQL("CREATE TABLE " + TABLE_SUBJECT_NAME + " ("
                    + SUBJECT_COLUMN_IDSUBJECT + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + SUBJECT_COLUMN_SUBJECT + " TEXT"
                    + ");");
            db.execSQL("INSERT INTO " + TABLE_SUBJECT_NAME +
                    " (" + SUBJECT_COLUMN_SUBJECT + ") " +
                    " VALUES ('КГИГ');");
            db.execSQL("INSERT INTO " + TABLE_SUBJECT_NAME +
                    " (" + SUBJECT_COLUMN_SUBJECT + ") " +
                    " VALUES ('СТПМС');");
            db.execSQL("INSERT INTO " + TABLE_SUBJECT_NAME +
                    " (" + SUBJECT_COLUMN_SUBJECT + ") " +
                    " VALUES ('БД');");
            db.execSQL("INSERT INTO " + TABLE_SUBJECT_NAME +
                    " (" + SUBJECT_COLUMN_SUBJECT + ") " +
                    " VALUES ('КСиС');");
            db.execSQL("INSERT INTO " + TABLE_SUBJECT_NAME +
                    " (" + SUBJECT_COLUMN_SUBJECT + ") " +
                    " VALUES ('ОИТ');");
            db.execSQL("INSERT INTO " + TABLE_SUBJECT_NAME +
                    " (" + SUBJECT_COLUMN_SUBJECT + ") " +
                    " VALUES ('ОАиП');");
        }
    }


    private void createTableFaculty(SQLiteDatabase db) {

        try {
            db.rawQuery("select * from " + TABLE_FACULTY_NAME, null);
        } catch (SQLException sqlException) {
            db.execSQL("CREATE TABLE " + TABLE_FACULTY_NAME + " ("
                    + FACULTY_COLUMN_IDFACULTY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + FACULTY_COLUMN_FACULTY + " TEXT,"
                    + FACULTY_COLUMN_DEAN + " TEXT,"
                    + FACULTY_COLUMN_OFFICETIMETABLE + " TEXT"
                    + ");");
            db.execSQL("INSERT INTO " + TABLE_FACULTY_NAME +
                    " (" + FACULTY_COLUMN_FACULTY + "," + FACULTY_COLUMN_DEAN + "," + FACULTY_COLUMN_OFFICETIMETABLE + ") " +
                    " VALUES ('Информационных технологий','Шиман Дмитрий Васильевич','пн-сб 8:00-16:15');");
            db.execSQL("INSERT INTO " + TABLE_FACULTY_NAME +
                    " (" + FACULTY_COLUMN_FACULTY + "," + FACULTY_COLUMN_DEAN + "," + FACULTY_COLUMN_OFFICETIMETABLE + ") " +
                    " VALUES ('Химической технологии и техники','Климош Юрий Александрович','пн-сб 8:00-16:15');");
            db.execSQL("INSERT INTO " + TABLE_FACULTY_NAME +
                    " (" + FACULTY_COLUMN_FACULTY + "," + FACULTY_COLUMN_DEAN + "," + FACULTY_COLUMN_OFFICETIMETABLE + ") " +
                    " VALUES ('Инженерно-экономический факультет','Ольферович Андрей Богданович','пн-сб 8:00-16:15');");
            db.execSQL("INSERT INTO " + TABLE_FACULTY_NAME +
                    " (" + FACULTY_COLUMN_FACULTY + "," + FACULTY_COLUMN_DEAN + "," + FACULTY_COLUMN_OFFICETIMETABLE + ") " +
                    " VALUES ('Принттехнологий и медиакоммуникаций','Долгова Татьяна Алексндровна','пн-сб 8:00-16:15');");
        }
    }

    private void createTableStudent(SQLiteDatabase db) {
        try {

            db.rawQuery("select * from " + TABLE_STUDENT_NAME, null);
        } catch (SQLException sqlException) {
            db.execSQL("CREATE TABLE " + TABLE_STUDENT_NAME + " ("
                    + STUDENT_COLUMN_IDSTUDENT + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + STUDENT_COLUMN_IDGROUP + " INTEGER,"
                    + STUDENT_COLUMN_NAME + " TEXT,"
                    + STUDENT_COLUMN_BIRTHDATE + " TEXT,"
                    + STUDENT_COLUMN_ADDRESS + " TEXT,"
                    + "  FOREIGN KEY(" + STUDENT_COLUMN_IDGROUP + ") REFERENCES " + TABLE_GROUP_NAME + "(" + GROUP_COLUMN_IDGROUP + ")"
                    + ");");
            db.execSQL("INSERT INTO " + TABLE_STUDENT_NAME +
                    " (" + STUDENT_COLUMN_IDGROUP + "," + STUDENT_COLUMN_NAME + "," + STUDENT_COLUMN_BIRTHDATE + "," + STUDENT_COLUMN_ADDRESS + ") " +
                    " VALUES (1,'Владислав','27.02.2001','ул. Белорусская 21');");
            db.execSQL("INSERT INTO " + TABLE_STUDENT_NAME +
                    " (" + STUDENT_COLUMN_IDGROUP + "," + STUDENT_COLUMN_NAME + "," + STUDENT_COLUMN_BIRTHDATE + "," + STUDENT_COLUMN_ADDRESS + ") " +
                    " VALUES (1,'Марк','10.12.2000','ул. Связистов 10-123');");
            db.execSQL("INSERT INTO " + TABLE_STUDENT_NAME +
                    " (" + STUDENT_COLUMN_IDGROUP + "," + STUDENT_COLUMN_NAME + "," + STUDENT_COLUMN_BIRTHDATE + "," + STUDENT_COLUMN_ADDRESS + ") " +
                    " VALUES (2,'Ксения','15.05.1999','ул. Аграрная 29-12');");
            db.execSQL("INSERT INTO " + TABLE_STUDENT_NAME +
                    " (" + STUDENT_COLUMN_IDGROUP + "," + STUDENT_COLUMN_NAME + "," + STUDENT_COLUMN_BIRTHDATE + "," + STUDENT_COLUMN_ADDRESS + ") " +
                    " VALUES (3,'Антон','03.01.2001','ул. Карла Маркса 40-24');");
            db.execSQL("INSERT INTO " + TABLE_STUDENT_NAME +
                    " (" + STUDENT_COLUMN_IDGROUP + "," + STUDENT_COLUMN_NAME + "," + STUDENT_COLUMN_BIRTHDATE + "," + STUDENT_COLUMN_ADDRESS + ") " +
                    " VALUES (4,'Владимир','22.02.2003','ул. Денисовская 123-32');");
            db.execSQL("INSERT INTO " + TABLE_STUDENT_NAME +
                    " (" + STUDENT_COLUMN_IDGROUP + "," + STUDENT_COLUMN_NAME + "," + STUDENT_COLUMN_BIRTHDATE + "," + STUDENT_COLUMN_ADDRESS + ") " +
                    " VALUES (5,'Руслан','12.09.2000','ул. Скорины 51-30');");
            db.execSQL("INSERT INTO " + TABLE_STUDENT_NAME +
                    " (" + STUDENT_COLUMN_IDGROUP + "," + STUDENT_COLUMN_NAME + "," + STUDENT_COLUMN_BIRTHDATE + "," + STUDENT_COLUMN_ADDRESS + ") " +
                    " VALUES (1,'Александра','15.06.2001','ул. Белорусская 21');");
            db.execSQL("INSERT INTO " + TABLE_STUDENT_NAME +
                    " (" + STUDENT_COLUMN_IDGROUP + "," + STUDENT_COLUMN_NAME + "," + STUDENT_COLUMN_BIRTHDATE + "," + STUDENT_COLUMN_ADDRESS + ") " +
                    " VALUES (1,'Анна','02.12.2001','ул. Белорусская 21');");
            db.execSQL("INSERT INTO " + TABLE_STUDENT_NAME +
                    " (" + STUDENT_COLUMN_IDGROUP + "," + STUDENT_COLUMN_NAME + "," + STUDENT_COLUMN_BIRTHDATE + "," + STUDENT_COLUMN_ADDRESS + ") " +
                    " VALUES (1,'Иван','15.12.2001','ул. Белорусская 21');");
            db.execSQL("INSERT INTO " + TABLE_STUDENT_NAME +
                    " (" + STUDENT_COLUMN_IDGROUP + "," + STUDENT_COLUMN_NAME + "," + STUDENT_COLUMN_BIRTHDATE + "," + STUDENT_COLUMN_ADDRESS + ") " +
                    " VALUES (2,'Алексей','25.09.1999','ул. Аграрная 29-12');");
            db.execSQL("INSERT INTO " + TABLE_STUDENT_NAME +
                    " (" + STUDENT_COLUMN_IDGROUP + "," + STUDENT_COLUMN_NAME + "," + STUDENT_COLUMN_BIRTHDATE + "," + STUDENT_COLUMN_ADDRESS + ") " +
                    " VALUES (2,'Петр','15.05.1999','ул. Аграрная 29-12');");
        }
    }


    private void createTableGroup(SQLiteDatabase db) {
        try {
            db.rawQuery("select * from " + TABLE_GROUP_NAME, null);
        } catch (SQLException sqlException) {
            db.execSQL("CREATE TABLE " + TABLE_GROUP_NAME + " ("
                    + GROUP_COLUMN_IDGROUP + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + GROUP_COLUMN_FACULTY + " INTEGER,"
                    + GROUP_COLUMN_COURSE + " INTEGER,"
                    + GROUP_COLUMN_NAME + " TEXT,"
                    + GROUP_COLUMN_HEAD + " INTEGER,"
                    + "  FOREIGN KEY(" + GROUP_COLUMN_FACULTY + ") REFERENCES " + TABLE_FACULTY_NAME + "(" + FACULTY_COLUMN_IDFACULTY + "),"
                    + "  FOREIGN KEY(" + GROUP_COLUMN_HEAD + ") REFERENCES " + TABLE_STUDENT_NAME + "(" + STUDENT_COLUMN_IDSTUDENT + ")"
                    + ");");
            db.execSQL("INSERT INTO " + TABLE_GROUP_NAME +
                    " (" + GROUP_COLUMN_FACULTY + "," + GROUP_COLUMN_COURSE + "," + GROUP_COLUMN_NAME + "," + GROUP_COLUMN_HEAD + ") " +
                    " VALUES (1,3,'ПОИБМС',2);");
            db.execSQL("INSERT INTO " + TABLE_GROUP_NAME +
                    " (" + GROUP_COLUMN_FACULTY + "," + GROUP_COLUMN_COURSE + "," + GROUP_COLUMN_NAME + "," + GROUP_COLUMN_HEAD + ") " +
                    " VALUES (4,4,'ИД',3);");
            db.execSQL("INSERT INTO " + TABLE_GROUP_NAME +
                    " (" + GROUP_COLUMN_FACULTY + "," + GROUP_COLUMN_COURSE + "," + GROUP_COLUMN_NAME + "," + GROUP_COLUMN_HEAD + ") " +
                    " VALUES (2,2,'ХТНВ',4);");
            db.execSQL("INSERT INTO " + TABLE_GROUP_NAME +
                    " (" + GROUP_COLUMN_FACULTY + "," + GROUP_COLUMN_COURSE + "," + GROUP_COLUMN_NAME + "," + GROUP_COLUMN_HEAD + ") " +
                    " VALUES (2,1,'ТЭП',5);");
            db.execSQL("INSERT INTO " + TABLE_GROUP_NAME +
                    " (" + GROUP_COLUMN_FACULTY + "," + GROUP_COLUMN_COURSE + "," + GROUP_COLUMN_NAME + "," + GROUP_COLUMN_HEAD + ") " +
                    " VALUES (3,3,'ЭУП',6);");

        }
    }


    private void createTableProgress(SQLiteDatabase db) {
        try {
            db.rawQuery("select * from " + TABLE_PROGRESS_NAME, null);
        } catch (SQLException sqlException) {
            db.execSQL("CREATE TABLE " + TABLE_PROGRESS_NAME + " ("
                    + PROGRESS_COLUMN_IDSTUDENT + " INTEGER,"
                    + PROGRESS_COLUMN_IDSUBJECT + " INTEGER,"
                    + PROGRESS_COLUMN_EXAMDATE + " TEXT,"
                    + PROGRESS_COLUMN_MARK + " INTEGER,"
                    + PROGRESS_COLUMN_TEACHER + " TEXT,"
                    + "  FOREIGN KEY(" + PROGRESS_COLUMN_IDSTUDENT + ") REFERENCES " + TABLE_STUDENT_NAME + "(" + STUDENT_COLUMN_IDSTUDENT + "),"
                    + "  FOREIGN KEY(" + PROGRESS_COLUMN_IDSUBJECT + ") REFERENCES " + TABLE_SUBJECT_NAME + "(" + SUBJECT_COLUMN_IDSUBJECT + ")"
                    + ");");
            db.execSQL("INSERT INTO " + TABLE_PROGRESS_NAME +
                    " (" + PROGRESS_COLUMN_IDSTUDENT + "," + PROGRESS_COLUMN_IDSUBJECT + "," + PROGRESS_COLUMN_EXAMDATE + "," + PROGRESS_COLUMN_MARK + "," + PROGRESS_COLUMN_TEACHER + ") " +
                    " VALUES (1,1,'2020-11-27',5,'Дятко');");

            db.execSQL("INSERT INTO " + TABLE_PROGRESS_NAME +
                    " (" + PROGRESS_COLUMN_IDSTUDENT + "," + PROGRESS_COLUMN_IDSUBJECT + "," + PROGRESS_COLUMN_EXAMDATE + "," + PROGRESS_COLUMN_MARK + "," + PROGRESS_COLUMN_TEACHER + ") " +
                    " VALUES (2,1,'2020-06-25',8,'Дятко');");

            db.execSQL("INSERT INTO " + TABLE_PROGRESS_NAME +
                    " (" + PROGRESS_COLUMN_IDSTUDENT + "," + PROGRESS_COLUMN_IDSUBJECT + "," + PROGRESS_COLUMN_EXAMDATE + "," + PROGRESS_COLUMN_MARK + "," + PROGRESS_COLUMN_TEACHER + ") " +
                    " VALUES (3,3,'2018-06-22',7,'Блинова');");
            db.execSQL("INSERT INTO " + TABLE_PROGRESS_NAME +
                    " (" + PROGRESS_COLUMN_IDSTUDENT + "," + PROGRESS_COLUMN_IDSUBJECT + "," + PROGRESS_COLUMN_EXAMDATE + "," + PROGRESS_COLUMN_MARK + "," + PROGRESS_COLUMN_TEACHER + ") " +
                    " VALUES (1,4,'2020-11-14',8,'Романенко');");
            db.execSQL("INSERT INTO " + TABLE_PROGRESS_NAME +
                    " (" + PROGRESS_COLUMN_IDSTUDENT + "," + PROGRESS_COLUMN_IDSUBJECT + "," + PROGRESS_COLUMN_EXAMDATE + "," + PROGRESS_COLUMN_MARK + "," + PROGRESS_COLUMN_TEACHER + ") " +
                    " VALUES (2,4,'2020-11-25',7,'Романенко');");
            db.execSQL("INSERT INTO " + TABLE_PROGRESS_NAME +
                    " (" + PROGRESS_COLUMN_IDSTUDENT + "," + PROGRESS_COLUMN_IDSUBJECT + "," + PROGRESS_COLUMN_EXAMDATE + "," + PROGRESS_COLUMN_MARK + "," + PROGRESS_COLUMN_TEACHER + ") " +
                    " VALUES (5,5,'2020-11-27',7,'Жиляк');");
            db.execSQL("INSERT INTO " + TABLE_PROGRESS_NAME +
                    " (" + PROGRESS_COLUMN_IDSTUDENT + "," + PROGRESS_COLUMN_IDSUBJECT + "," + PROGRESS_COLUMN_EXAMDATE + "," + PROGRESS_COLUMN_MARK + "," + PROGRESS_COLUMN_TEACHER + ") " +
                    " VALUES (5,3,'2019-06-22',3,'Блинова');");
            db.execSQL("INSERT INTO " + TABLE_PROGRESS_NAME +
                    " (" + PROGRESS_COLUMN_IDSTUDENT + "," + PROGRESS_COLUMN_IDSUBJECT + "," + PROGRESS_COLUMN_EXAMDATE + "," + PROGRESS_COLUMN_MARK + "," + PROGRESS_COLUMN_TEACHER + ") " +
                    " VALUES (4,2,'2019-01-14',2,'Пацей');");

        }
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createIndex(SQLiteDatabase db) {
        db.execSQL("CREATE INDEX IF NOT EXISTS " + INDEX_PROGRESS_EXAMDATE +
                " ON " + TABLE_PROGRESS_NAME + " (" + PROGRESS_COLUMN_EXAMDATE + ")");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + INDEX_GROUP_FACULTY +
                " ON " + TABLE_GROUP_NAME + " (" + GROUP_COLUMN_FACULTY + ")");
    }


    //INSERT INTO STUDENT (IDGROUP, NAME, BIRTHDATE, ADDRESS) VALUES(1,'TEST','TEST','TEST')
    //DELETE FROM STUDENT WHERE IDSTUDENT=11

    private void createTrigger(SQLiteDatabase db) {
        db.execSQL("CREATE TRIGGER IF NOT EXISTS " + TRIGGER_MORE_SIX_STUDENTS +
                " BEFORE INSERT ON " + TABLE_STUDENT_NAME +
                " Begin select RAISE (ABORT,'Не может быть больше шести студентов') WHERE (SELECT count(*) FROM STUDENT current WHERE NEW.IDGROUP=current.IDGROUP) >= 6 ;" +
                " END;");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS " + TRIGGER_LESS_THREE_STUDENTS +
                " BEFORE DELETE ON " + TABLE_STUDENT_NAME +
                " Begin select RAISE (ABORT,'Не может быть меьнше трёх студентов') WHERE (SELECT count(*) FROM STUDENT current WHERE OLD.IDGROUP=current.IDGROUP) <= 3 ;" +
                " END;");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS " + TRIGGER_FOR_CREATE_VIEW +
                " AFTER INSERT ON " + TABLE_STUDENT_NAME +
                " BEGIN " +
                " UPDATE " + TABLE_GROUP_NAME + " SET " + GROUP_COLUMN_HEAD + "=new." + STUDENT_COLUMN_IDSTUDENT + " WHERE " + TABLE_GROUP_NAME + "." + GROUP_COLUMN_IDGROUP + "; " +
                " END;");
    }
}

