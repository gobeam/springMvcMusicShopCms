package com.music.cms.controller;

import com.music.cms.FlashMessage;
import com.music.cms.model.Role;
import com.music.cms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
    public String create(Role roles,ModelMap model)
    {
        if(!model.containsAttribute("roles"))
        {
            model.addAttribute("roles",roles);
        }
        model.addAttribute("pageTitle","Edit Role");
        model.addAttribute("button","Add");
        model.addAttribute("url",String.format("/admin/role/store"));
        return "backend/role/form";

    }


    @RequestMapping(value = "/store", method = RequestMethod.POST)
    public String store(@Valid @ModelAttribute("roles") Role roles, BindingResult result, RedirectAttributes redirectAttributes) throws Exception
    {
        if(result.hasErrors())
        {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.roles",result);
            redirectAttributes.addFlashAttribute("roles",roles);
            return "redirect:/admin/role/create";
        }
        roleService.store(roles);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Role added successfully!",FlashMessage.Status.SUCCESS));
        return "redirect:/admin/role";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Integer id, ModelMap model, RedirectAttributes redirectAttributes) throws Exception
    {
        Role roles = roleService.findById(id);
        if(roles != null)
        {
            model.addAttribute("button","Update");
            model.addAttribute("pageTitle","Edit Role");
            model.addAttribute("url",String.format("/admin/role/%s/update",id));
            if(!model.containsAttribute("roles"))
            {
                model.addAttribute("roles",roles);
            }

            return "backend/role/form";

        }else{
            redirectAttributes.addFlashAttribute("flash",new FlashMessage("Role was not found!",FlashMessage.Status.DANGER));
            return "redirect:/admin/role";
        }

    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    public String update(@PathVariable("id") Integer id,@Valid @ModelAttribute("roles") Role roles,BindingResult result, RedirectAttributes redirectAttributes) throws Exception
    {
        if(result.hasErrors())
        {
            System.out.println("hello myan");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.roles",result);
            redirectAttributes.addFlashAttribute("roles",roles);

            return String.format("redirect:/admin/role/%s/edit",id);

        }
        roleService.update(roles);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Role updated successfully!",FlashMessage.Status.SUCCESS));
        return "redirect:/admin/role";


    }

    @RequestMapping(value = "/{id}/delete",method = RequestMethod.POST)
    public String destroy(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes)
    {
        roleService.destroy(id);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Role deleted successfully!",FlashMessage.Status.SUCCESS));
        return "redirect:/admin/role";

    }
}
