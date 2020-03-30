package com.zzm.entity;

public class Blog {
    private  int id;
    private  int uid;
    private  int anonymous;
    private  String time;
    private  String contend;
    private int likenum;
    private int favounum;

    public void setLikenum(int likenum) {
        this.likenum = likenum;
    }

    public void setFavounum(int favounum) {
        this.favounum = favounum;
    }

    public int getLikenum() {
        return likenum;
    }

    public int getFavounum() {
        return favounum;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }

    public int getAnonymous() {
        return anonymous;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContend(String contend) {
        this.contend = contend;
    }

    public int getId() {
        return id;
    }

    public int getUid() {
        return uid;
    }

    public String getTime() {
        return time;
    }

    public String getContend() {
        return contend;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", uid=" + uid +
                ", anonymous=" + anonymous +
                ", time='" + time + '\'' +
                ", contend='" + contend + '\'' +
                ", likenum=" + likenum +
                ", favounum=" + favounum +
                '}';
    }
}
