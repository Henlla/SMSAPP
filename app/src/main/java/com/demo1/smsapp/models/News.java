package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

public class News {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("sub_title")
    private String sub_title;
    @SerializedName("content")
    private String content;
    @SerializedName("post_date")
    private String postdate;
    @SerializedName("thumbnailUrl")
    private String thumbnailUrl;
    @SerializedName("thumbnailPath")
    private String thumbnailPath;
    @SerializedName("isActive")
    private boolean isActive;

    public News() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }
}
