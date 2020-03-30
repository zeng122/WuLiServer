package com.zzm.controller;

import com.zzm.entity.Blog;
import com.zzm.entity.FavouBlog;
import com.zzm.entity.LikeBlog;
import com.zzm.entity.UserInfo;
import com.zzm.facade.BlogFacade;
import com.zzm.facade.CommentFacade;
import com.zzm.server.Impl.BlogServerImpl;
import com.zzm.server.Impl.UserInfoServerImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


@Controller
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private UserInfoServerImpl userInfoServer;

    @Autowired
    private BlogServerImpl blogServer;

    @RequestMapping("/login.do")
    public void  login(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }

            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);
            UserInfo userInfo = new UserInfo();
            userInfo.setName(jsonObjectIn.getString("name"));
            userInfo.setPassword(jsonObjectIn.getString("password"));

            //返回
            JSONObject jsonObjectOut = new JSONObject();
            response.setContentType("application/json");
            ServletOutputStream out = null;
            UserInfo userInfoRes = userInfoServer.login(userInfo);
            if(userInfoRes==null){
                jsonObjectOut.put("state","false");
            }else{
                System.out.println(userInfoRes.getName());
                jsonObjectOut.put("name",userInfoRes.getName());
                jsonObjectOut.put("state","true");
            }
            out = response.getOutputStream();
            out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/adminLogin.do" ,method = RequestMethod.POST)
    public String  adminLogin(HttpServletRequest request, HttpServletResponse response){
        String name = request.getParameter("user");
        String password = request.getParameter("password");
        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        System.out.println(name+"--------"+password);
        userInfo.setPassword(password);
        JSONObject jsonObject = new JSONObject();
        UserInfo userInfoRes = userInfoServer.login(userInfo);
        if(userInfoRes !=null && userInfoRes.getRole().equals("admin")){
             jsonObject.put("state","true");
        }else{
             jsonObject.put("state","false");
        }
        return  jsonObject.toString();

    }

    @RequestMapping("/register.do")
    public void  register(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);
            UserInfo userInfo = new UserInfo();
            userInfo.setName(jsonObjectIn.getString("name"));
            userInfo.setPassword(jsonObjectIn.getString("password"));

            //返回
            JSONObject jsonObjectOut = new JSONObject();
            response.setContentType("application/json");
            ServletOutputStream out = null;

            if(userInfoServer.addUser(userInfo)){
                jsonObjectOut.put("state","true");
            }else{
                jsonObjectOut.put("state","false");
            };
            out = response.getOutputStream();
            out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/getUserByName.do")
    public void  getUserByName(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);
            String name = (jsonObjectIn.getString("name"));
            //返回
            JSONObject jsonObjectOut = new JSONObject();
            response.setContentType("application/json");
            ServletOutputStream out = null;
            UserInfo userInfo = userInfoServer.isExistByName(name);
            if(userInfo == null){
                jsonObjectOut.put("state","false");
            }else{
                jsonObjectOut.put("state","true");
                jsonObjectOut.put("name",userInfo.getName());
                jsonObjectOut.put("signature",userInfo.getSignature());
                jsonObjectOut.put("sex",userInfo.getSex());
                jsonObjectOut.put("telephone",userInfo.getTelephone());
            };
            out = response.getOutputStream();
            out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/changeSignatureByName.do")
    public void  changeSignatureByName(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);
            String name = jsonObjectIn.getString("name");
            String signature = jsonObjectIn.getString("signature");
            //返回
            JSONObject jsonObjectOut = new JSONObject();
            response.setContentType("application/json");
            ServletOutputStream out = null;
            UserInfo userInfo = new UserInfo();
            userInfo.setName(name);
            userInfo.setSignature(signature);
            userInfoServer.changeSignatureByName(userInfo);
            jsonObjectOut.put("state","true");
            out = response.getOutputStream();
            out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/changeTelephoneByName.do")
    public void  changeTelephoneByName(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);
            String name = jsonObjectIn.getString("name");
            String telephone = jsonObjectIn.getString("telephone");
            //返回
            JSONObject jsonObjectOut = new JSONObject();
            response.setContentType("application/json");
            ServletOutputStream out = null;
            UserInfo userInfo = new UserInfo();
            userInfo.setName(name);
            userInfo.setTelephone(telephone);
            userInfoServer.changeTelephoneByName(userInfo);
            jsonObjectOut.put("state","true");
            out = response.getOutputStream();
            out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/changeSexByName.do")
    public void  changeSexByName(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);
            String name = jsonObjectIn.getString("name");
            String sex = jsonObjectIn.getString("sex");
            //返回
            JSONObject jsonObjectOut = new JSONObject();
            response.setContentType("application/json");
            ServletOutputStream out = null;
            UserInfo userInfo = new UserInfo();
            userInfo.setName(name);
            userInfo.setSex(sex);
            userInfoServer.changeSexByName(userInfo);
            jsonObjectOut.put("state","true");
            out = response.getOutputStream();
            out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/myLikeBlog.do")
    public void  myLikeBlog(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);
            String name = jsonObjectIn.getString("name");


            //返回
            response.setContentType("application/json");
            ServletOutputStream out = null;
            out = response.getOutputStream();

            List<Blog> blogs = userInfoServer.getAllMyLike(name);
            if(blogs == null){
                JSONObject jsonObjectOut = new JSONObject();
                jsonObjectOut.put("state","false");
                out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            }else{
                ListIterator<Blog> listIterator = blogs.listIterator();
                List<BlogFacade> blogFacades = new ArrayList<BlogFacade>();
                while (listIterator.hasNext()){
                    Blog blog = listIterator.next();
                    System.out.println(blog.toString());
                    BlogFacade blogFacade = new BlogFacade();

                    //获取喜欢、点赞状态
                    LikeBlog likeBlog = new LikeBlog();
                    int uid = userInfoServer.isExistByName(name).getId();
                    likeBlog.setUid(uid);
                    likeBlog.setBlogid(blog.getId());
                    blogFacade.setLikeState(blogServer.isLikeBlog(likeBlog));
                    FavouBlog favouBlog = new FavouBlog();
                    favouBlog.setUid(uid);
                    favouBlog.setBlogid(blog.getId());
                    blogFacade.setFavouState(blogServer.isFavouBlog(favouBlog));

                    //获取评论
                    List<CommentFacade> commentFacades = blogServer.getBlogFacadeById(blog.getId());
                    blogFacade.setCommentFacades(commentFacades);

                    //获取博客是否匿名
                    if(blog.getAnonymous() == 1){
                        blogFacade.setAnonymous(true);
                    }else{
                        blogFacade.setAnonymous(false);
                    }

                    //获取其他信息
                    blogFacade.setContend(blog.getContend());
                    blogFacade.setName(userInfoServer.getNameById(blog.getUid()));
                    blogFacade.setTime(blog.getTime().substring(0,19));
                    blogFacade.setId(blog.getId());
                    blogFacade.setFavounum(blog.getFavounum());
                    blogFacade.setLikenum(blog.getLikenum());
                    blogFacades.add(blogFacade);
                }

                JSONArray jsonArray =JSONArray.fromObject(blogFacades);
                out.write(jsonArray.toString().getBytes(Charset.forName("utf-8")));
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
