package com.music.cms.controller;

import com.music.cms.model.User;
import com.music.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BackendHomeController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/admin/home",method = RequestMethod.GET)
    public String index(ModelMap model) {
        model.addAttribute("title", "Spring MVC with Thymeleaf");
        return "backend/home/index";
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(ModelMap model) {
        User user = userService.findById(1);
        System.out.println("roshan rana");
        System.out.println(user.getEmail());
        model.addAttribute("title", "Admin Login");
        return "backend/login/login";
    }


}
