package com.epam.esm.service.validator;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

public class TagValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Tag.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Tag tag = (Tag) o;
        int maxLength = 255;
        int minValue =1;

        if (tag.getName() == null) {
            errors.rejectValue("name", "empty field");
        } else if (tag.getName().length() < minValue || tag.getName().length() > maxLength) {
            errors.rejectValue("name", "invalid length", "Name: invalid length");
        }

    }
}