package com.music.cms.controller;

import com.music.cms.FlashMessage;
import com.music.cms.model.User;
import com.music.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class BackendHomeController {

    @Autowired
    PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationTrustResolver authenticationTrustResolver;

    @RequestMapping(value = "/admin/home",method = RequestMethod.GET)
    public String index(ModelMap model) {
        model.addAttribute("title", "Spring MVC with Thymeleaf");








        return "backend/home/index";
    }

//    @RequestMapping(value = "/login",method = RequestMethod.GET)
//    public String login(ModelMap model) {
//        model.addAttribute("title", "Admin Login");
//        return "backend/login/login";
//    }

    @RequestMapping(value = "/registration",method = RequestMethod.GET)
    public String register(ModelMap model) {
        model.addAttribute("title", "Register");
        User user = new User();
        model.addAttribute("user", user);
        return "backend/login/register";
    }

    @RequestMapping(value = "/registration",method = RequestMethod.POST)
    public String registration(@Validated(User.GroupValidationRegister.class) User user, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes) throws Exception {

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
            //redirectAttributes.addAttribute("successMessage", "User has been registered successfully");
            redirectAttributes.addFlashAttribute("flash",new FlashMessage("Cheers you have been successfully registered please go to login page to log in!", FlashMessage.Status.SUCCESS));
            return "redirect:/registration";
        }
        return "backend/login/register";
    }

    @RequestMapping(value = "/access-denied")
    public  String accessDenied()
    {
        return "backend/fragment/access-denied";
    }


    /**
     * This method handles login GET requests.
     * If users is already logged-in and tries to goto login page again, will be redirected to list page.
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(ModelMap model, HttpSession session) {
        if(session.getAttribute("error") != null){
            System.out.println(session.getAttribute("error"));
            System.out.println("hareram");
            model.addAttribute("isError",true);
            model.addAttribute("error",session.getAttribute("error"));
            session.removeAttribute("error");
        }else{
            model.addAttribute("isError",false);
        }

        if (isCurrentAuthenticationAnonymous()) {
            model.addAttribute("title", "Admin Login");
            return "backend/login/login";
        } else {
            return "redirect:/admin/home";
        }
    }


    /**
     * This method handles logout requests.
     * Toggle the handlers if you are RememberMe functionality is useless in your app.
     */
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            //new SecurityContextLogoutHandler().logout(request, response, auth);
            persistentTokenBasedRememberMeServices.logout(request, response, auth);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/login?logout";
    }

//    /**
//     * This method returns the principal[user-name] of logged-in user.
//     */
//    private String getPrincipal(){
//        String userName = null;
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (principal instanceof UserDetails) {
//            userName = ((UserDetails)principal).getUsername();
//        } else {
//            userName = principal.toString();
//        }
//        return userName;
//    }

    /**
     * This method returns true if users is already authenticated [logged-in], else false.
     */
    private boolean isCurrentAuthenticationAnonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authenticationTrustResolver.isAnonymous(authentication);
    }


}
