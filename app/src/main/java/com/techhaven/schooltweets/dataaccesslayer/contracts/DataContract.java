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

    // Format used for storing dates in the database.  ALso used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyyMMdd";

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
}
