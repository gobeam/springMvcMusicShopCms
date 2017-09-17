package com.music.cms.controller;

import com.music.cms.model.Category;
import com.music.cms.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class FrontendHomeController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        List<Category> categories = categoryService.findAllCategory();

        model.addAttribute("categories",categories);
        model.addAttribute("title", "Spring MVC with Thymeleaf");
        model.addAttribute("pageHeading", "Spring MVC with Thymeleaf");
        model.addAttribute("imgUrl", "http://www.thymeleaf.org/doc/tutorials/2.1/images/header.png");

        return "frontend/home/index";
    }
}
