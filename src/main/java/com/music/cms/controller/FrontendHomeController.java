package com.music.cms.controller;

import com.music.cms.model.Category;
import com.music.cms.model.Song;
import com.music.cms.service.CategoryService;
import com.music.cms.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Controller
@PropertySource(value = { "classpath:application.properties" })
public class FrontendHomeController {

    @Autowired
    private Environment environment;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SongService songService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        List<Category> categories = categoryService.findAllCategory();
        List<Song> songs = songService.listAll();

        model.addAttribute("categories",categories);
        model.addAttribute("songs",songs);
        model.addAttribute("title", "Spring MVC with Thymeleaf");
        model.addAttribute("pageHeading", "Spring MVC with Thymeleaf");
        model.addAttribute("imgUrl", "http://www.thymeleaf.org/doc/tutorials/2.1/images/header.png");

        return "frontend/home/index";
    }

    @RequestMapping(value = "/image/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable(value = "imageName") String imageName) throws IOException {

        File serverFile = new File(environment.getRequiredProperty("image.filePath") + imageName);

        byte[] imageContent = Files.readAllBytes(serverFile.toPath());
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
    }
}
