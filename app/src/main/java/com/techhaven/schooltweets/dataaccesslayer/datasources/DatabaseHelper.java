package com.techhaven.schooltweets.dataaccesslayer.datasources;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.techhaven.schooltweets.MyApplication;
import com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.ContentReceptionEntry;
import com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.ForumRoomEntry;
import com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.ForumRoomPostEntry;
import com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.NewsEntry;
import com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.NewsForumTagEntry;
import com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.TagEntry;
import com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.UserProfileEntry;

/**
 * Created by Oluwayomi on 2/9/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "schooltweets.db";

    public static synchronized DatabaseHelper getInstance() {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(MyApplication.getAppContext());
        }
        return sInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_NEWS_TABLE = "CREATE TABLE " + NewsEntry.TABLE_NAME + " (" +
                NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NewsEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                NewsEntry.COLUMN_AUTHOR_NAME + " TEXT, " +
                NewsEntry.COLUMN_COMMENT_COUNT + " INTEGER DEFAULT '0', " +
                NewsEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                NewsEntry.COLUMN_DATE_POSTED + " TEXT NOT NULL, " +
                NewsEntry.COLUMN_IMAGE_LINK + " TEXT, " +
                NewsEntry.COLUMN_DOWN_VOTE_COUNT + " INTEGER DEFAULT '0', " +
                NewsEntry.COLUMN_UP_VOTE_COUNT + "INTEGER DEFAULT '0', " +
                " UNIQUE (" + NewsEntry.COLUMN_TITLE + ", " +
                NewsEntry.COLUMN_AUTHOR_NAME + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_FORUM_TABLE = "CREATE TABLE " + ForumRoomEntry.TABLE_NAME + " (" +
                ForumRoomEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ForumRoomEntry.COLUMN_ROOM_NAME + " TEXT NOT NULL, " +
                "UNIQUE(" + ForumRoomEntry.COLUMN_ROOM_NAME + ") ON CONFLICT IGNORE);";

        final String SQL_CREATE_FORUM_POST_TABLE = "CREATE TABLE " + ForumRoomPostEntry.TABLE_NAME + " (" +
                ForumRoomPostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ForumRoomPostEntry.COLUMN_AUTHOR_NAME + " TEXT NOT NULL, " +
                ForumRoomPostEntry.COLUMN_COMMENT_COUNT + " INTEGER DEFAULT '0', " +
                ForumRoomPostEntry.COLUMN_DATE_POSTED + " TEXT NOT NULL, " +
                ForumRoomPostEntry.COLUMN_DOWN_VOTE_COUNT + " INTEGER DEFAULT '0', " +
                ForumRoomPostEntry.COLUMN_FORUM_KEY + " INTEGER NOT NULL, " +
                ForumRoomPostEntry.COLUMN_IMAGE_LINK + " TEXT, " +
                ForumRoomPostEntry.COLUMN_POST_CONTENT + " TEXT NOT NULL, " +
                ForumRoomPostEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                ForumRoomPostEntry.COLUMN_UP_VOTE_COUNT + " INTEGER DEFAULT '0', " +
                // Set up the forum column as a foreign key to forum table.
                " FOREIGN KEY (" + ForumRoomPostEntry.COLUMN_FORUM_KEY + ") REFERENCES " +
                ForumRoomEntry.TABLE_NAME + " (" + ForumRoomEntry._ID + ") );";

        final String SQL_CREATE_TAG_TABLE = "CREATE TABLE " + TagEntry.TABLE_NAME + " (" +
                TagEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TagEntry.COLUMN_TAG_NAME + "TEXT NOT NULL, " +
                "UNIQUE(" + TagEntry._ID + ") ON CONFLICT IGNORE);";

        // A many-many relationship table between tag table, news table and forum post table
        final String SQL_CREATE_NEWS_FORUM_TAG_TABLE = "CREATE TABLE " + NewsForumTagEntry.TABLE_NAME + " (" +
                NewsForumTagEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NewsForumTagEntry.COLUMN_NEWS_KEY + " INTEGER, " +
                NewsForumTagEntry.COLUMN_FORUM_POST_KEY + " INTEGER, " +
                NewsForumTagEntry.COLUMN_TAG_KEY + "INTEGER NOT NULL, " +
                // Set up the news column as a foreign key to news table.
                " FOREIGN KEY (" + NewsForumTagEntry.COLUMN_NEWS_KEY + ") REFERENCES " +
                NewsEntry.TABLE_NAME + " (" + NewsEntry._ID + "), " +
                // Set up the tag column as a foreign key to tag table.
                " FOREIGN KEY (" + NewsForumTagEntry.COLUMN_TAG_KEY + ") REFERENCES " +
                TagEntry.TABLE_NAME + " (" + TagEntry._ID + "), " +
                // Set up the forum post column as a foreign key to news table.
                " FOREIGN KEY (" + NewsForumTagEntry.COLUMN_FORUM_POST_KEY + ") REFERENCES " +
                ForumRoomPostEntry.TABLE_NAME + " (" + ForumRoomPostEntry._ID + ") );";

        final String SQL_CREATE_CONTENT_REC_TABLE = "CREATE TABLE " + ContentReceptionEntry.TABLE_NAME + " (" +
                ContentReceptionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContentReceptionEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ContentReceptionEntry.COLUMN_DATE_TIME + " TEXT NOT NULL, " +
                ContentReceptionEntry.COLUMN_IMAGE_LINK + " TEXT, " +
                ContentReceptionEntry.COLUMN_POST_CONTENT + " TEXT NOT NULL);";

        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserProfileEntry.TABLE_NAME + " (" +
                UserProfileEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserProfileEntry.COLUMN_API_KEY + " TEXT NOT NULL, " +
                UserProfileEntry.COLUMN_EMAIL + " TEXT, " +
                UserProfileEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                UserProfileEntry.COLUMN_IMAGE_LINK + " TEXT, " +
                UserProfileEntry.COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                UserProfileEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                UserProfileEntry.COLUMN_USER_NAME + " TEXT UNIQUE NOT NULL);";
        db.execSQL(SQL_CREATE_CONTENT_REC_TABLE);
        db.execSQL(SQL_CREATE_FORUM_POST_TABLE);
        db.execSQL(SQL_CREATE_FORUM_TABLE);
        db.execSQL(SQL_CREATE_NEWS_FORUM_TAG_TABLE);
        db.execSQL(SQL_CREATE_NEWS_TABLE);
        db.execSQL(SQL_CREATE_TAG_TABLE);
        db.execSQL(SQL_CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ContentReceptionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ForumRoomPostEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ForumRoomEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NewsForumTagEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NewsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TagEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserProfileEntry.TABLE_NAME);
        onCreate(db);
    }
}
