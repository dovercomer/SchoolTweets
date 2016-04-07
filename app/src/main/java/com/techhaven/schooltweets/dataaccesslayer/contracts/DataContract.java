package com.techhaven.schooltweets.dataaccesslayer.contracts;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Oluwayomi on 2/9/2016.
 */
public class DataContract {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.techhaven.schooltweets";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_NEWS = "news";
    public static final String PATH_TAG = "tag";
    public static final String PATH_NEWS_FORUM_TAG = "news_forum_tag";
    public static final String PATH_FORUM_ROOM = "forum_room";
    public static final String PATH_FORUM_ROOM_POST = "room_post";
    public static final String PATH_CONTENT_RECEPTION = "content_reception";
    public static final String PATH_USER_PROFILE = "user_profile";
    public static final String PATH_STUDY_GROUP = "study_group";
    public static final String PATH_STUDY_GROUP_DISCUSSION = "study_group_discussion";
    public static final String PATH_STUDY_MATERIAL = "study_material";
    public static final String PATH_COURSE = "course";
    public static final String PATH_COURSE_DISCUSSION = "course_discussion";
    public static final String PATH_DISCUSSION_COMMENT = "discussion_comment";
    public static final String PATH_TOPIC = "topic";
    public static final String PATH_SUBTOPIC = "subtopic";

    // Format used for storing dates in the database.  ALso used for converting those strings
    // back into date objects for comparison/processing.
    //public static final String DATE_FORMAT = "yyyyMMdd";
    public static final String DATE_FORMAT = "yyyyMMddHHmmss";

    /**
     * Converts Date class to a string representation, used for easy comparison and database lookup.
     */
    public static String getDbDateString(Date date) {
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * Converts a dateText to a long Unix time representation
     *
     * @param dateText the input date string
     * @return the Date object
     */
    public static Date getDateFromDb(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final class NewsEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;

        public static final String TABLE_NAME = "news";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CONTENT = "post_content";
        public static final String COLUMN_IMAGE_LINK = "image_link";
        public static final String COLUMN_AUTHOR_NAME = "author_name";
        public static final String COLUMN_DATE_POSTED = "date_posted";
        public static final String COLUMN_UP_VOTE_COUNT = "up_vote_count";
        public static final String COLUMN_DOWN_VOTE_COUNT = "down_vote_count";
        public static final String COLUMN_COMMENT_COUNT = "comment_count";

        public static Uri buildNewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildNewsTag(String tagSetting) {
            return CONTENT_URI.buildUpon().appendPath(tagSetting).build();
        }

        public static Uri buildNewsWithStartDate(String startDate) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_DATE_POSTED, startDate).build();
        }

        public static String getTagFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getDateFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getStartDateFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_DATE_POSTED);
        }
    }

    public static final class TagEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TAG).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_TAG;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_TAG;

        public static final String TABLE_NAME = "tag";
        public static final String COLUMN_TAG_NAME = "tag_name";

        public static Uri buildTagUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class NewsForumTagEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS_FORUM_TAG).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_NEWS_FORUM_TAG;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_NEWS_FORUM_TAG;

        public static final String TABLE_NAME = "news_forum_tag";
        public static final String COLUMN_TAG_KEY = "tag_id";
        public static final String COLUMN_NEWS_KEY = "blog_id";
        public static final String COLUMN_FORUM_POST_KEY = "forum_id";

        public static Uri buildNewsForumTagUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ForumRoomEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FORUM_ROOM).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_FORUM_ROOM;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_FORUM_ROOM;

        public static final String TABLE_NAME = "forum_room";
        public static final String COLUMN_ROOM_NAME = "room_name";

        public static Uri buildForumRoomUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ForumRoomPostEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FORUM_ROOM_POST).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_FORUM_ROOM_POST;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_FORUM_ROOM_POST;

        public static final String TABLE_NAME = "room_post";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_POST_CONTENT = "PostContent";
        public static final String COLUMN_IMAGE_LINK = "ImageLink";
        public static final String COLUMN_AUTHOR_NAME = "AuthorName";
        public static final String COLUMN_DATE_POSTED = "DatePosted";
        public static final String COLUMN_UP_VOTE_COUNT = "UpVoteCount";
        public static final String COLUMN_DOWN_VOTE_COUNT = "DownVoteCount";
        public static final String COLUMN_COMMENT_COUNT = "CommentCount";
        public static final String COLUMN_FORUM_KEY = "forum_id";

        public static Uri buildForumPostUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildForumPostRoom(String room) {
            return CONTENT_URI.buildUpon().appendPath(room).build();
        }

        public static Uri buildForumPostRoomWithStartDate(
                String room, String startDate) {
            return CONTENT_URI.buildUpon().appendPath(room)
                    .appendQueryParameter(COLUMN_DATE_POSTED, startDate).build();
        }

        public static String getRoomFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getDateFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getTagFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getStartDateFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_DATE_POSTED);
        }
    }

    public static final class ContentReceptionEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTENT_RECEPTION).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CONTENT_RECEPTION;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CONTENT_RECEPTION;
        public static final String TABLE_NAME = "content_reception";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_POST_CONTENT = "PostContent";
        public static final String COLUMN_IMAGE_LINK = "ImageLink";
        public static final String COLUMN_DATE_TIME = "Datetime";

        public static Uri buildContentReceptionUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class UserProfileEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_PROFILE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_USER_PROFILE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_USER_PROFILE;

        public static final String TABLE_NAME = "user_profile";
        public static final String COLUMN_USER_NAME = "Username";
        public static final String COLUMN_EMAIL = "Email";
        public static final String COLUMN_PASSWORD = "Password";
        public static final String COLUMN_FIRST_NAME = "FirstName";
        public static final String COLUMN_LAST_NAME = "LastName";
        public static final String COLUMN_API_KEY = "ApiSecretKey";
        public static final String COLUMN_IMAGE_LINK = "ImageLink";

        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class InstitutionEntry implements BaseColumns {
        public static final String TABLE_NAME = "institution";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NAME_ABBR = "name_abbreviation";
        public static final String COLUMN_INSTITUTION_TYPE = "institution_type";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_BANNER = "banner";


    }


    public static final class StudyGroupEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STUDY_GROUP).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_STUDY_GROUP;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_STUDY_GROUP;

        public static final String TABLE_NAME = "studygroup";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE_PATH = "image_path";
        public static final String COLUMN_JOINED = "joined";

        public static Uri buildStudyGroupUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class StudyGroupDiscussionEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STUDY_GROUP_DISCUSSION).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_STUDY_GROUP_DISCUSSION;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_STUDY_GROUP_DISCUSSION;
        public static final String TABLE_NAME = "study_group_discussion";
        public static final String COLUMN_STUDY_GROUP_ID = "study_group_id";
        public static final String COLUMN_SENDER_ID = "sender_id";
        public static final String COLUMN_SENDER_NAME = "sender_name";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_FILE_OFFLINE = "file_offline";
        public static final String COLUMN_FILE_PATH = "file_path";
        public static final String COLUMN_FILE_TYPE = "file_type";
        public static final String COLUMN_READ = "read";
        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_LIKED = "liked";

        public static Uri buildStudyGroupDiscussionUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getGroupFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

    }


    public static final class CourseEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COURSE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_COURSE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_COURSE;
        public static final String TABLE_NAME = "course";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_INSTITUTION_ID = "institution_id";
        public static final String COLUMN_INSTITUTION_NAME = "institution";
        public static final String COLUMN_DEPARTMENT_ID = "department_id";
        public static final String COLUMN_DEPARTMENT_NAME = "department";
        public static final String COLUMN_LEVEL_ID = "level_id";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_SESSION_ID = "session_id";
        public static final String COLUMN_SESSION_NAME = "session_name";
        public static final String COLUMN_SEMESTER_ID = "semester_id";
        public static final String COLUMN_SEMESTER_NAME = "semester_name";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_END_DATE = "end_date";
        public static final String COLUMN_SUMMARY = "summary";

        public static Uri buildCourseUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TopicEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOPIC).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_TOPIC;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_TOPIC;
        public static final String TABLE_NAME = "topic";
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_TOPIC = "topic";
        public static final String COLUMN_SUMMARY = "summary";

        public static Uri buildTopicUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getCourseFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
    }

    public static final class SubTopicEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUBTOPIC).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_SUBTOPIC;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_SUBTOPIC;
        public static final String TABLE_NAME = "subtopic";
        public static final String COLUMN_TOPIC_ID = "topic_id";
        public static final String COLUMN_SUBTOPIC = "subtopic";
        public static final String COLUMN_SUMMARY = "summary";

        public static Uri buildSubTopicUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getTopicFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

    }

    public static final class CourseDiscussionEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COURSE_DISCUSSION).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_COURSE_DISCUSSION;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_COURSE_DISCUSSION;
        public static final String TABLE_NAME = "discussion";
        public static final String COLUMN_COMMENT_COUNT = "comment_count";
        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_LIKED = "liked";
        public static final String COLUMN_SENDER_ID = "sender_id";
        public static final String COLUMN_SENDER_NAME = "sender_name";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_FILE_OFFLINE = "file_offline";
        public static final String COLUMN_FILE_PATH = "file_path";
        public static final String COLUMN_FILE_TYPE = "file_type";
        public static final String COLUMN_READ = "read";
        public static final String COLUMN_COURSE_ID = "course_id";

        public static Uri buildCourseDiscussionUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getCourseFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
    }

    public static final class DiscussionCommentEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DISCUSSION_COMMENT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_DISCUSSION_COMMENT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_DISCUSSION_COMMENT;
        public static final String TABLE_NAME = "discussioncomment";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_SENDER_ID = "sender_id";
        public static final String COLUMN_SENDER_NAME = "sender_name";
        public static final String COLUMN_READ = "read";
        public static final String COLUMN_DISCUSSION_ID = "discussion_id";

        public static Uri buildDiscussionCommentUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getDiscussionFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

    }

    public static final class StudyMaterialEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STUDY_MATERIAL).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_STUDY_MATERIAL;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_STUDY_MATERIAL;
        public static final String TABLE_NAME = "study_material";
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_TOPIC_ID = "topic_id";
        public static final String COLUMN_MATERIAL_TYPE_ID = "message_type_id";
        public static final String COLUMN_MATERIAL_TYPE = "message_type";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_FILE_PATH = "file_path";
        public static final String COLUMN_FILE_OFFLINE = "file_offline";

        public static Uri buildStudyMaterialUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getCourseFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getTopicFromUri(Uri uri) {
            return uri.getPathSegments().get(4);
        }


    }

}
