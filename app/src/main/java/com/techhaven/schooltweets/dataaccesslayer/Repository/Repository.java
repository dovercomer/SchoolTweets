package com.techhaven.schooltweets.dataaccesslayer.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.techhaven.schooltweets.dataaccesslayer.datasources.DatabaseHelper;

/**
 * Created by Oluwayomi on 2/11/2016.
 */
public class Repository<T> implements IRepository<T> {
    DatabaseHelper mHelper;
    SQLiteDatabase db;

    public Repository(Context context) {
        mHelper = new DatabaseHelper(context);
        db = mHelper.getWritableDatabase();
    }

    @Override
    public long Create(String tableName, ContentValues values) {
        return db.insert(tableName, null, values);
    }

    @Override
    public boolean Edit(String tableName, long id, ContentValues values) {
        int row = db.update(tableName, values, "id=?", new String[]{id + ""});
        if (row > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Cursor GetAll(String tableName) {
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        return cursor;
    }

    @Override
    public Cursor GetById(String tableName, int id) {
        Cursor cursor = db.query(tableName, null, "id=?", new String[]{id + ""}, null, null, null);
        return cursor;
    }

    @Override
    public boolean Delete(String tableName, long id) {
        int row = db.delete(tableName, "id=?", new String[]{id + ""});
        if (row > 0) {
            return true;
        }
        return false;
    }
}
