package com.techhaven.schooltweets.dataaccesslayer.contracts;

import android.provider.BaseColumns;

/**
 * Created by Oluwayomi on 2/9/2016.
 */
public class DataContract {
    public class BlogEntry {
        public static final String TABLE_NAME = "NewsBlogPosts";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CONTENT = "PostContent";
        public static final String COLUMN_IMAGE_LINK = "ImageLink";
        public static final String COLUMN_AUTHOR_NAME = "AuthorName";
        public static final String COLUMN_DATE_POSTED = "DatePosted";
        public static final String COLUMN_UP_VOTE_COUNT = "UpVoteCount";
        public static final String COLUMN_DOWN_VOTE_COUNT = "DownVoteCount";
        public static final String COLUMN_COMMENT_COUNT = "CommentCount";
    }

    public class TagEntry {
        public static final String TABLE_NAME = "tags";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_POST_ID = "post_id";
        public static final String COLUMN_TAG_NAME = "tag_name";
    }
    public class NewsForumTagEntry{
        public static final String TABLE_NAME="news_forum_tag";
        public static final String COLUMN_TAG_KEY="tag_id";
        public static final String COLUMN_BLOG_KEY="blog_id";
        public static final String COLUMN_FORUM_KEY="ForumId";
    }

    public class ForumEntry {
        public static final String TABLE_NAME = "Forum";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_ROOM_NAME = "RoomName";
        public static final String COLUMN_DATE = "Date";
    }

    public class ForumRoomPostsEntry implements BaseColumns {
        public static final String TABLE_NAME = "RoomPosts";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_POST_CONTENT = "PostContent";
        public static final String COLUMN_IMAGE_LINK = "ImageLink";
        public static final String COLUMN_AUTHOR_NAME = "AuthorName";
        public static final String COLUMN_DATE_POSTED = "DatePosted";
        public static final String COLUMN_UP_VOTE_COUNT = "UpVoteCount";
        public static final String COLUMN_DOWN_VOTE_COUNT = "DownVoteCount";
        public static final String COLUMN_COMMENT_COUNT = "CommentCount";
    }

    public class ContentReceptionEntry implements BaseColumns {
        public static final String TABLE_NAME = "ContentReception";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_POST_CONTENT = "PostContent";
        public static final String COLUMN_IMAGE_LINK = "ImageLink";
        public static final String COLUMN_DATE_TIME = "Datetime";
    }

    public class UserProfileEntry {
        public static final String TABLE_NAME = "UserProfile";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_USER_NAME = "Username";
        public static final String COLUMN_EMAIL = "Email";
        public static final String COLUMN_PASSWORD = "Password";
        public static final String COLUMN_FIRST_NAME = "FirstName";
        public static final String COLUMN_LAST_NAME = "LastName";
        public static final String COLUMN_API_KEY = "ApiSecretKey";
        public static final String COLUMN_IMAGE_LINK = "ImageLink";

    }
}
