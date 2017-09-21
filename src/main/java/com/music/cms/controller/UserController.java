package com.music.cms.controller;

import com.music.cms.FlashMessage;
import com.music.cms.model.Role;
import com.music.cms.model.User;
import com.music.cms.service.RoleService;
import com.music.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;


    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model)
    {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users",users);
        model.addAttribute("pageTitle","User Management");
        return "backend/user/index";
    }

    @RequestMapping(value = "/create",method = RequestMethod.GET)
    public String create(ModelMap model)
    {
        List<Role> roles = roleService.getallRole();

        System.out.println(roles);

        if(!model.containsAttribute("user"))
        {
            model.addAttribute("user",new User());
        }

        model.addAttribute("button","Add");
        model.addAttribute("pageTitle","Add User");
        model.addAttribute("url",String.format("/admin/user/store"));
        model.addAttribute("store",true);
        model.addAttribute("roles",roles);

        return "backend/user/form";
    }


    @RequestMapping(value = "/store",method = RequestMethod.POST)
    public String store(@Validated(User.GroupValidationAdd.class) @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirectAttributes) throws Exception
    {
        if(result.hasErrors())
        {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user",result);
            redirectAttributes.addFlashAttribute("user","user");
            return "redirect:/admin/user/create";
        }
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("User updated successfully!",FlashMessage.Status.SUCCESS));
        return "redirect:/admin/user";

    }

    @RequestMapping(value = "/{id}/edit",method = RequestMethod.GET)
    public String edit(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, ModelMap model) {
        User user = userService.findById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("User not found!", FlashMessage.Status.DANGER));
            return "redirect:/admin/user";
        }

        List<Role> roles = roleService.getallRole();

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", user);
        }

        if(user.getRoles() != null)
        {
            Integer i = 0;
            String userRoleName = null;
            Set<Role> roleSet = user.getRoles();
            for (Role userRole : roleSet) {
                i++;
                if (i == 1) {
                    userRoleName = userRole.getRole();
                }
            }
            model.addAttribute("userRoleName",userRoleName);
        }


        model.addAttribute("button","Update");
        model.addAttribute("pageTitle","Edit User");
        model.addAttribute("url",String.format("/admin/user/%s/update",id));
        model.addAttribute("roles",roles);
        model.addAttribute("store",false);

        return "backend/user/form";

    }


    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    public String update(@PathVariable("id") Integer id, @Validated(User.GroupValidationUpdate.class) @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirectAttributes) throws Exception
    {
        if(result.hasErrors())
        {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user",result);
            redirectAttributes.addFlashAttribute("user",user);
            return String.format("redirect:/admin/user/%s/edit",id);
        }

        User userCheck = userService.findById(id);
        if(userCheck == null)
        {
            redirectAttributes.addFlashAttribute("flash",new FlashMessage("User not found!",FlashMessage.Status.DANGER));
            return  "redirect:/admin/user";

        }

        User userExist = userService.findUserByEmailForUpdate(user);
        if (userExist != null) {
            result
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }else{
            userService.updatePartial(user);
            redirectAttributes.addFlashAttribute("flash",new FlashMessage("User successfully updated!",FlashMessage.Status.SUCCESS));
        }


        return  "redirect:/admin/user";

    }

    @RequestMapping(value = "/{id}/delete",method = RequestMethod.POST)
    public String destroy(@PathVariable("id")Integer id,RedirectAttributes redirectAttributes)
    {
        userService.destroy(id);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("User deleted successfully!",FlashMessage.Status.SUCCESS));
        return  "redirect:/admin/user";
    }



}
