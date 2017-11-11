package com.music.cms.validator;


import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = ValidImageSizeValidator.class)
@Documented
public @interface ImageValidationSize {

    String message() default "Image size should be not more than 300KB!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

//    int min() default 5;
}