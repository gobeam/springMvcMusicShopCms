package com.music.cms.service;

import com.music.cms.dao.CategoryDao;
import com.music.cms.model.Category;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public void saveCategory(Category category) {
        if(category.getFile() != null)
        {
            category = setIfImageexists(category);
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

        if(category.getFile() != null)
        {
            category = setIfImageexists(category);
        }

        categoryDao.update(category);

    }

    @Override
    public void delete(Integer id) {
        Category category = categoryDao.findById(id);
        if(category != null)
        {
            categoryDao.delete(category);
        }


    }

    public Category setIfImageexists(Category category)
    {
        try {
            if(category.getFile() != null)
            {
                MultipartFile file = category.getFile();
                BufferedImage originalImage = ImageIO.read(file.getInputStream());
                int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
                BufferedImage resizeImageJpg = resizeImage(originalImage, type);

                // BufferedImage to ByteArrayInputStream
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                String ext = FilenameUtils.getExtension(file.getOriginalFilename());
                ImageIO.write(resizeImageJpg, ext, os);
                byte[] bytes = os.toByteArray();
                //InputStream is = new ByteArrayInputStream(os.toByteArray());

                category.setImage(bytes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return category;

    }

    private BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(100, 100, type);//set width and height of image
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, 100, 100, null);
        g.dispose();

        return resizedImage;
    }
}
