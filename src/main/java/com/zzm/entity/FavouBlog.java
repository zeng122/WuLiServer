package com.zzm.entity;

public class FavouBlog {
    private int id ;
    private int blogid;
    private int uid;

    public void setId(int id) {
        this.id = id;
    }

    public void setBlogid(int blogid) {
        this.blogid = blogid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public int getBlogid() {
        return blogid;
    }

    public int getUid() {
        return uid;
    }
}
