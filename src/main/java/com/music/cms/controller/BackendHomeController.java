package com.music.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BackendHomeController {

    @RequestMapping(value = "/admin/home",method = RequestMethod.GET)
    public String index(ModelMap model) {
        model.addAttribute("title", "Spring MVC with Thymeleaf");
        return "backend/home/index";
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(ModelMap model) {
        model.addAttribute("title", "Admin Login");
        return "backend/login/login";
    }


}
