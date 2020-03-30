package com.zzm.dao;

import com.zzm.entity.Blog;
import com.zzm.entity.UserInfo;

import java.util.List;

public interface IUserInfoDao {

    public UserInfo login(UserInfo userInfo);

    public  void addUser(UserInfo userInfo);

    public UserInfo isExistByName(String name);

    public void changeSignatureByName(UserInfo userInfo);

    public void changeTelephoneByName(UserInfo userInfo);

    public void changeSexByName(UserInfo userInfo);

    public UserInfo getNameById(int id);

    public List<Blog> getAllMyLike(int uid);
}
