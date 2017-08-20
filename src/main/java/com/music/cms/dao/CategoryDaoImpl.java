package com.music.cms.dao;

import com.music.cms.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDaoImpl implements CategoryDao {
    @Override
    public Category findById(int id) {
        return null;
    }

    @Override
    public void save(Category category) {

    }

    @Override
    public void update(Category category) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public List<Category> findAllCategory() {
        return null;
    }
}
