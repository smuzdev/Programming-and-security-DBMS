package by.bstu.svs.db.lr10.provider;

import android.content.ContentUris;
import android.net.Uri;

import by.bstu.svs.db.lr10.database.DatabaseContract;

public final class ContentProviderContract {
    public static final String AUTHORITY    = "by.bstu.svs.db.lr10";
    public static final Uri AUTHORITY_URI   = Uri.parse("content://" + AUTHORITY);

    public static final Uri CONTENT_URI_GROUPS = Uri.withAppendedPath(AUTHORITY_URI, DatabaseContract.GroupsTable.TABLE_NAME);
    public static final Uri CONTENT_URI_STUDENTS = Uri.withAppendedPath(AUTHORITY_URI, DatabaseContract.StudentsTable.TABLE_NAME);

    public static Uri buildGroupUri(long taskId){
        return ContentUris.withAppendedId(CONTENT_URI_GROUPS, taskId);
    }
    public static Uri buildStudentUri(long taskId){
        return ContentUris.withAppendedId(CONTENT_URI_STUDENTS, taskId);
    }

    static long getId(Uri uri){
        return ContentUris.parseId(uri);
    }

}
