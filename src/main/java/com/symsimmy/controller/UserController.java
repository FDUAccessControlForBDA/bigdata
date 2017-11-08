package com.symsimmy.controller;

import com.google.gson.Gson;
import com.symsimmy.model.User;
import com.symsimmy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getUserById",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getUserById(HttpServletRequest request){
        Map<String,Object> rm = new HashMap<>();
        Integer uid=Integer.parseInt(request.getParameter("uid"));
        try {
            User user = userService.getUserById(uid);
            Gson gson = new Gson();
            String userString = gson.toJson(user);
            System.out.println(userString);
            rm.put("user",userString);
            rm.put("result","success");
            rm.put("code","200");
        }catch (Exception e){
            e.printStackTrace();
        }
        return rm;
    }


}
