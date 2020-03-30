package com.zzm.dao;

import com.zzm.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IBlogDao {
 public List<Blog> getBlogsByUid(int uid);

 public void addBlog(Blog blog);

 public List<Blog> getRefrushBlog(@Param("anonymous")int anonymous ,@Param("num")int num);

 public List<Blog> getRefrushMyBlog(@Param("uid") int uid , @Param("num")int num);

 public int getId(int id);

 public int getNumById(int id);

 public List<Blog> getMoreBlog(@Param("anonymous")int anonymous, @Param("start") int start,@Param("end") int end);

 public List<Blog> getMoreMyBlog(@Param("uid") int uid, @Param("start") int start,@Param("end") int end);

 public LikeBlog isLikeBlog(LikeBlog likeBlog);

 public FavouBlog isFavouBlog(FavouBlog favouBlog);

 public void addFavouNum(int id);

 public void addLikeNum(int id);

 public void addFavou(FavouBlog favouBlog);

 public void addLike(LikeBlog likeBlog);

 public void minusFavouNum(int id);

 public void minusLikeNum(int id);

 public void minusFavou(FavouBlog favouBlog);

 public void minusLike(LikeBlog likeBlog);

 public List<Comment> getCommentByBlogId(int blogid);

 public void addComment(Comment comment);

 public void addJubao(Jubao jubao);

 public List<Jubao> getAllJubao();

 public void deleteBlog(int id);

 public  void deleteJubao(int id);

 public  String getContendById(int id);

 public void addBlogPic(List<BlogPic> blogPics);

 public int getUidByBlogId(int blogid);
}
