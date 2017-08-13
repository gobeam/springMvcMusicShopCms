package com.music.cms.controller;

import com.music.cms.model.User;
import com.music.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
        model.addAttribute("title", "Admin Login");
        return "backend/login/login";
    }

    @RequestMapping(value = "/registration",method = RequestMethod.GET)
    public String register(ModelMap model) {
        model.addAttribute("title", "Register");
        User user = new User();
        model.addAttribute("user", user);
        return "backend/login/register";
    }

    @RequestMapping(value = "/registration",method = RequestMethod.POST)
    public String registration(@Valid User user, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) throws Exception {

        if (result.hasErrors()) {
            System.out.println(result);
            return "backend/login/register";
        }

        User userExist = userService.findUserByEmail(user.getEmail());
        if (userExist != null) {
            result
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }else{
            userService.saveUser(user);
            //model.addAttribute("successMessage", "User has been registered successfully");
            model.addAttribute("user", new User());
            redirectAttributes.addAttribute("successMessage", "User has been registered successfully");
            return "redirect:/registration";
        }
        return "backend/login/register";
    }


}
