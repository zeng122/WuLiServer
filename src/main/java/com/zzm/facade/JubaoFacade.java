package com.zzm.facade;

public class JubaoFacade {
    int id;
    int uid;
    int blogid;
    String reason;
    String name;
    String blogContend;

    public void setBlogContend(String blogContend) {
        this.blogContend = blogContend;
    }

    public String getBlogContend() {
        return blogContend;
    }

    public int getId() {
        return id;
    }

    public int getUid() {
        return uid;
    }

    public int getBlogid() {
        return blogid;
    }

    public String getReason() {
        return reason;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setBlogid(int blogid) {
        this.blogid = blogid;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setName(String name) {
        this.name = name;
    }
}
