package by.bstu.svs.db.lr10.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import java.util.List;
import java.util.Optional;

import by.bstu.svs.db.lr10.database.exception.SQLiteDatabaseException;

public abstract class DatabaseManager<T> {

    protected ContentResolver resolver;

    public DatabaseManager(Context context) {
        this.resolver = context.getContentResolver();
    }

    public abstract void add(T item) throws SQLiteDatabaseException;
    public abstract Optional<T> getById(Integer id);
    public abstract List<T> getAll() throws SQLiteDatabaseException;
    public abstract void deleteById(Integer id);
    public abstract void update(T item) throws SQLiteDatabaseException;
    public abstract T getByCursor(Cursor cursor);

}
