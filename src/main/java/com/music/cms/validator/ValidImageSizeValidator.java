package com.music.cms.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;


public class ValidImageSizeValidator implements ConstraintValidator<ImageValidationSize, MultipartFile> {

    //private int min;

    @Override
    public void initialize(ImageValidationSize checkImageSize) {

    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        try {
            if(file.getBytes() != null)
            {
                if((file.getSize() /1024) > 200)
                {
                    System.out.println("this is val");
                    System.out.println((file.getSize() /1024));
                    return false;
                }

            }
            return true;
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }

    }

}
