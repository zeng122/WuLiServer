package com.zzm.server.Impl;

import com.zzm.dao.IUserInfoDao;
import com.zzm.entity.Blog;
import com.zzm.entity.UserInfo;
import com.zzm.server.IUserInfoServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoServerImpl implements IUserInfoServer {
    @Autowired
    private IUserInfoDao iUserInfoDao;

    @Override
    public UserInfo login(UserInfo userInfo) {
        UserInfo userInfoRes = iUserInfoDao.login(userInfo);
        return  userInfoRes;
    }

    @Override
    public boolean addUser(UserInfo userInfo) {
        if (isExistByName(userInfo.getName()) == null) {
            iUserInfoDao.addUser(userInfo);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UserInfo isExistByName(String name) {
        return iUserInfoDao.isExistByName(name);
    }

    @Override
    public void changeSignatureByName(UserInfo userInfo) {
        iUserInfoDao.changeSignatureByName(userInfo);
    }

    @Override
    public void changeTelephoneByName(UserInfo userInfo) {
        iUserInfoDao.changeTelephoneByName(userInfo);
    }

    @Override
    public void changeSexByName(UserInfo userInfo) {
        iUserInfoDao.changeSexByName(userInfo);
    }

    @Override
    public String getNameById(int id) {
        UserInfo userInfo  = iUserInfoDao.getNameById(id);
        return  userInfo.getName();
    }

    @Override
    public List<Blog> getAllMyLike(String name) {
        int uid = isExistByName(name).getId();
        List<Blog> blogs = null;
        blogs = iUserInfoDao.getAllMyLike(uid);
        return  blogs;
    }
}
