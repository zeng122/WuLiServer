package com.zzm.server;

import com.zzm.entity.Blog;
import com.zzm.entity.UserInfo;

import java.util.List;

public interface IUserInfoServer {
    public UserInfo login(UserInfo userInfo);

    public  boolean addUser(UserInfo userInfo);

    public UserInfo isExistByName(String name);

    public void changeSignatureByName(UserInfo userInfo);

    public void changeTelephoneByName(UserInfo userInfo);

    public void changeSexByName(UserInfo userInfo);

    public String getNameById(int id);

    public List<Blog> getAllMyLike(String name);
}
