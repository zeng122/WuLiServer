package com.zzm.entity;

public class Comment {
    private int id;
    private int blogid;
    private int uid;
    private String contend;
    private String time;

    public void setId(int id) {
        this.id = id;
    }

    public void setBlogid(int blogid) {
        this.blogid = blogid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setContend(String contend) {
        this.contend = contend;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getContend() {
        return contend;
    }

    public String getTime() {
        return time;
    }
}
