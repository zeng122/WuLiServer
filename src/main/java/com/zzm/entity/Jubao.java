package com.zzm.entity;

public class Jubao {
    int id;
    int uid;
    int blogid;
    String reason;

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

    @Override
    public String toString() {
        return "Jubao{" +
                "id=" + id +
                ", uid=" + uid +
                ", blogid=" + blogid +
                ", reason='" + reason + '\'' +
                '}';
    }
}
