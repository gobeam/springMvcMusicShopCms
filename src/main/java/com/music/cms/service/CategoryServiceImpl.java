package com.music.cms.service;

import com.music.cms.dao.CategoryDao;
import com.music.cms.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public void saveCategory(Category category) {

        try {
            if(category.getFile() != null)
            {
                MultipartFile file = category.getFile();
                category.setImage(file.getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        categoryDao.save(category);

    }

    @Override
    public Category findById(Integer id) {
        return categoryDao.findById(id);
    }

    @Override
    public List<Category> findAllCategory() {
        return categoryDao.findAllCategory();
    }

    @Override
    public void update(Category category) {
        categoryDao.update(category);

    }

    @Override
    public void delete(Integer id) {

    }
}
