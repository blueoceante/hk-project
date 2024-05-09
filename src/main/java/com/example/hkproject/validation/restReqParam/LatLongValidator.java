package com.example.hkproject.validation.restReqParam;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LatLongValidator implements ConstraintValidator<LatLongValid, String[]> {
    @Override
    public void initialize(LatLongValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(String[] value, ConstraintValidatorContext context) {
        if (value == null || value.length != 2) {
            return false;
        }
        try {
            double lat = Double.parseDouble(value[0]);
            double lon = Double.parseDouble(value[1]);
            return lat >= -90 && lat <= 90 && lon >= -180 && lon <= 180;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
