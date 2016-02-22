package com.techhaven.schooltweets.dataobjects;

/**
 * Created by Oluwayomi on 2/9/2016.
 */
public class NewsBlogObject {
    private int id;
    private String Title;
    private String PostText;
    private String AuthorName;
    private String DatePosted;
    private String UpVoteCount;
    private String DownVoteCount;
    private String ImagePost;
    private int CommentCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPostText() {
        return PostText;
    }

    public void setPostText(String postText) {
        PostText = postText;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public String getDatePosted() {
        return DatePosted;
    }

    public void setDatePosted(String datePosted) {
        DatePosted = datePosted;
    }

    public String getUpVoteCount() {
        return UpVoteCount;
    }

    public void setUpVoteCount(String upVoteCount) {
        UpVoteCount = upVoteCount;
    }

    public String getDownVoteCount() {
        return DownVoteCount;
    }

    public void setDownVoteCount(String downVoteCount) {
        DownVoteCount = downVoteCount;
    }

    public String getImagePost() {
        return ImagePost;
    }

    public void setImagePost(String imagePost) {
        ImagePost = imagePost;
    }

    public int getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(int commentCount) {
        CommentCount = commentCount;
    }
}
