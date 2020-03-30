package com.zzm.entity;

public class BlogPic {
    private int id;
    private int blogid;
    private String picture;

    public int getId() {
        return id;
    }

    public int getBlogid() {
        return blogid;
    }

    public String getPicture() {
        return picture;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBlogid(int blogid) {
        this.blogid = blogid;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
