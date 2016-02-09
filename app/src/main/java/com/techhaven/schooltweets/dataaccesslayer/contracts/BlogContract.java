package com.techhaven.schooltweets.dataaccesslayer.contracts;

/**
 * Created by Oluwayomi on 2/9/2016.
 */
public class BlogContract {
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
        public static final String TABLE_NAME = "NewsBlogPostsTags";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_POST_ID = "PostId";
        public static final String COLUMN_TAG_NAME = "TagName";
    }

    public class ForumEntry {
        public static final String TABLE_NAME = "Forum";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_ROOM_NAME = "RoomName";
        public static final String COLUMN_DATE = "Date";
    }

    public class ForumRoomPostsEntry {
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

    public class ContentReceptionEntry {
        public static final String TABLE_NAME = "ContentReception";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_POST_CONTENT = "PostContent";
        public static final String COLUMN_IMAGE_LINK = "ImageLink";
        public static final String COLUMN_DATE_TIME = "Datetime";
    }
    public class UserProfileEntry{
        public static final String TABLE_NAME = "UserProfile";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_USER_NAME = "Username";
        public static final String COLUMN_EMAIL= "Email";
        public static final String COLUMN_PASSWORD = "Password";
        public static final String COLUMN_FIRSTNAME = "FirstName";
        public static final String COLUMN_LASTNAME = "LastName";
        public static final String COLUMN_API = "ApiSecretKey";
        public static final String COLUMN_IMAGE_LINK = "ImageLink";

    }
}
