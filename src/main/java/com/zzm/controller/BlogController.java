package com.zzm.controller;


import com.zzm.entity.*;
import com.zzm.facade.BlogFacade;
import com.zzm.facade.CommentFacade;
import com.zzm.facade.JubaoFacade;
import com.zzm.server.Impl.BlogServerImpl;
import com.zzm.server.Impl.UserInfoServerImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Controller
@RequestMapping("/blog")
public class BlogController {
    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB


    @Autowired
    private BlogServerImpl blogServer;

    @Autowired
    private UserInfoServerImpl userInfoServer;

    @RequestMapping("/getId.do")
    public void  getId(HttpServletResponse response){
        response.setContentType("application/json");
        ServletOutputStream out = null;
        int id = blogServer.getId(1);
        System.out.println(id);
    }

    @RequestMapping("/getTime.do")
    public void  getTime(HttpServletResponse response){
        response.setContentType("application/json");
        ServletOutputStream out = null;
            List<Blog> blogs = blogServer.getBlogsByUid(1);
            ListIterator<Blog> listIterator = blogs.listIterator();
            while (listIterator.hasNext()){
                Blog blog = listIterator.next();
                System.out.println(blog.getTime().substring(0,19));
                System.out.println(blog.getContend());
            }

    }

    @RequestMapping("/getFive.do")
    public void  getFive(HttpServletResponse response){
        response.setContentType("application/json");
        ServletOutputStream out = null;
        List<Blog> blogs = blogServer.getRefrushBlog(0,5);
        ListIterator<Blog> listIterator = blogs.listIterator();
        while (listIterator.hasNext()){
            Blog blog = listIterator.next();
            System.out.println(blog.getTime());
            System.out.println(blog.getContend());
        }

    }

    @RequestMapping("/addBlogByName.do")
    public void addBlogByName(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);

            String name = jsonObjectIn.getString("name");
            String contend = jsonObjectIn.getString("contend");
            Boolean anonymous = jsonObjectIn.getBoolean("anonymous");
            int blogid = blogServer.addBlogByName(name,contend,anonymous);

            //返回
            JSONObject jsonObjectOut = new JSONObject();
            response.setContentType("application/json");
            ServletOutputStream out = null;
            jsonObjectOut.put("state","true");
            jsonObjectOut.put("blogId",blogid);
            out = response.getOutputStream();
            out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/refrushBlog.do")
    public void refrushBlog(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);
            Boolean isLogin  = jsonObjectIn.getBoolean("isLogin");
            String name = null;
            if(isLogin){
                name = jsonObjectIn.getString("name");
            }

            Boolean anonymous = jsonObjectIn.getBoolean("anonymous");

            //返回
            response.setContentType("application/json");
            ServletOutputStream out = null;
            out = response.getOutputStream();
            List<Blog> blogs = null;
            if(anonymous){
               blogs = blogServer.getRefrushBlog(1,5);
            }else{
                blogs = blogServer.getRefrushBlog(0,5);
            }

            if(blogs == null){
                JSONObject jsonObjectOut = new JSONObject();
                jsonObjectOut.put("state","false");
                out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            }else{
                ListIterator<Blog> listIterator = blogs.listIterator();
                List<BlogFacade> blogFacades = new ArrayList<BlogFacade>();
                while (listIterator.hasNext()){
                    Blog blog = listIterator.next();
                    BlogFacade blogFacade = new BlogFacade();

                    //获取喜欢、点赞状态
                    if(isLogin){
                        LikeBlog likeBlog = new LikeBlog();
                        int uid = userInfoServer.isExistByName(name).getId();
                        likeBlog.setUid(uid);
                        likeBlog.setBlogid(blog.getId());
                        blogFacade.setLikeState(blogServer.isLikeBlog(likeBlog));
                        FavouBlog favouBlog = new FavouBlog();
                        favouBlog.setUid(uid);
                        favouBlog.setBlogid(blog.getId());
                        blogFacade.setFavouState(blogServer.isFavouBlog(favouBlog));
                    }



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


    @RequestMapping("/loadMoreBlog.do")
    public void loadMoreBlog(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);

            Boolean anonymous = jsonObjectIn.getBoolean("anonymous");
            int id  = jsonObjectIn.getInt("id");
            Boolean isLogin  = jsonObjectIn.getBoolean("isLogin");
            String name = null;
            if(isLogin){
                name = jsonObjectIn.getString("name");
            }

            //返回
            response.setContentType("application/json");
            ServletOutputStream out = null;
            out = response.getOutputStream();
            List<Blog> blogs = null;
            if(anonymous){
                blogs = blogServer.getMoreBlog(id,1);
            }else{
                blogs = blogServer.getMoreBlog(id,0);
            }

            if(blogs == null){
                JSONObject jsonObjectOut = new JSONObject();
                jsonObjectOut.put("state","false");
                out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            }else{
                ListIterator<Blog> listIterator = blogs.listIterator();
                List<BlogFacade> blogFacades = new ArrayList<BlogFacade>();
                while (listIterator.hasNext()){
                    Blog blog = listIterator.next();
                    BlogFacade blogFacade = new BlogFacade();

                    if(isLogin) {
                        LikeBlog likeBlog = new LikeBlog();
                        int uid = userInfoServer.isExistByName(name).getId();
                        likeBlog.setUid(uid);
                        likeBlog.setBlogid(blog.getId());
                        blogFacade.setLikeState(blogServer.isLikeBlog(likeBlog));
                        FavouBlog favouBlog = new FavouBlog();
                        favouBlog.setUid(uid);
                        favouBlog.setBlogid(blog.getId());
                        blogFacade.setFavouState(blogServer.isFavouBlog(favouBlog));
                    }
                    //获取博客是否匿名
                    if(blog.getAnonymous() == 1){
                        blogFacade.setAnonymous(true);
                    }else{
                        blogFacade.setAnonymous(false);
                    }

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


    @RequestMapping("/refrushMyBlog.do")
    public void refrushMyBlog(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);

            String name  = jsonObjectIn.getString("name");

            //返回
            response.setContentType("application/json");
            ServletOutputStream out = null;
            out = response.getOutputStream();
            List<Blog> blogs = null;
            blogs = blogServer.getRefrushMyBlog(name, 5);
            if(blogs == null){
                JSONObject jsonObjectOut = new JSONObject();
                jsonObjectOut.put("state","false");
                out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            }else{
                ListIterator<Blog> listIterator = blogs.listIterator();
                List<BlogFacade> blogFacades = new ArrayList<BlogFacade>();
                while (listIterator.hasNext()){
                    Blog blog = listIterator.next();
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


    @RequestMapping("/loadMoreMyBlog.do")
    public void loadMoreMyBlog(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);

            int id  = jsonObjectIn.getInt("id");
            String name  = jsonObjectIn.getString("name");

            //返回
            response.setContentType("application/json");
            ServletOutputStream out = null;
            out = response.getOutputStream();
            List<Blog> blogs = null;
            blogs =  blogServer.getMoreMyBlog(id,name);
            if(blogs == null){
                JSONObject jsonObjectOut = new JSONObject();
                jsonObjectOut.put("state","false");
                out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            }else{
                ListIterator<Blog> listIterator = blogs.listIterator();
                List<BlogFacade> blogFacades = new ArrayList<BlogFacade>();
                while (listIterator.hasNext()){
                    Blog blog = listIterator.next();
                    BlogFacade blogFacade = new BlogFacade();

                    LikeBlog likeBlog = new LikeBlog();
                    int uid = userInfoServer.isExistByName(name).getId();
                    likeBlog.setUid(uid);
                    likeBlog.setBlogid(blog.getId());
                    blogFacade.setLikeState(blogServer.isLikeBlog(likeBlog));
                    FavouBlog favouBlog = new FavouBlog();
                    favouBlog.setUid(uid);
                    favouBlog.setBlogid(blog.getId());
                    blogFacade.setFavouState(blogServer.isFavouBlog(favouBlog));

                    //获取博客是否匿名
                    if(blog.getAnonymous() == 1){
                        blogFacade.setAnonymous(true);
                    }else{
                        blogFacade.setAnonymous(false);
                    }

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

    @RequestMapping("/likeBlog.do")
    public void likeBlog(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);

            Boolean isLike = jsonObjectIn.getBoolean("isLike");
            int blogid  = jsonObjectIn.getInt("id");
            String name  = jsonObjectIn.getString("name");

            //返回
            response.setContentType("application/json");
            ServletOutputStream out = null;
            out = response.getOutputStream();

            LikeBlog likeBlog = new LikeBlog();
            likeBlog.setBlogid(blogid);
            likeBlog.setUid(userInfoServer.isExistByName(name).getId());
            if(isLike){
                blogServer.doLikeBlog(likeBlog);
            }else{
                blogServer.noLikeBlog(likeBlog);
            }
            JSONObject jsonObjectOut = new JSONObject();
            jsonObjectOut.put("state","true");
            out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/favouBlog.do")
    public void favouBlog(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);

            Boolean isFavou = jsonObjectIn.getBoolean("isFavou");
            int blogid  = jsonObjectIn.getInt("id");
            String name  = jsonObjectIn.getString("name");

            //返回
            response.setContentType("application/json");
            ServletOutputStream out = null;
            out = response.getOutputStream();

            FavouBlog favouBlog = new FavouBlog();
            favouBlog.setBlogid(blogid);
            favouBlog.setUid(userInfoServer.isExistByName(name).getId());
            if(isFavou){
                blogServer.doFavouBlog(favouBlog);
            }else{
                blogServer.noFavouBlog(favouBlog);
            }
            JSONObject jsonObjectOut = new JSONObject();
            jsonObjectOut.put("state","true");
            out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/addComment.do")
    public void addComment(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);

            int blogid  = jsonObjectIn.getInt("blogId");
            String name  = jsonObjectIn.getString("name");
            String contend = jsonObjectIn.getString("contend");

            //返回
            response.setContentType("application/json");
            ServletOutputStream out = null;
            out = response.getOutputStream();

            CommentFacade commentFacade = new CommentFacade();
            commentFacade.setName(name);
            commentFacade.setContend(contend);
            blogServer.addCommentByCommnetFacade(commentFacade,blogid);
            JSONObject jsonObjectOut = new JSONObject();
            jsonObjectOut.put("state","true");
            out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/addJubao.do")
    public void addJubao(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br = request.getReader();
            String str,body="";
            while((str=br.readLine())!=null){
                body = body+str.trim();
            }
            System.out.println(body);
            JSONObject jsonObjectIn = JSONObject.fromObject(body);

            int blogid  = jsonObjectIn.getInt("blogId");
            String name  = jsonObjectIn.getString("name");
            String contend = jsonObjectIn.getString("contend");

            //返回
            response.setContentType("application/json");
            ServletOutputStream out = null;
            out = response.getOutputStream();

            blogServer.addJubao(name,blogid,contend);
            JSONObject jsonObjectOut = new JSONObject();
            jsonObjectOut.put("state","true");
            out.write(jsonObjectOut.toString().getBytes(Charset.forName("utf-8")));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getAllJubao.do" ,method = RequestMethod.POST ,produces = "text/plain;charset=utf-8")
    public String  adminLogin(HttpServletRequest request, HttpServletResponse response){
        List<Jubao> jubaos = blogServer.getAllJubao();
        ListIterator<Jubao> listIterator = jubaos.listIterator();
        List<JubaoFacade> jubaoFacades = new ArrayList<JubaoFacade>();
        while (listIterator.hasNext()){
            Jubao jubao = listIterator.next();
            JubaoFacade jubaoFacade = new JubaoFacade();
            jubaoFacade.setBlogid(jubao.getBlogid());
            jubaoFacade.setId(jubao.getId());
            jubaoFacade.setName(userInfoServer.getNameById(jubao.getUid()));
            jubaoFacade.setReason(jubao.getReason());
            jubaoFacade.setUid(jubao.getUid());
            jubaoFacade.setBlogContend(blogServer.getContendById(jubao.getBlogid()));
            jubaoFacades.add(jubaoFacade);
        }
        System.out.println(jubaos.get(0).toString());
        JSONArray jsonArray  = JSONArray.fromObject(jubaoFacades);
        return  jsonArray.toString();
    }


    @ResponseBody
    @RequestMapping(value = "/agreeJubao.do" ,method = RequestMethod.POST ,produces = "text/plain;charset=utf-8")
    public String  agreeJubao(HttpServletRequest request, HttpServletResponse response){
        int jubaoId  = Integer.parseInt(request.getParameter("jubaoId"));
        int blogId   = Integer.parseInt(request.getParameter("blogId"));


        blogServer.deleteBlog(blogId);
        blogServer.deleteJubao(jubaoId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state","true");
        return  jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/refuseJubao.do" ,method = RequestMethod.POST ,produces = "text/plain;charset=utf-8")
    public String refuseJubao(HttpServletRequest request, HttpServletResponse response){
        int jubaoId  = Integer.parseInt(request.getParameter("jubaoId"));

        blogServer.deleteJubao(jubaoId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state","true");
        return  jsonObject.toString();
    }

    @RequestMapping("/getBlogPic.do")
    public void getBlogPic(HttpServletRequest request, HttpServletResponse response){
        try {
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json;charset=utf-8");
            System.out.println("sdsad");
            // 配置上传参数
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
            factory.setSizeThreshold(MEMORY_THRESHOLD);
            // 设置临时存储目录
            factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 设置最大文件上传值
            upload.setFileSizeMax(MAX_FILE_SIZE);
            // 设置最大请求值 (包含文件和表单数据)
            upload.setSizeMax(MAX_REQUEST_SIZE);
            upload.setHeaderEncoding("UTF-8");
            // 构造临时路径来存储上传的文件
            // 这个路径相对当前应用的目录
            String uploadPath = "e://wuhan";
            //String uploadPath = "/usr/wuli/images";
            // 如果目录不存在则创建
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            JSONObject json = new JSONObject();
            // 解析请求的内容提取文件数据
            List<FileItem> formItems = upload.parseRequest(request);
            System.out.println(formItems.size());
            List<BlogPic> blogPics = new ArrayList<>();
            int blogId = 0;
            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (item.isFormField()) {
                        System.out.println(item.getFieldName());
                        System.out.println("AAAAAAAA"+item.getString("UTF-8"));
                        json.put(item.getFieldName(), item.getString("UTF-8"));
                        blogId = Integer.parseInt(item.getString("UTF-8"));
                    }else{
                        BlogPic blogPic = new BlogPic();
                        blogPic.setBlogid(blogId);
                        blogPic.setPicture(item.getName());
                        blogPics.add(blogPic);

                        //保存图片到硬盘
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        System.out.println(filePath);
                        File storeFile = new File(filePath);
                        // 保存文件到硬盘
                        item.write(storeFile);
                        json.put(item.getFieldName(),fileName);
                        }
                    }
                }
                blogServer.addBlogPic(blogPics);
            } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.println("success");
        out.close();
    }

}
