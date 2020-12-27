package by.bstu.svs.db.lr10.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import by.bstu.svs.db.lr10.database.DatabaseContract;
import by.bstu.svs.db.lr10.database.DatabaseOpenHelper;

public class AppProvider extends ContentProvider {

    public static final int FULL = 100;
    public static final int ID = 101;

    private DatabaseOpenHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(ContentProviderContract.AUTHORITY, DatabaseContract.GroupsTable.TABLE_NAME, FULL);
        matcher.addURI(ContentProviderContract.AUTHORITY, DatabaseContract.StudentsTable.TABLE_NAME, FULL);

        matcher.addURI(ContentProviderContract.AUTHORITY, DatabaseContract.GroupsTable.TABLE_NAME + "/#", ID);
        matcher.addURI(ContentProviderContract.AUTHORITY, DatabaseContract.StudentsTable.TABLE_NAME + "/#", ID);
        return matcher;
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch(match){
            case FULL:
                queryBuilder.setTables(uri.getPath().replace("/", ""));
                break;
            case ID:
                queryBuilder.setTables(uri.getPath().replace("/", ""));
                long taskId = ContentProviderContract.getId(uri);
                queryBuilder.appendWhere(DatabaseContract.GroupsTable.COLUMN_ID + " = " + taskId);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db;
        Uri returnUri;
        long recordId;

        switch(match){
            case FULL:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(uri.getPath().replace("/", ""), null, contentValues);
                if(recordId > 0){
                    if(uri.getPath().replace("/", "").equals(DatabaseContract.GroupsTable.TABLE_NAME)) {
                        returnUri = ContentProviderContract.buildGroupUri(recordId);
                    } else {
                        returnUri = ContentProviderContract.buildStudentUri(recordId);
                    }
                }
                else{
                    throw new SQLException("Failed to insert: " + uri.toString());
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        String selectionCriteria = selection;

        if(match==ID) {
            long taskId = ContentProviderContract.getId(uri);
            selectionCriteria = DatabaseContract.GroupsTable.COLUMN_ID + " = " + taskId;
            if ((selection != null) && (selection.length() > 0)) {
                selectionCriteria += " AND (" + selection + ")";
            }
        }
        return db.delete(uri.getPath().replace("/", ""), selectionCriteria, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        String selectionCriteria = selection;

        if(match==ID) {
            long taskId = ContentProviderContract.getId(uri);
            selectionCriteria = DatabaseContract.GroupsTable.COLUMN_ID + " = " + taskId;
            if ((selection != null) && (selection.length() > 0)) {
                selectionCriteria += " AND (" + selection + ")";
            }
        }
        return db.update(uri.getPath().replace("/", ""), contentValues, selectionCriteria, selectionArgs);
    }
}
