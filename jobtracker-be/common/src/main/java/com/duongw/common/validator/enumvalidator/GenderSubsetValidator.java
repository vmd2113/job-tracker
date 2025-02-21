package com.duongw.common.validator.enumvalidator;

import com.duongw.common.validator.enumvalidator.UserGender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class GenderSubsetValidator implements ConstraintValidator<GenderSubset, UserGender> {
    private com.duongw.common.validator.enumvalidator.UserGender[] genders;

    @Override
    public void initialize(GenderSubset constraint) {
        this.genders = constraint.anyOf();
    }

    @Override
    public boolean isValid(UserGender value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(genders).contains(value);
    }
}
