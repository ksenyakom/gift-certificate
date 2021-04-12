package com.epam.esm.service.validator;

import com.epam.esm.model.Entity;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

public class GiftCertificateValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return GiftCertificate.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        GiftCertificate certificate = (GiftCertificate) o;
        int maxLength = 255;
        int minValue =1;

        if (certificate.getName() == null) {
            errors.rejectValue("name", "empty field");
        } else if (certificate.getName().length() < minValue || certificate.getName().length() > maxLength) {
            errors.rejectValue("name", "invalid length");
        }

        String description = certificate.getDescription();
        if (description == null) {
            errors.rejectValue("description", "empty field");
        } else if (description.isEmpty() || description.length() > maxLength) {
            errors.rejectValue("description", "invalid length");
        }

        if (certificate.getDuration() <= 0) {
            errors.rejectValue("duration", "invalid value");
        }

        if (certificate.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.rejectValue("price", "invalid value");
        }

        if (certificate.getTags() == null || certificate.getTags().isEmpty() ) {
            errors.rejectValue("tags", "empty field");
        } else {
            for (Tag tag : certificate.getTags()) {
                if (tag.getName().length() < minValue || tag.getName().length() > maxLength ) {
                    errors.rejectValue("tags", "invalid name length");
                }
            }
        }

    }
}
