package com.music.cms.controller;

import com.music.cms.FlashMessage;
import com.music.cms.model.Category;
import com.music.cms.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String index()
    {
        //List<Category> categories= categoryService.findAllCategory();
        return "backend/category/index";
    }


    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String create(ModelMap model)
    {
        model.addAttribute("category",new Category());
        return "create";
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public String store(@Valid Category category, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes)
    {
        if(result.hasErrors())
        {
            return "category";
        }
        categoryService.saveCategory(category);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("New Category created successfully!", FlashMessage.Status.SUCCESS));

        return "redirect:/admin/category";


    }


    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String edit(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes)
    {
        Category category = categoryService.findById(id);
        if(category ==null)
        {
            redirectAttributes.addFlashAttribute("flash",new FlashMessage("Sorry category was not found!", FlashMessage.Status.FAILURE));
            return "redirect:/admin/category";
        }

        return "edit";

    }


    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public String update(@Valid Category category, RedirectAttributes redirectAttributes)
    {
        categoryService.update(category);

        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Category successfully updated!", FlashMessage.Status.SUCCESS));
        return "redirect:/admin/category";

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public String destroy(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes)
    {
        categoryService.delete(id);

        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Category successfully updated!", FlashMessage.Status.SUCCESS));
        return "redirect:/admin/category";
    }
}
