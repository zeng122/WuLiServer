package com.zzm.server.Impl;

import com.zzm.dao.IBlogDao;
import com.zzm.dao.IUserInfoDao;
import com.zzm.entity.*;
import com.zzm.facade.CommentFacade;
import com.zzm.server.IBlogServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Service
public class BlogServerImpl implements IBlogServer {
    @Autowired
    private IBlogDao iBlogDao;

    @Autowired
    private IUserInfoDao iUserInfoDao;

    @Override
    public List<Blog> getBlogsByUid(int uid) {

        List<Blog> blogs= iBlogDao.getBlogsByUid(uid);
        return blogs;
    }

    @Override
    public int addBlogByName(String name, String contend,Boolean anonymous) {
     UserInfo userInfo = iUserInfoDao.isExistByName(name);
     Blog blog = new Blog();
     blog.setUid(userInfo.getId());
     blog.setContend(contend);
     if(anonymous){
         blog.setAnonymous(1);
     }else{
         blog.setAnonymous(0);
     }
     iBlogDao.addBlog(blog);
     return blog.getId();
    }

    @Override
    public List<Blog> getRefrushBlog(int anonymous, int num) {
        List<Blog> blogs = null;
        blogs = iBlogDao.getRefrushBlog(anonymous,num);
        return  blogs;
    }

    @Override
    public List<Blog> getRefrushMyBlog(String name,int num) {
        int uid = iUserInfoDao.isExistByName(name).getId();
        List<Blog> blogs = null;
        blogs = iBlogDao.getRefrushMyBlog(uid, num);
        return blogs;
    }

    @Override
    public int getId(int id) {
        return  iBlogDao.getId(id);
    }

    @Override
    public int getNumById(int id) {
        return  iBlogDao.getNumById(id);
    }

    @Override
    public List<Blog> getMoreBlog(int id ,int anonymous) {
        int start = getNumById(id);
        List<Blog> blogs = null;
        blogs = iBlogDao.getMoreBlog(anonymous,start-1,start+4);
        return  blogs;
    }

    @Override
    public List<Blog> getMoreMyBlog(int id, String name) {
        int blogId = iUserInfoDao.isExistByName(name).getId();
        int start = getNumById(id);
        List<Blog> blogs = null;
        blogs = iBlogDao.getMoreMyBlog(blogId, start-1,start+4);
        return null;
    }

    @Override
    public boolean isLikeBlog(LikeBlog likeBlog) {
       LikeBlog likeBlog1 =  iBlogDao.isLikeBlog(likeBlog);
       if(likeBlog1 == null){
           return false;
       }else{
           return  true;
       }
    }

    @Override
    public boolean isFavouBlog(FavouBlog favouBlog) {
        FavouBlog favouBlog1 = iBlogDao.isFavouBlog(favouBlog);
        if(favouBlog1 == null){
            return  false;
        }else {
            return true;
        }
    }

    @Override
    public void doLikeBlog(LikeBlog likeBlog) {
        //增加数量
        iBlogDao.addLikeNum(likeBlog.getBlogid());
        //添加到likeblog表
        iBlogDao.addLike(likeBlog);
    }

    @Override
    public void noLikeBlog(LikeBlog likeBlog) {
        //减少数量
        iBlogDao.minusLikeNum(likeBlog.getBlogid());
        //移除
        iBlogDao.minusLike(likeBlog);
    }

    @Override
    public void doFavouBlog(FavouBlog favouBlog) {
        iBlogDao.addFavouNum(favouBlog.getBlogid());
        iBlogDao.addFavou(favouBlog);
    }

    @Override
    public void noFavouBlog(FavouBlog favouBlog) {
        iBlogDao.minusFavouNum(favouBlog.getBlogid());
        iBlogDao.minusFavou(favouBlog);
    }

    @Override
    public List<CommentFacade> getBlogFacadeById(int blogid) {
        List<Comment> comments = iBlogDao.getCommentByBlogId(blogid);
        ListIterator<Comment> listIterator = comments.listIterator();
        List<CommentFacade> commentFacades = new ArrayList<CommentFacade>();
        while (listIterator.hasNext()){
          CommentFacade commentFacade = new CommentFacade();
          Comment comment = listIterator.next();
          commentFacade.setContend(comment.getContend());
          commentFacade.setName(iUserInfoDao.getNameById(comment.getUid()).getName());
          commentFacades.add(commentFacade);
        }
        return commentFacades;
    }

    @Override
    public void addCommentByCommnetFacade(CommentFacade commentFacade ,int blogId) {
        Comment comment = new Comment();
        comment.setBlogid(blogId);
        comment.setContend(commentFacade.getContend());
        comment.setUid(iUserInfoDao.isExistByName(commentFacade.getName()).getId());
        iBlogDao.addComment(comment);
    }

    @Override
    public void addJubao(String name, int blogId, String reason) {
        int uid = iUserInfoDao.isExistByName(name).getId();
        Jubao jubao = new Jubao();
        jubao.setBlogid(blogId);
        jubao.setUid(uid);
        jubao.setReason(reason);
        iBlogDao.addJubao(jubao);
    }

    @Override
    public List<Jubao> getAllJubao() {
        return  iBlogDao.getAllJubao();
    }

    @Override
    public String getContendById(int id) {
        return iBlogDao.getContendById(id);
    }

    @Override
    public void deleteBlog(int id) {
        iBlogDao.deleteBlog(id);
    }

    @Override
    public void deleteJubao(int id) {
       iBlogDao.deleteJubao(id);
    }

    @Override
    public void addBlogPic(List<BlogPic> blogPics) {
        iBlogDao.addBlogPic(blogPics);
    }

    @Override
    public int getUidByBlogId(int blogid) {
        return iBlogDao.getUidByBlogId(blogid);
    }


}
