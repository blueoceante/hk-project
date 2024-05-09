package com.example.hkproject.validation.restReqParam;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

// 经纬度入参校验
@Documented
@Constraint(validatedBy = LatLongValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface LatLongValid {
    String message() default "Invalid latitude or longitude";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
