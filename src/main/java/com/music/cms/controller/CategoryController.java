package com.music.cms.controller;

import com.music.cms.FlashMessage;
import com.music.cms.model.Category;
import com.music.cms.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
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
        if (!model.containsAttribute("category")) {
            model.addAttribute("category", new Category());
        }
        //model.addAttribute("category",new Category());
        model.addAttribute("button","Add");
        model.addAttribute("pageTitle","Add Category");
        model.addAttribute("url",String.format("/admin/category/store"));
        return "backend/category/form";
    }


    @RequestMapping(value = "/store",method = RequestMethod.POST)
    public String store(@Valid @ModelAttribute("category") Category category, BindingResult result, RedirectAttributes redirectAttributes)
    {
        if(result.hasErrors())
        {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", result);
            redirectAttributes.addFlashAttribute("category", category);
            return "redirect:/admin/category/create";
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
        if(!model.containsAttribute("category"))
        {
            model.addAttribute("category",category);
        }

        model.addAttribute("button","Update");
        model.addAttribute("pageTitle","Edit Category");
        model.addAttribute("url",String.format("/admin/category/%s/update",id));

        return "backend/category/form";

    }


    @RequestMapping(value = "/{id}/update",method = RequestMethod.POST)
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("category") Category category, BindingResult result,ModelMap model,RedirectAttributes redirectAttributes)
            throws Exception {
        if(result.hasErrors())
        {redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", result);
            redirectAttributes.addFlashAttribute("category", category);
            //return "redirect:/admin/category/create";
            return String.format("redirect:/admin/category/%s/edit",id);
        }

        Category categoryCheck = categoryService.findById(id);
        if(categoryCheck == null)
        {
            redirectAttributes.addFlashAttribute("flash",new FlashMessage("Sorry category was not found!", FlashMessage.Status.DANGER));
            return "redirect:/admin/category";
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

    @RequestMapping(value = "/{id}.gif", method = RequestMethod.GET)
    public ResponseEntity<byte[]> categoryImage(@PathVariable("id") Integer id)
    {
        byte[] imageContent = categoryService.findById(id).getImage();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
    }
}
