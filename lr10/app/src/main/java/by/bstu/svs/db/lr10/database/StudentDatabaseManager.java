package by.bstu.svs.db.lr10.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import by.bstu.svs.db.lr10.database.exception.SQLiteDatabaseException;
import by.bstu.svs.db.lr10.model.Student;
import by.bstu.svs.db.lr10.provider.ContentProviderContract;

public class StudentDatabaseManager extends DatabaseManager<Student> {

    public StudentDatabaseManager(Context context) {
        super(context);
    }

    @Override
    public void add(Student student) throws SQLiteDatabaseException {

        ContentValues values = new ContentValues();

        if (student.getStudentId() != null) {
            values.put(DatabaseContract.StudentsTable.COLUMN_ID, student.getStudentId());
        }
        values.put(DatabaseContract.StudentsTable.COLUMN_GROUP_ID, student.getGroupId());
        values.put(DatabaseContract.StudentsTable.COLUMN_NAME, student.getName());

        resolver.insert(ContentProviderContract.CONTENT_URI_STUDENTS, values);

    }

    @Override
    public Optional<Student> getById(Integer id) {

        if (id != null) {
            Cursor cursor = resolver.query(
                    ContentProviderContract.CONTENT_URI_STUDENTS,
                    null,
                    "id = ?",
                    new String[]{id.toString()},
                    null
            );

            if (cursor.moveToFirst()) {
                return Optional.of(getByCursor(cursor));
            } else {
                return Optional.empty();
            }
        } else
            return Optional.empty();
    }

    @Override
    public List<Student> getAll() {

        List<Student> students = new ArrayList<>();

        Cursor cursor = resolver.query(
                ContentProviderContract.CONTENT_URI_STUDENTS,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                students.add(getByCursor(cursor));
            } while (cursor.moveToNext());
        }
        return students;
    }

    public List<Student> getAllByGroupId(Integer groupId) {

        List<Student> students = new ArrayList<>();

        Cursor cursor = resolver.query(
                ContentProviderContract.CONTENT_URI_STUDENTS,
                null,
                DatabaseContract.StudentsTable.COLUMN_GROUP_ID + " == ?",
                new String[]{groupId == null ? "null" : groupId.toString()},
                null
        );

        if (cursor.moveToFirst()) {
            do {
                students.add(getByCursor(cursor));
            } while (cursor.moveToNext());
        }
        return students;
    }

    @Override
    public void deleteById(Integer id) {

        resolver.delete(
                ContentProviderContract.CONTENT_URI_STUDENTS,
                "id = ?",
                new String[]{id.toString()}
        );

    }

    @Override
    public void update(Student student) {
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.StudentsTable.COLUMN_GROUP_ID, student.getGroupId());
        values.put(DatabaseContract.StudentsTable.COLUMN_NAME, student.getName());

        resolver.update(
                ContentProviderContract.CONTENT_URI_STUDENTS,
                values,
                "id = ?",
                new String[]{student.getStudentId().toString()}
        );
    }

    @Override
    public Student getByCursor(Cursor cursor) {

        int studentId = cursor.getColumnIndex(DatabaseContract.StudentsTable.COLUMN_ID);
        int groupId = cursor.getColumnIndex(DatabaseContract.StudentsTable.COLUMN_GROUP_ID);
        int name = cursor.getColumnIndex(DatabaseContract.StudentsTable.COLUMN_NAME);

        return new Student(
                cursor.getInt(studentId),
                cursor.getInt(groupId),
                cursor.getString(name)
        );
    }

    public Integer getStudentId(Student student) {

        Cursor cursor = resolver.query(
                ContentProviderContract.CONTENT_URI_STUDENTS,
                null,
                DatabaseContract.StudentsTable.COLUMN_GROUP_ID + " == ? " +
                        "and " + DatabaseContract.StudentsTable.COLUMN_NAME + " == ?",
                new String[]{student.getGroupId().toString(), student.getName()},
                null
        );

        if (cursor.moveToFirst()) {
            return getByCursor(cursor).getStudentId();
        }
        return null;
    }
}
