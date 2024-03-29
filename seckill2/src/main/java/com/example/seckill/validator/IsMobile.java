package com.example.seckill.validator;


import com.example.seckill.vo.isMobileValidator;
import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {isMobileValidator.class}
)
public @interface IsMobile {
    boolean required() default true;

    String message() default "手机号码无效！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
