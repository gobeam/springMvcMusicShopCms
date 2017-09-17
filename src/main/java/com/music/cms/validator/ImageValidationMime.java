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
@Constraint(validatedBy = ValidImageMime.class)
@Documented
public @interface ImageValidationMime {

    String message() default "Image extension not supported!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

//    int min() default 5;
}
