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

import static com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.CourseDiscussionEntry;
import static com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.CourseEntry;
import static com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.DiscussionCommentEntry;
import static com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.StudyGroupDiscussionEntry;
import static com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.StudyGroupEntry;
import static com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.StudyMaterialEntry;
import static com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.SubTopicEntry;
import static com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract.TopicEntry;

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
                NewsEntry.COLUMN_UP_VOTE_COUNT + " INTEGER DEFAULT '0', " +
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
                NewsForumTagEntry.COLUMN_TAG_KEY + " INTEGER NOT NULL, " +
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

        final String SQL_CREATE_STUDY_GROUP_TABLE = "CREATE TABLE " + StudyGroupEntry.TABLE_NAME + " (" +
                StudyGroupEntry._ID + " INTEGER PRIMARY KEY, " +
                StudyGroupEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                StudyGroupEntry.COLUMN_IMAGE_PATH + " TEXT, " +
                StudyGroupEntry.COLUMN_JOINED + " INTEGER DEFAULT '0');";

        final String SQL_CREATE_STUDY_GROUP_DISCUSSION_TABLE = "CREATE TABLE " + StudyGroupDiscussionEntry.TABLE_NAME + " (" +
                StudyGroupDiscussionEntry._ID + " INTEGER PRIMARY KEY, " +
                StudyGroupDiscussionEntry.COLUMN_FILE_PATH + " TEXT, " +
                StudyGroupDiscussionEntry.COLUMN_FILE_OFFLINE + " INTEGER DEFAULT '0', " +
                StudyGroupDiscussionEntry.COLUMN_FILE_TYPE + " TEXT, " +
                StudyGroupDiscussionEntry.COLUMN_LIKED + " INTEGER DEFAULT '0', " +
                StudyGroupDiscussionEntry.COLUMN_LIKES + " INTEGER DEFAULT '0', " +
                StudyGroupDiscussionEntry.COLUMN_MESSAGE + " TEXT NOT NULL, " +
                StudyGroupDiscussionEntry.COLUMN_READ + " INTEGER DEFAULT '0'," +
                StudyGroupDiscussionEntry.COLUMN_SENDER_ID + " INTEGER NOT NULL," +
                StudyGroupDiscussionEntry.COLUMN_SENDER_NAME + " TEXT NOT NULL," +
                StudyGroupDiscussionEntry.COLUMN_STUDY_GROUP_ID + " INTEGER NOT NULL," +
                StudyGroupDiscussionEntry.COLUMN_TIME + " TEXT" +
                " FOREIGN KEY (" + StudyGroupDiscussionEntry.COLUMN_STUDY_GROUP_ID + ") REFERENCES " +
                StudyGroupEntry.TABLE_NAME + " (" + StudyGroupEntry._ID + ")" +
                " UNIQUE (" + StudyGroupDiscussionEntry._ID + ", " +
                StudyGroupDiscussionEntry.COLUMN_SENDER_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_COURSE_TABLE = "CREATE TABLE " + CourseEntry.TABLE_NAME + " (" +
                CourseEntry._ID + " INTEGER PRIMARY KEY, " +
                CourseEntry.COLUMN_CODE + " TEXT, " +
                CourseEntry.COLUMN_DEPARTMENT_ID + " INTEGER," +
                CourseEntry.COLUMN_DEPARTMENT_NAME + " TEXT," +
                CourseEntry.COLUMN_END_DATE + " TEXT," +
                CourseEntry.COLUMN_INSTITUTION_ID + " INTEGER," +
                CourseEntry.COLUMN_INSTITUTION_NAME + " TEXT," +
                CourseEntry.COLUMN_LEVEL + " TEXT," +
                CourseEntry.COLUMN_LEVEL_ID + " INTEGER," +
                CourseEntry.COLUMN_SEMESTER_ID + " INTEGER," +
                CourseEntry.COLUMN_SEMESTER_NAME + " TEXT," +
                CourseEntry.COLUMN_SESSION_ID + " INTEGER," +
                CourseEntry.COLUMN_SESSION_NAME + " TEXT," +
                CourseEntry.COLUMN_START_DATE + " TEXT," +
                CourseEntry.COLUMN_SUMMARY + " TEXT," +
                CourseEntry.COLUMN_IMAGE + " TEXT," +
                CourseEntry.COLUMN_TITLE + " TEXT NOT NULL);";

        final String SQL_CREATE_TOPIC_TABLE = "CREATE TABLE " + TopicEntry.TABLE_NAME + " (" +
                TopicEntry._ID + " INTEGER PRIMARY KEY, " +
                TopicEntry.COLUMN_COURSE_ID + " INTEGER NOT NULL," +
                TopicEntry.COLUMN_SUMMARY + " TEXT," +
                TopicEntry.COLUMN_TOPIC + " TEXT NOT NULL," +
                " FOREIGN KEY (" + TopicEntry.COLUMN_COURSE_ID + ") REFERENCES " +
                CourseEntry.TABLE_NAME + " (" + CourseEntry._ID + "));";

        final String SQL_CREATE_SUBTOPIC_TABLE = "CREATE TABLE " + SubTopicEntry.TABLE_NAME + " (" +
                SubTopicEntry._ID + " INTEGER PRIMARY KEY, " +
                SubTopicEntry.COLUMN_SUBTOPIC + " TEXT NOT NULL," +
                SubTopicEntry.COLUMN_TOPIC_ID + " INTEGER NOT NULL," +
                SubTopicEntry.COLUMN_SUMMARY + " TEXT," +
                " FOREIGN KEY (" + SubTopicEntry.COLUMN_TOPIC_ID + ") REFERENCES " +
                TopicEntry.TABLE_NAME + " (" + TopicEntry._ID + "));";
        final String SQL_CREATE_COURSE_DISCUSSION_TABLE = "CREATE TABLE " + CourseDiscussionEntry.TABLE_NAME + " (" +
                CourseDiscussionEntry._ID + " INTEGER PRIMARY KEY, " +
                CourseDiscussionEntry.COLUMN_MESSAGE + " TEXT NOT NULL," +
                CourseDiscussionEntry.COLUMN_COURSE_ID + " INTEGER NOT NULL," +
                CourseDiscussionEntry.COLUMN_FILE_PATH + " TEXT," +
                CourseDiscussionEntry.COLUMN_FILE_TYPE + " TEXT," +
                CourseDiscussionEntry.COLUMN_FILE_OFFLINE + " INTEGER DEFAULT '0'," +
                CourseDiscussionEntry.COLUMN_COMMENT_COUNT + " INTEGER DEFAULT '0'," +
                CourseDiscussionEntry.COLUMN_SENDER_ID + " INTEGER," +
                CourseDiscussionEntry.COLUMN_SENDER_NAME + " TEXT," +
                CourseDiscussionEntry.COLUMN_LIKED + " INTEGER DEFAULT '0'," +
                CourseDiscussionEntry.COLUMN_LIKES + " INTEGER DEFAULT '0'," +
                CourseDiscussionEntry.COLUMN_TIME + " TEXT," +
                CourseDiscussionEntry.COLUMN_READ + " INTEGER DEFAULT '0'," +
                " FOREIGN KEY (" + CourseDiscussionEntry.COLUMN_COURSE_ID + ") REFERENCES " +
                CourseEntry.TABLE_NAME + " (" + CourseEntry._ID + "));";
        final String SQL_CREATE_DISCUSSION_COMMENT_TABLE = "CREATE TABLE " + DiscussionCommentEntry.TABLE_NAME + " (" +
                DiscussionCommentEntry._ID + " INTEGER PRIMARY KEY, " +
                DiscussionCommentEntry.COLUMN_MESSAGE + " TEXT NOT NULL," +
                DiscussionCommentEntry.COLUMN_DISCUSSION_ID + " INTEGER NOT NULL," +
                DiscussionCommentEntry.COLUMN_SENDER_ID + " INTEGER," +
                DiscussionCommentEntry.COLUMN_SENDER_NAME + " TEXT," +
                DiscussionCommentEntry.COLUMN_TIME + " TEXT," +
                DiscussionCommentEntry.COLUMN_READ + " INTEGER DEFAULT '0'," +
                " FOREIGN KEY (" + DiscussionCommentEntry.COLUMN_DISCUSSION_ID + ") REFERENCES " +
                CourseDiscussionEntry.TABLE_NAME + " (" + CourseDiscussionEntry._ID + "));";
        final String SQL_CREATE_STUDY_MATERIAL_TABLE = "CREATE TABLE " + StudyMaterialEntry.TABLE_NAME + " (" +
                StudyMaterialEntry._ID + " INTEGER PRIMARY KEY, " +
                StudyMaterialEntry.COLUMN_NAME + " TEXT NOT NULL," +
                StudyMaterialEntry.COLUMN_COURSE_ID + " INTEGER NOT NULL," +
                StudyMaterialEntry.COLUMN_TOPIC_ID + " INTEGER," +
                StudyMaterialEntry.COLUMN_FILE_OFFLINE + " INTEGER," +
                StudyMaterialEntry.COLUMN_FILE_PATH + " TEXT," +
                StudyMaterialEntry.COLUMN_MATERIAL_TYPE_ID + " TEXT," +
                StudyMaterialEntry.COLUMN_MATERIAL_TYPE + " INTEGER DEFAULT '0'," +
                " FOREIGN KEY (" + StudyMaterialEntry.COLUMN_COURSE_ID + ") REFERENCES " +
                CourseEntry.TABLE_NAME + " (" + CourseEntry._ID + ")," +
                " FOREIGN KEY (" + StudyMaterialEntry.COLUMN_TOPIC_ID + ") REFERENCES " +
                TopicEntry.TABLE_NAME + " (" + TopicEntry._ID + "));";

        db.execSQL(SQL_CREATE_CONTENT_REC_TABLE);
        db.execSQL(SQL_CREATE_FORUM_POST_TABLE);
        db.execSQL(SQL_CREATE_FORUM_TABLE);
        db.execSQL(SQL_CREATE_NEWS_FORUM_TAG_TABLE);
        db.execSQL(SQL_CREATE_NEWS_TABLE);
        db.execSQL(SQL_CREATE_TAG_TABLE);
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_STUDY_GROUP_TABLE);
        db.execSQL(SQL_CREATE_STUDY_GROUP_DISCUSSION_TABLE);
        db.execSQL(SQL_CREATE_COURSE_TABLE);
        db.execSQL(SQL_CREATE_TOPIC_TABLE);
        db.execSQL(SQL_CREATE_SUBTOPIC_TABLE);
        db.execSQL(SQL_CREATE_COURSE_DISCUSSION_TABLE);
        db.execSQL(SQL_CREATE_DISCUSSION_COMMENT_TABLE);
        db.execSQL(SQL_CREATE_STUDY_MATERIAL_TABLE);
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
        db.execSQL("DROP TABLE IF EXISTS " + StudyGroupEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StudyGroupDiscussionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CourseEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TopicEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SubTopicEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DiscussionCommentEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CourseDiscussionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StudyMaterialEntry.TABLE_NAME);

        onCreate(db);
    }
}
