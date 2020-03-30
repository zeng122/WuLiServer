package com.zzm.controller;

import com.zzm.entity.UserInfo;
import com.zzm.server.Impl.UserInfoServerImpl;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class ExController {
    @Autowired
    private UserInfoServerImpl userInfoServer;

    //JSONArray array = JSONArray.fromObject(list);
    // String jsonstr = array.toString();
    //@RequestParam("aa") String s
   /* @RequestMapping("/hhhh")
    @ResponseBody
    public String  hahah(@RequestParam("aa") String s){
        JSONObject jsonObject = new JSONObject();
        System.out.println("11111");

        jsonObject.put("hah","1111");
        System.out.println(s);
       //return jsonObject.toString();
        return jsonObject.toString();
    }*/
    @RequestMapping("/hhhh.do")
    public void  hahah(HttpServletResponse response){
        response.setContentType("application/json");
        System.out.println("11111");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("status", 1);
        out.write(json.toString());
        out.flush();
        out.close();
    }

    @RequestMapping("/database.do")
    public void  database(HttpServletResponse response){
        JSONObject json = new JSONObject();
        UserInfo userInfo = new UserInfo();
        userInfo.setName("admin");
        userInfo.setPassword("123");
        UserInfo userInfoRes = userInfoServer.login(userInfo);
        if(userInfoRes !=null){
            json.put("state","true");
        }else{
            json.put("state","false");
        }
        response.setContentType("application/json");
        System.out.println("11111");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.write(json.toString());
        out.flush();
        out.close();
    }
}
