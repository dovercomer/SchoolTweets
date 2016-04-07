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

    private static final int STUDY_GROUP = 900;
    private static final int STUDY_GROUP_ID = 902;
    private static final int STUDY_GROUP_DISCUSSION = 1000;
    private static final int STUDY_GROUP_DISCUSSION_ID = 1001;
    private static final int DISCUSSION_WITH_GROUP = 1002;
    private static final int STUDY_MATERIAL = 1100;
    private static final int STUDY_MATERIAL_ID = 1101;
    private static final int STUDY_MATERIAL_WITH_COURSE = 1102;
    private static final int STUDY_MATERIAL_WITH_TOPIC = 1103;
    private static final int COURSE = 1200;
    private static final int COURSE_ID = 1204;
    private static final int COURSE_DISCUSSION = 1300;
    private static final int COURSE_DISCUSSION_ID = 1301;
    private static final int COURSE_DISCUSSION_WITH_COURSE = 1302;
    private static final int DISCUSSION_COMMENT = 1400;
    private static final int DISCUSSION_COMMENT_ID = 1401;
    private static final int COMMENT_WITH_DISCUSSION = 1402;
    private static final int TOPIC = 1500;
    private static final int TOPIC_WITH_COURSE = 1501;
    private static final int TOPIC_ID = 1502;
    private static final int SUBTOPIC = 1600;
    private static final int SUBTOPIC_ID = 1601;
    private static final int SUBTOPIC_WITH_TOPIC = 1602;


    private static final SQLiteQueryBuilder sDiscussionByStudyGroupQueryBuilder;

    static {
        sDiscussionByStudyGroupQueryBuilder = new SQLiteQueryBuilder();
        sDiscussionByStudyGroupQueryBuilder.setTables(
                DataContract.StudyGroupDiscussionEntry.TABLE_NAME + " INNER JOIN " +
                        DataContract.StudyGroupEntry.TABLE_NAME +
                        " ON " + DataContract.StudyGroupDiscussionEntry.TABLE_NAME +
                        "." + DataContract.StudyGroupDiscussionEntry.COLUMN_STUDY_GROUP_ID +
                        " = " + DataContract.StudyGroupEntry.TABLE_NAME +
                        "." + DataContract.StudyGroupEntry._ID);
    }

    private static final SQLiteQueryBuilder sMaterialByCourseQueryBuilder;

    static {
        sMaterialByCourseQueryBuilder = new SQLiteQueryBuilder();
        sMaterialByCourseQueryBuilder.setTables(
                DataContract.StudyMaterialEntry.TABLE_NAME + " INNER JOIN " +
                        DataContract.CourseEntry.TABLE_NAME +
                        " ON " + DataContract.StudyMaterialEntry.TABLE_NAME +
                        "." + DataContract.StudyMaterialEntry.COLUMN_COURSE_ID +
                        " = " + DataContract.CourseEntry.TABLE_NAME +
                        "." + DataContract.CourseEntry._ID);
    }

    private static final SQLiteQueryBuilder sMaterialByTopicQueryBuilder;

    static {
        sMaterialByTopicQueryBuilder = new SQLiteQueryBuilder();
        sMaterialByTopicQueryBuilder.setTables(
                DataContract.StudyMaterialEntry.TABLE_NAME + " INNER JOIN " +
                        DataContract.TopicEntry.TABLE_NAME +
                        " ON " + DataContract.StudyMaterialEntry.TABLE_NAME +
                        "." + DataContract.StudyMaterialEntry.COLUMN_TOPIC_ID +
                        " = " + DataContract.TopicEntry.TABLE_NAME +
                        "." + DataContract.TopicEntry._ID);
    }

    private static final SQLiteQueryBuilder sTopicByCourseQueryBuilder;

    static {
        sTopicByCourseQueryBuilder = new SQLiteQueryBuilder();
        sTopicByCourseQueryBuilder.setTables(
                DataContract.TopicEntry.TABLE_NAME + " INNER JOIN " +
                        DataContract.CourseEntry.TABLE_NAME +
                        " ON " + DataContract.TopicEntry.TABLE_NAME +
                        "." + DataContract.TopicEntry.COLUMN_COURSE_ID +
                        " = " + DataContract.CourseEntry.TABLE_NAME +
                        "." + DataContract.CourseEntry._ID);
    }

    private static final SQLiteQueryBuilder sSubByTopicQueryBuilder;

    static {
        sSubByTopicQueryBuilder = new SQLiteQueryBuilder();
        sSubByTopicQueryBuilder.setTables(
                DataContract.SubTopicEntry.TABLE_NAME + " INNER JOIN " +
                        DataContract.TopicEntry.TABLE_NAME +
                        " ON " + DataContract.SubTopicEntry.TABLE_NAME +
                        "." + DataContract.SubTopicEntry.COLUMN_TOPIC_ID +
                        " = " + DataContract.TopicEntry.TABLE_NAME +
                        "." + DataContract.TopicEntry._ID);
    }

    private static final SQLiteQueryBuilder sDiscussionByCourseQueryBuilder;

    static {
        sDiscussionByCourseQueryBuilder = new SQLiteQueryBuilder();
        sDiscussionByCourseQueryBuilder.setTables(
                DataContract.CourseDiscussionEntry.TABLE_NAME + " INNER JOIN " +
                        DataContract.CourseEntry.TABLE_NAME +
                        " ON " + DataContract.CourseDiscussionEntry.TABLE_NAME +
                        "." + DataContract.CourseDiscussionEntry.COLUMN_COURSE_ID +
                        " = " + DataContract.CourseEntry.TABLE_NAME +
                        "." + DataContract.CourseEntry._ID);
    }

    private static final SQLiteQueryBuilder sCommentByDiscussionQueryBuilder;

    static {
        sCommentByDiscussionQueryBuilder = new SQLiteQueryBuilder();
        sCommentByDiscussionQueryBuilder.setTables(
                DataContract.DiscussionCommentEntry.TABLE_NAME + " INNER JOIN " +
                        DataContract.CourseDiscussionEntry.TABLE_NAME +
                        " ON " + DataContract.DiscussionCommentEntry.TABLE_NAME +
                        "." + DataContract.DiscussionCommentEntry.COLUMN_DISCUSSION_ID +
                        " = " + DataContract.CourseDiscussionEntry.TABLE_NAME +
                        "." + DataContract.CourseDiscussionEntry._ID);
    }

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
    private static final String sGroupSelection =
            DataContract.StudyGroupEntry.TABLE_NAME +
                    "." + DataContract.StudyGroupEntry.COLUMN_TITLE + " = ? ";
    private static final String sCourseSelection =
            DataContract.CourseEntry.TABLE_NAME +
                    "." + DataContract.CourseEntry.COLUMN_TITLE + " = ? ";
    private static final String sTopicSelection =
            DataContract.TopicEntry.TABLE_NAME +
                    "." + DataContract.TopicEntry.COLUMN_TOPIC + " = ? ";
    private static final String sDiscussionSelection =
            DataContract.CourseDiscussionEntry.TABLE_NAME +
                    "." + DataContract.CourseDiscussionEntry._ID + " = ? ";

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

    private Cursor getDiscussionByStudyGroup(Uri uri, String[] projection, String sortOrder) {
        String group = DataContract.StudyGroupDiscussionEntry.getGroupFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sGroupSelection;
        selectionArgs = new String[]{group};


        return sDiscussionByStudyGroupQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMaterialByCourse(Uri uri, String[] projection, String sortOrder) {
        String course = DataContract.StudyMaterialEntry.getCourseFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sCourseSelection;
        selectionArgs = new String[]{course};


        return sMaterialByCourseQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMaterialByTopic(Uri uri, String[] projection, String sortOrder) {
        String topic = DataContract.StudyMaterialEntry.getTopicFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sTopicSelection;
        selectionArgs = new String[]{topic};


        return sMaterialByTopicQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTopicByCourse(Uri uri, String[] projection, String sortOrder) {
        String course = DataContract.TopicEntry.getCourseFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sCourseSelection;
        selectionArgs = new String[]{course};


        return sTopicByCourseQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getSubByTopic(Uri uri, String[] projection, String sortOrder) {
        String topic = DataContract.SubTopicEntry.getTopicFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sTopicSelection;
        selectionArgs = new String[]{topic};


        return sSubByTopicQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getDiscussionByCourse(Uri uri, String[] projection, String sortOrder) {
        String course = DataContract.CourseDiscussionEntry.getCourseFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sCourseSelection;
        selectionArgs = new String[]{course};


        return sDiscussionByCourseQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getCommentByDiscussion(Uri uri, String[] projection, String sortOrder) {
        String discussion = DataContract.DiscussionCommentEntry.getDiscussionFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sDiscussionSelection;
        selectionArgs = new String[]{discussion};


        return sCommentByDiscussionQueryBuilder.query(mOpenHelper.getReadableDatabase(),
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

        matcher.addURI(authority, DataContract.PATH_STUDY_GROUP, STUDY_GROUP);
        matcher.addURI(authority, DataContract.PATH_STUDY_GROUP + "/#", STUDY_GROUP_ID);

        matcher.addURI(authority, DataContract.PATH_STUDY_GROUP_DISCUSSION, STUDY_GROUP_DISCUSSION);
        matcher.addURI(authority, DataContract.PATH_STUDY_GROUP_DISCUSSION + "/#", STUDY_GROUP_DISCUSSION_ID);
        matcher.addURI(authority, DataContract.PATH_STUDY_GROUP_DISCUSSION + "/*", DISCUSSION_WITH_GROUP);

        matcher.addURI(authority, DataContract.PATH_STUDY_MATERIAL, STUDY_MATERIAL);
        matcher.addURI(authority, DataContract.PATH_STUDY_MATERIAL + "/#", STUDY_MATERIAL_ID);
        matcher.addURI(authority, DataContract.PATH_STUDY_MATERIAL + "/*", STUDY_MATERIAL_WITH_COURSE);
        matcher.addURI(authority, DataContract.PATH_STUDY_MATERIAL + "/*/*", STUDY_MATERIAL_WITH_TOPIC);

        matcher.addURI(authority, DataContract.PATH_COURSE, COURSE);
        matcher.addURI(authority, DataContract.PATH_COURSE + "/#", COURSE_ID);

        matcher.addURI(authority, DataContract.PATH_COURSE_DISCUSSION, COURSE_DISCUSSION);
        matcher.addURI(authority, DataContract.PATH_COURSE_DISCUSSION + "/#", COURSE_DISCUSSION_ID);
        matcher.addURI(authority, DataContract.PATH_COURSE_DISCUSSION + "/*", COURSE_DISCUSSION_WITH_COURSE);

        matcher.addURI(authority, DataContract.PATH_DISCUSSION_COMMENT, DISCUSSION_COMMENT);
        matcher.addURI(authority, DataContract.PATH_DISCUSSION_COMMENT + "/#", DISCUSSION_COMMENT_ID);
        matcher.addURI(authority, DataContract.PATH_DISCUSSION_COMMENT + "/*", COMMENT_WITH_DISCUSSION);

        matcher.addURI(authority, DataContract.PATH_TOPIC, TOPIC);
        matcher.addURI(authority, DataContract.PATH_TOPIC + "/#", TOPIC_ID);
        matcher.addURI(authority, DataContract.PATH_TOPIC + "/*", TOPIC_WITH_COURSE);

        matcher.addURI(authority, DataContract.PATH_SUBTOPIC, SUBTOPIC);
        matcher.addURI(authority, DataContract.PATH_SUBTOPIC + "/#", SUBTOPIC_ID);
        matcher.addURI(authority, DataContract.PATH_SUBTOPIC + "/*", SUBTOPIC_WITH_TOPIC);

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
            case STUDY_GROUP_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.StudyGroupEntry.TABLE_NAME,
                        projection,
                        DataContract.StudyGroupEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case STUDY_GROUP:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.StudyGroupEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case DISCUSSION_WITH_GROUP:
                retCursor = getDiscussionByStudyGroup(uri, projection, sortOrder);
                break;
            case STUDY_GROUP_DISCUSSION_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.StudyGroupDiscussionEntry.TABLE_NAME,
                        projection,
                        DataContract.StudyGroupDiscussionEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case STUDY_GROUP_DISCUSSION:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.StudyGroupDiscussionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case STUDY_MATERIAL_WITH_COURSE:
                retCursor = getMaterialByCourse(uri, projection, sortOrder);
                break;
            case STUDY_MATERIAL_WITH_TOPIC:
                retCursor = getMaterialByTopic(uri, projection, sortOrder);
                break;
            case STUDY_MATERIAL_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.StudyMaterialEntry.TABLE_NAME,
                        projection,
                        DataContract.StudyMaterialEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case STUDY_MATERIAL:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.StudyMaterialEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case COURSE_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.CourseEntry.TABLE_NAME,
                        projection,
                        DataContract.CourseEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case COURSE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.CourseEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case COURSE_DISCUSSION_WITH_COURSE:
                retCursor = getDiscussionByCourse(uri, projection, sortOrder);
                break;
            case COURSE_DISCUSSION_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.CourseDiscussionEntry.TABLE_NAME,
                        projection,
                        DataContract.CourseDiscussionEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case COURSE_DISCUSSION:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.CourseDiscussionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case COMMENT_WITH_DISCUSSION:
                retCursor = getCommentByDiscussion(uri, projection, sortOrder);
                break;
            case DISCUSSION_COMMENT_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.DiscussionCommentEntry.TABLE_NAME,
                        projection,
                        DataContract.DiscussionCommentEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case DISCUSSION_COMMENT:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.DiscussionCommentEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case TOPIC_WITH_COURSE:
                retCursor = getTopicByCourse(uri, projection, sortOrder);
                break;
            case TOPIC_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.TopicEntry.TABLE_NAME,
                        projection,
                        DataContract.TopicEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case TOPIC:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.TopicEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case SUBTOPIC_WITH_TOPIC:
                retCursor = getSubByTopic(uri, projection, sortOrder);
                break;
            case SUBTOPIC_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.SubTopicEntry.TABLE_NAME,
                        projection,
                        DataContract.SubTopicEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case SUBTOPIC:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.SubTopicEntry.TABLE_NAME,
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
            case STUDY_GROUP:
                return DataContract.StudyGroupEntry.CONTENT_TYPE;
            case STUDY_GROUP_ID:
                return DataContract.StudyGroupEntry.CONTENT_ITEM_TYPE;
            case STUDY_GROUP_DISCUSSION:
                return DataContract.StudyGroupDiscussionEntry.CONTENT_TYPE;
            case STUDY_GROUP_DISCUSSION_ID:
                return DataContract.StudyGroupDiscussionEntry.CONTENT_ITEM_TYPE;
            case DISCUSSION_WITH_GROUP:
                return DataContract.StudyGroupDiscussionEntry.CONTENT_TYPE;
            case STUDY_MATERIAL:
                return DataContract.StudyMaterialEntry.CONTENT_TYPE;
            case STUDY_MATERIAL_ID:
                return DataContract.StudyMaterialEntry.CONTENT_ITEM_TYPE;
            case STUDY_MATERIAL_WITH_COURSE:
                return DataContract.StudyMaterialEntry.CONTENT_TYPE;
            case STUDY_MATERIAL_WITH_TOPIC:
                return DataContract.StudyMaterialEntry.CONTENT_TYPE;
            case COURSE:
                return DataContract.CourseEntry.CONTENT_TYPE;
            case COURSE_ID:
                return DataContract.CourseEntry.CONTENT_ITEM_TYPE;
            case COURSE_DISCUSSION:
                return DataContract.CourseDiscussionEntry.CONTENT_TYPE;
            case COURSE_DISCUSSION_ID:
                return DataContract.CourseDiscussionEntry.CONTENT_ITEM_TYPE;
            case COURSE_DISCUSSION_WITH_COURSE:
                return DataContract.CourseDiscussionEntry.CONTENT_TYPE;
            case DISCUSSION_COMMENT:
                return DataContract.DiscussionCommentEntry.CONTENT_TYPE;
            case DISCUSSION_COMMENT_ID:
                return DataContract.DiscussionCommentEntry.CONTENT_ITEM_TYPE;
            case COMMENT_WITH_DISCUSSION:
                return DataContract.DiscussionCommentEntry.CONTENT_TYPE;
            case TOPIC:
                return DataContract.TopicEntry.CONTENT_TYPE;
            case TOPIC_ID:
                return DataContract.TopicEntry.CONTENT_ITEM_TYPE;
            case TOPIC_WITH_COURSE:
                return DataContract.TopicEntry.CONTENT_TYPE;
            case SUBTOPIC:
                return DataContract.SubTopicEntry.CONTENT_TYPE;
            case SUBTOPIC_ID:
                return DataContract.SubTopicEntry.CONTENT_ITEM_TYPE;
            case SUBTOPIC_WITH_TOPIC:
                return DataContract.SubTopicEntry.CONTENT_TYPE;
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
            case STUDY_GROUP: {
                long _id = db.insert(DataContract.StudyGroupEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.StudyGroupEntry.buildStudyGroupUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case STUDY_GROUP_DISCUSSION: {
                long _id = db.insert(DataContract.StudyGroupDiscussionEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.StudyGroupDiscussionEntry.buildStudyGroupDiscussionUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case STUDY_MATERIAL: {
                long _id = db.insert(DataContract.StudyMaterialEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.StudyMaterialEntry.buildStudyMaterialUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case COURSE: {
                long _id = db.insert(DataContract.CourseEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.CourseEntry.buildCourseUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case COURSE_DISCUSSION: {
                long _id = db.insert(DataContract.CourseDiscussionEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.CourseDiscussionEntry.buildCourseDiscussionUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case DISCUSSION_COMMENT: {
                long _id = db.insert(DataContract.DiscussionCommentEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.DiscussionCommentEntry.buildDiscussionCommentUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TOPIC: {
                long _id = db.insert(DataContract.TopicEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.TopicEntry.buildTopicUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SUBTOPIC: {
                long _id = db.insert(DataContract.SubTopicEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.SubTopicEntry.buildSubTopicUri(_id);
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
            case STUDY_GROUP:
                rowsDeleted = db.delete(
                        DataContract.StudyGroupEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case STUDY_GROUP_DISCUSSION:
                rowsDeleted = db.delete(
                        DataContract.StudyGroupDiscussionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case STUDY_MATERIAL:
                rowsDeleted = db.delete(
                        DataContract.StudyMaterialEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COURSE:
                rowsDeleted = db.delete(
                        DataContract.CourseEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COURSE_DISCUSSION:
                rowsDeleted = db.delete(
                        DataContract.CourseDiscussionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case DISCUSSION_COMMENT:
                rowsDeleted = db.delete(
                        DataContract.DiscussionCommentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TOPIC:
                rowsDeleted = db.delete(
                        DataContract.TopicEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SUBTOPIC:
                rowsDeleted = db.delete(
                        DataContract.SubTopicEntry.TABLE_NAME, selection, selectionArgs);
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
            case STUDY_GROUP:
                rowsUpdated = db.update(DataContract.StudyGroupEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case STUDY_GROUP_DISCUSSION:
                rowsUpdated = db.update(DataContract.StudyGroupDiscussionEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case STUDY_MATERIAL:
                rowsUpdated = db.update(DataContract.StudyMaterialEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case COURSE:
                rowsUpdated = db.update(DataContract.CourseEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case COURSE_DISCUSSION:
                rowsUpdated = db.update(DataContract.CourseDiscussionEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case DISCUSSION_COMMENT:
                rowsUpdated = db.update(DataContract.DiscussionCommentEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TOPIC:
                rowsUpdated = db.update(DataContract.TopicEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case SUBTOPIC:
                rowsUpdated = db.update(DataContract.SubTopicEntry.TABLE_NAME, values, selection,
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
            case STUDY_GROUP: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.StudyGroupEntry.TABLE_NAME, null, value);
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
            case STUDY_GROUP_DISCUSSION: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.StudyGroupDiscussionEntry.TABLE_NAME, null, value);
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
            case STUDY_MATERIAL: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.StudyMaterialEntry.TABLE_NAME, null, value);
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
            case COURSE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.CourseEntry.TABLE_NAME, null, value);
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
            case COURSE_DISCUSSION: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.CourseDiscussionEntry.TABLE_NAME, null, value);
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
            case DISCUSSION_COMMENT: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.DiscussionCommentEntry.TABLE_NAME, null, value);
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
            case TOPIC: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.TopicEntry.TABLE_NAME, null, value);
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
            case SUBTOPIC: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.SubTopicEntry.TABLE_NAME, null, value);
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
