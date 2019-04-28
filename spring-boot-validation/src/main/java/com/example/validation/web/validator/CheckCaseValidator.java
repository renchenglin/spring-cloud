package com.example.validation.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckCaseValidator implements ConstraintValidator<CheckCase, String> {
    private CaseMode caseMode;
    public void initialize(CheckCase checkCase) {
        this.caseMode = checkCase.value();
    }

    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }

        if (caseMode == CaseMode.UPPER) {
            return s.equals(s.toUpperCase());
        } else {
            return s.equals(s.toLowerCase());
        }
    }
}
