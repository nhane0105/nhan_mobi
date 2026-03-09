package com.example.baitap4;

import com.google.gson.annotations.SerializedName;

/**
 * Model cho một post từ API https://jsonplaceholder.typicode.com/posts
 */
public class Post {

    private int userId;
    private int id;
    private String title;

    @SerializedName("body")
    private String body;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
