package by.bstu.svs.db.lr10.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import by.bstu.svs.db.lr10.database.exception.SQLiteDatabaseException;
import by.bstu.svs.db.lr10.model.Group;
import by.bstu.svs.db.lr10.provider.ContentProviderContract;

public class GroupDatabaseManager extends DatabaseManager<Group> {

    public GroupDatabaseManager(Context context) {
        super(context);
    }

    @Override
    public void add(Group group) throws SQLiteDatabaseException {
        ContentValues values = new ContentValues();

        if (group.getGroupId() != null) {
            values.put(DatabaseContract.GroupsTable.COLUMN_ID, group.getGroupId());
        }
        values.put(DatabaseContract.GroupsTable.COLUMN_COURSE, group.getCourse());
        values.put(DatabaseContract.GroupsTable.COLUMN_FACULTY, group.getFaculty());
        values.put(DatabaseContract.GroupsTable.COLUMN_HEAD_ID, group.getHeadId());
        values.put(DatabaseContract.GroupsTable.COLUMN_NAME, group.getName());

        resolver.insert(ContentProviderContract.CONTENT_URI_GROUPS, values);

    }

    @Override
    public Optional<Group> getById(Integer id) {
        Cursor cursor = resolver.query(
                ContentProviderContract.buildGroupUri(id),
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            return Optional.of(getByCursor(cursor));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Group> getAll() {
        List<Group> groups = new ArrayList<>();

        Cursor cursor = resolver.query(
                ContentProviderContract.CONTENT_URI_GROUPS,
                null,
                null,
                null,
                null
        );

         if (cursor.moveToFirst()) {
             do {
                 groups.add(getByCursor(cursor));
             } while (cursor.moveToNext());
         }
        return groups;
    }

    @Override
    public void deleteById(Integer id) {
        resolver.delete(
                ContentProviderContract.CONTENT_URI_GROUPS,
                "id = ?",
                new String[]{id.toString()}
        );
    }

    @Override
    public void update(Group group) throws SQLiteDatabaseException {

        try {
            ContentValues values = new ContentValues();

            values.put(DatabaseContract.GroupsTable.COLUMN_COURSE, group.getCourse());
            values.put(DatabaseContract.GroupsTable.COLUMN_FACULTY, group.getFaculty());
            values.put(DatabaseContract.GroupsTable.COLUMN_HEAD_ID, group.getHeadId());
            values.put(DatabaseContract.GroupsTable.COLUMN_NAME, group.getName());

            resolver.update(
                    ContentProviderContract.CONTENT_URI_GROUPS,
                    values,
                    "id = ?",
                    new String[]{group.getGroupId().toString()}
            );
        } catch (Exception e) {
            throw new SQLiteDatabaseException(e.getMessage());
        }
    }

    @Override
    public Group getByCursor(Cursor cursor) {
        int id = cursor.getColumnIndex(DatabaseContract.GroupsTable.COLUMN_ID);
        int course = cursor.getColumnIndex(DatabaseContract.GroupsTable.COLUMN_COURSE);
        int name = cursor.getColumnIndex(DatabaseContract.GroupsTable.COLUMN_NAME);
        int faculty = cursor.getColumnIndex(DatabaseContract.GroupsTable.COLUMN_FACULTY);
        int headId = cursor.getColumnIndex(DatabaseContract.GroupsTable.COLUMN_HEAD_ID);


        Integer headIdValue = cursor.isNull(headId) ? null : cursor.getInt(headId);
        return new Group(
                cursor.getInt(id),
                cursor.getInt(course),
                cursor.getString(faculty),
                cursor.getString(name),
                headIdValue
        );
    }

    public Integer getGroupId(Group group) {

        Cursor cursor = resolver.query(
                ContentProviderContract.CONTENT_URI_GROUPS,
                null,
                DatabaseContract.GroupsTable.COLUMN_COURSE + " == ? " +
                        "and " + DatabaseContract.GroupsTable.COLUMN_FACULTY + " == ? " +
                        "and " + DatabaseContract.GroupsTable.COLUMN_NAME + " == ?",
                new String[]{group.getCourse().toString(), group.getFaculty(), group.getName()},
                null
        );

        if (cursor.moveToFirst()) {
            return getByCursor(cursor).getGroupId();
        }
        return null;
    }
}
