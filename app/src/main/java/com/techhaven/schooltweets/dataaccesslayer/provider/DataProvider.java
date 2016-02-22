package com.techhaven.schooltweets.dataaccesslayer.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract;
import com.techhaven.schooltweets.dataaccesslayer.datasources.DatabaseHelper;

/**
 * Created by Oluwayomi on 2/20/2016.
 */
public class DataProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DatabaseHelper mOpenHelper;

    private static final int NEWS = 100;
    private static final int NEWS_WITH_STARTDATE = 101;
    private static final int NEWS_ID = 102;
    private static final int NEWS_WITH_TAG = 103;
    private static final int TAG = 300;
    private static final int TAG_ID = 301;
    private static final int NEWS_FORUM_TAG = 400;
    private static final int FORUM_ROOM = 500;
    private static final int FORUM_ROOM_ID = 501;
    private static final int FORUM_POST = 600;
    private static final int FORUM_POST_WITH_ROOM = 601;
    private static final int FORUM_POST_ID = 602;
    private static final int FORUM_POST_WITH_TAG = 603;
    private static final int CONTENT_RECEPTION = 700;
    private static final int CONTENT_RECEPTION_ID = 701;
    private static final int USER_PROFILE = 800;
    private static final int USER_PROFILE_ID = 801;


    private static final SQLiteQueryBuilder sForumPostByForumRoomQueryBuilder;

    static {
        sForumPostByForumRoomQueryBuilder = new SQLiteQueryBuilder();
        sForumPostByForumRoomQueryBuilder.setTables(
                DataContract.ForumRoomPostEntry.TABLE_NAME + " INNER JOIN " +
                        DataContract.ForumRoomEntry.TABLE_NAME +
                        " ON " + DataContract.ForumRoomPostEntry.TABLE_NAME +
                        "." + DataContract.ForumRoomPostEntry.COLUMN_FORUM_KEY +
                        " = " + DataContract.ForumRoomEntry.TABLE_NAME +
                        "." + DataContract.ForumRoomEntry._ID);
    }

    private static final SQLiteQueryBuilder sForumPostByTagQueryBuilder;

    static {
        sForumPostByTagQueryBuilder = new SQLiteQueryBuilder();
        sForumPostByTagQueryBuilder.setTables(
                DataContract.ForumRoomPostEntry.TABLE_NAME + " INNER JOIN " +
                        DataContract.NewsForumTagEntry.TABLE_NAME +
                        " ON " + DataContract.ForumRoomPostEntry.TABLE_NAME +
                        "." + DataContract.ForumRoomPostEntry._ID +
                        " = " + DataContract.NewsForumTagEntry.TABLE_NAME +
                        "." + DataContract.NewsForumTagEntry.COLUMN_FORUM_POST_KEY +
                        " INNER JOIN " + DataContract.TagEntry.TABLE_NAME +
                        " ON " + DataContract.NewsForumTagEntry.TABLE_NAME +
                        "." + DataContract.NewsForumTagEntry.COLUMN_TAG_KEY +
                        " = " + DataContract.TagEntry.TABLE_NAME +
                        "." + DataContract.TagEntry._ID);
    }

    private static final SQLiteQueryBuilder sNewsByTagQueryBuilder;

    static {
        sNewsByTagQueryBuilder = new SQLiteQueryBuilder();
        sNewsByTagQueryBuilder.setTables(
                DataContract.NewsEntry.TABLE_NAME + " INNER JOIN " +
                        DataContract.NewsForumTagEntry.TABLE_NAME +
                        " ON " + DataContract.NewsEntry.TABLE_NAME +
                        "." + DataContract.NewsEntry._ID +
                        " = " + DataContract.NewsForumTagEntry.TABLE_NAME +
                        "." + DataContract.NewsForumTagEntry.COLUMN_NEWS_KEY +
                        " INNER JOIN " + DataContract.TagEntry.TABLE_NAME +
                        " ON " + DataContract.NewsForumTagEntry.TABLE_NAME +
                        "." + DataContract.NewsForumTagEntry.COLUMN_TAG_KEY +
                        " = " + DataContract.TagEntry.TABLE_NAME +
                        "." + DataContract.TagEntry._ID);
    }

    private static final String sTagSelection =
            DataContract.TagEntry.TABLE_NAME +
                    "." + DataContract.TagEntry.COLUMN_TAG_NAME + " = ? ";
    private static final String sTagWithStartDateSelection =
            DataContract.TagEntry.TABLE_NAME +
                    "." + DataContract.TagEntry.COLUMN_TAG_NAME + " = ? AND " +
                    DataContract.NewsEntry.COLUMN_DATE_POSTED + " >= ? ";
    private static final String sRoomSelection =
            DataContract.ForumRoomEntry.TABLE_NAME +
                    "." + DataContract.ForumRoomEntry.COLUMN_ROOM_NAME + " = ? ";

    private Cursor getNewsByTag(Uri uri, String[] projection, String sortOrder) {
        String tag = DataContract.NewsEntry.getTagFromUri(uri);
        String startDate = DataContract.NewsEntry.getStartDateFromUri(uri);

        String[] selectionArgs;
        String selection;

        if (startDate == null) {
            selection = sTagSelection;
            selectionArgs = new String[]{tag};
        } else {
            selectionArgs = new String[]{tag, startDate};
            selection = sTagWithStartDateSelection;
        }

        return sNewsByTagQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getForumPostByTag(Uri uri, String[] projection, String sortOrder) {
        String tag = DataContract.ForumRoomPostEntry.getTagFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sTagSelection;
        selectionArgs = new String[]{tag};


        return sForumPostByTagQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getForumPostByRoom(Uri uri, String[] projection, String sortOrder) {
        String room = DataContract.ForumRoomPostEntry.getRoomFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sRoomSelection;
        selectionArgs = new String[]{room};


        return sForumPostByForumRoomQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, DataContract.PATH_NEWS, NEWS);
        matcher.addURI(authority, DataContract.PATH_NEWS + "/*", NEWS_WITH_STARTDATE);
        matcher.addURI(authority, DataContract.PATH_NEWS + "/#", NEWS_ID);
        matcher.addURI(authority, DataContract.PATH_NEWS + "/tag/*", NEWS_FORUM_TAG);

        matcher.addURI(authority, DataContract.PATH_FORUM_ROOM, FORUM_ROOM);
        matcher.addURI(authority, DataContract.PATH_FORUM_ROOM + "/#", FORUM_ROOM_ID);

        matcher.addURI(authority, DataContract.PATH_FORUM_ROOM_POST, FORUM_POST);
        matcher.addURI(authority, DataContract.PATH_FORUM_ROOM_POST + "/*", FORUM_POST_WITH_ROOM);
        matcher.addURI(authority, DataContract.PATH_FORUM_ROOM_POST + "/#", FORUM_POST_ID);
        matcher.addURI(authority, DataContract.PATH_FORUM_ROOM_POST + "/tag/*", FORUM_POST_WITH_TAG);

        matcher.addURI(authority, DataContract.PATH_TAG, TAG);
        matcher.addURI(authority, DataContract.PATH_TAG + "/#", TAG_ID);

        matcher.addURI(authority, DataContract.PATH_NEWS_FORUM_TAG, NEWS_FORUM_TAG);

        matcher.addURI(authority, DataContract.PATH_CONTENT_RECEPTION, CONTENT_RECEPTION);
        matcher.addURI(authority, DataContract.PATH_CONTENT_RECEPTION + "/#", CONTENT_RECEPTION_ID);

        matcher.addURI(authority, DataContract.PATH_USER_PROFILE, USER_PROFILE);
        matcher.addURI(authority, DataContract.PATH_USER_PROFILE + "/#", USER_PROFILE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case NEWS_WITH_TAG:
                retCursor = getNewsByTag(uri, projection, sortOrder);
                break;
            case NEWS_WITH_STARTDATE:
                selection = DataContract.NewsEntry.COLUMN_DATE_POSTED + " >= ? ";
                selectionArgs = new String[]{DataContract.NewsEntry.getStartDateFromUri(uri)};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.NewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case NEWS_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.NewsEntry.TABLE_NAME,
                        projection,
                        DataContract.NewsEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case NEWS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.NewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case NEWS_FORUM_TAG:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.NewsForumTagEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case TAG_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.TagEntry.TABLE_NAME,
                        projection,
                        DataContract.TagEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TAG:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.TagEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FORUM_ROOM_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.ForumRoomEntry.TABLE_NAME,
                        projection,
                        DataContract.ForumRoomEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FORUM_ROOM:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.ForumRoomEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FORUM_POST_WITH_TAG:
                retCursor = getForumPostByTag(uri, projection, sortOrder);
                break;
            case FORUM_POST_WITH_ROOM:
                retCursor = getForumPostByRoom(uri, projection, sortOrder);
                break;
            case FORUM_POST_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.ForumRoomPostEntry.TABLE_NAME,
                        projection,
                        DataContract.ForumRoomPostEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FORUM_POST:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.ForumRoomPostEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CONTENT_RECEPTION_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.ContentReceptionEntry.TABLE_NAME,
                        projection,
                        DataContract.ContentReceptionEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CONTENT_RECEPTION:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.ContentReceptionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case USER_PROFILE_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.UserProfileEntry.TABLE_NAME,
                        projection,
                        DataContract.UserProfileEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case USER_PROFILE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.UserProfileEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                return DataContract.NewsEntry.CONTENT_TYPE;
            case NEWS_FORUM_TAG:
                return DataContract.NewsForumTagEntry.CONTENT_TYPE;
            case NEWS_ID:
                return DataContract.NewsEntry.CONTENT_ITEM_TYPE;
            case NEWS_WITH_STARTDATE:
                return DataContract.NewsEntry.CONTENT_TYPE;
            case NEWS_WITH_TAG:
                return DataContract.NewsEntry.CONTENT_TYPE;
            case TAG:
                return DataContract.TagEntry.CONTENT_TYPE;
            case TAG_ID:
                return DataContract.TagEntry.CONTENT_ITEM_TYPE;
            case FORUM_ROOM:
                return DataContract.ForumRoomEntry.CONTENT_TYPE;
            case FORUM_ROOM_ID:
                return DataContract.ForumRoomEntry.CONTENT_ITEM_TYPE;
            case FORUM_POST:
                return DataContract.ForumRoomPostEntry.CONTENT_TYPE;
            case FORUM_POST_WITH_ROOM:
                return DataContract.ForumRoomPostEntry.CONTENT_TYPE;
            case FORUM_POST_ID:
                return DataContract.ForumRoomPostEntry.CONTENT_ITEM_TYPE;
            case FORUM_POST_WITH_TAG:
                return DataContract.ForumRoomPostEntry.CONTENT_TYPE;
            case CONTENT_RECEPTION:
                return DataContract.ContentReceptionEntry.CONTENT_TYPE;
            case CONTENT_RECEPTION_ID:
                return DataContract.ContentReceptionEntry.CONTENT_ITEM_TYPE;
            case USER_PROFILE:
                return DataContract.UserProfileEntry.CONTENT_TYPE;
            case USER_PROFILE_ID:
                return DataContract.UserProfileEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case NEWS: {
                long _id = db.insert(DataContract.NewsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.NewsEntry.buildNewsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TAG: {
                long _id = db.insert(DataContract.TagEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.TagEntry.buildTagUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FORUM_ROOM: {
                long _id = db.insert(DataContract.ForumRoomEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.ForumRoomEntry.buildForumRoomUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FORUM_POST: {
                long _id = db.insert(DataContract.ForumRoomPostEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.ForumRoomPostEntry.buildForumPostUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CONTENT_RECEPTION: {
                long _id = db.insert(DataContract.ContentReceptionEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.ContentReceptionEntry.buildContentReceptionUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case USER_PROFILE: {
                long _id = db.insert(DataContract.UserProfileEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.UserProfileEntry.buildUserUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case NEWS:
                rowsDeleted = db.delete(
                        DataContract.NewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TAG:
                rowsDeleted = db.delete(
                        DataContract.TagEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FORUM_ROOM:
                rowsDeleted = db.delete(
                        DataContract.ForumRoomEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FORUM_POST:
                rowsDeleted = db.delete(
                        DataContract.ForumRoomPostEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CONTENT_RECEPTION:
                rowsDeleted = db.delete(
                        DataContract.ContentReceptionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER_PROFILE:
                rowsDeleted = db.delete(
                        DataContract.UserProfileEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case NEWS:
                rowsUpdated = db.update(DataContract.NewsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TAG:
                rowsUpdated = db.update(DataContract.TagEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FORUM_ROOM:
                rowsUpdated = db.update(DataContract.ForumRoomEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FORUM_POST:
                rowsUpdated = db.update(DataContract.ForumRoomPostEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CONTENT_RECEPTION:
                rowsUpdated = db.update(DataContract.ContentReceptionEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case USER_PROFILE:
                rowsUpdated = db.update(DataContract.UserProfileEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.NewsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case FORUM_ROOM: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.ForumRoomEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case FORUM_POST: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.ForumRoomPostEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
