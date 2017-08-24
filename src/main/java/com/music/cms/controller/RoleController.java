package com.music.cms.controller;

import com.music.cms.model.Role;
import com.music.cms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value = "/admin/role")
public class RoleController {

    @Autowired
    RoleService roleService;


    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model)
    {
        List<Role> roles = roleService.getallRole();
        model.addAttribute("roles",roles);
        model.addAttribute("pageTitle","Role Management");
        return "backend/role/index";

    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create()
    {
        return "backend/role/form";

    }
}
