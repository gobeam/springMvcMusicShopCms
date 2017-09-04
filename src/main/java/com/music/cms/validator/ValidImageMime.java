package com.music.cms.validator;


import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.util.ArrayList;


public class ValidImageMime implements ConstraintValidator<ImageValidationMime, MultipartFile> {

    //private int min;

    @Override
    public void initialize(ImageValidationMime checkImageSize) {

    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        try {
            ArrayList<String> mimes = new ArrayList<String>();
            mimes.add("jpg");
            mimes.add("JPG");
            mimes.add("JPEG");
            mimes.add("jpeg");
            mimes.add("png");
            if(file.getBytes() != null)
            {
                System.out.println("image not null!");
                if(mimes.contains(file.getContentType()))
                {

                    return true;
                }
                return false;

            }
            return true;
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }

    }

}

