package com.music.cms.service;

import com.music.cms.model.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    void saveCategory(Category category);

    Category findById(Integer id);

    List<Category> findAllCategory();

    void update(Category category);

    void delete(Integer id);


}
