package com.techhaven.schooltweets.dataaccesslayer.Repository;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Oluwayomi on 2/11/2016.
 */
public interface IRepository<T> {
    public long Create(String tableName, ContentValues values);

    public boolean Edit(String tableName, long id, ContentValues values);

    public Cursor GetAll(String tableName);

    public Cursor GetById(String tableName, int id);

    public boolean Delete(String tableName, long id);
}
