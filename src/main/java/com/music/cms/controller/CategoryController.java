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
    public String index(ModelMap model)
    {
        List<Category> categories= categoryService.findAllCategory();

        model.addAttribute("categories",categories);
        model.addAttribute("pageTitle","Category Management");
        return "backend/category/index";
    }


    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(ModelMap model)
    {
        model.addAttribute("category",new Category());
        return "create";
    }


    @RequestMapping(method = RequestMethod.POST)
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
    public String edit(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes,ModelMap model)
    {

        Category category = categoryService.findById(id);

        if(category == null)
        {
            redirectAttributes.addFlashAttribute("flash",new FlashMessage("Sorry category was not found!", FlashMessage.Status.DANGER));
            return "redirect:/admin/category";
        }
        model.addAttribute("category",category);
        model.addAttribute("method","PUT");
        model.addAttribute("button","Update");
        model.addAttribute("pageTitle","Edit Category");
        model.addAttribute("url",String.format("/admin/category/%s/update",id));

        return "backend/category/form";

    }


    @RequestMapping(value = "/{id}/update",method = RequestMethod.POST)
    public String update(@Valid Category category,@PathVariable("id") Integer id, RedirectAttributes redirectAttributes,BindingResult result)
    {
        Category categoryCheck = categoryService.findById(id);
        if(categoryCheck == null)
        {
            redirectAttributes.addFlashAttribute("flash",new FlashMessage("Sorry category was not found!", FlashMessage.Status.DANGER));
            return "redirect:/admin/category";
        }
        if(result.hasErrors())
        {
            return "backend/category/form";
        }

        categoryService.update(category);

        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Category successfully updated!", FlashMessage.Status.SUCCESS));
        return "redirect:/admin/category";

    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String destroy(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes)
    {
        categoryService.delete(id);

        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Category successfully updated!", FlashMessage.Status.SUCCESS));
        return "redirect:/admin/category";
    }
}
