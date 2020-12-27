package by.bstu.svs.db.lr10.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public DatabaseOpenHelper(@Nullable Context context) {
        super(
                context,
                DatabaseContract.DATABASE_NAME,
                null,
                DatabaseContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseContract.StudentsTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContract.GroupsTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DatabaseContract.StudentsTable.DELETE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContract.GroupsTable.DELETE_TABLE);
        this.onCreate(sqLiteDatabase);
    }

    @Override
    public void onConfigure(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
        super.onConfigure(sqLiteDatabase);
    }
}
