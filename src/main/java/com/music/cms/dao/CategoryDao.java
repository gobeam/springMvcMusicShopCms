package com.music.cms.dao;

import com.music.cms.model.Category;
import java.util.List;

public interface CategoryDao {

    Category findById(int id);

    void save(Category category);

    void update(Category category);

    void deleteById(int id);

    List<Category> findAllCategory();

}
