package com.zzm.server;

import com.zzm.entity.*;
import com.zzm.facade.BlogFacade;
import com.zzm.facade.CommentFacade;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IBlogServer {
    public List<Blog> getBlogsByUid(int uid);

    public int addBlogByName(String name,String contend,Boolean anonymous);

    public List<Blog> getRefrushBlog(int anonymous, int num);

    public List<Blog> getRefrushMyBlog(String name , int num);

    public int getId(int id);

    public int getNumById(int id);

    public List<Blog> getMoreBlog(int id ,int anonymous);

    public List<Blog> getMoreMyBlog(int id ,String name);

    public  boolean isLikeBlog(LikeBlog likeBlog);

    public  boolean isFavouBlog(FavouBlog favouBlog);

    public  void doLikeBlog(LikeBlog likeBlog);

    public  void noLikeBlog(LikeBlog likeBlog);

    public void doFavouBlog(FavouBlog favouBlog);

    public void noFavouBlog(FavouBlog favouBlog);

    public List<CommentFacade> getBlogFacadeById(int blogid);

    public void addCommentByCommnetFacade(CommentFacade commentFacade,int blogId);

    public void addJubao(String name,int blogId,String reason);

    public List<Jubao> getAllJubao();

    public String getContendById(int id);

    public void deleteBlog(int id);

    public  void deleteJubao(int id);

    public void addBlogPic(List<BlogPic> blogPics);

    public int getUidByBlogId(int blogid);
}
