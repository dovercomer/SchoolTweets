package com.techhaven.schooltweets.dataobjects;

/**
 * Created by Oluwayomi on 2/13/2016.
 */
public class TagsObject {
    private int id;
    private int postId;
    private String tagName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
