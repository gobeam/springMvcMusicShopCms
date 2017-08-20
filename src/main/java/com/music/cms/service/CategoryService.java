package com.music.cms.service;

import com.music.cms.model.Category;

public interface CategoryService {
    public Category findById(String email);

    public void saveCategory(Category category);

    public Category findById(Integer id);
}
