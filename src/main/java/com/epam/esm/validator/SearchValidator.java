package com.epam.esm.validator;

import com.epam.esm.model.SearchParams;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.Map;

public class SearchValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return SearchParams.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SearchParams searchParams = (SearchParams) o;

        int maxLength = 255;
        String sortByName = searchParams.getSortByName();
        String sortByDate = searchParams.getSortByDate();
        String name = searchParams.getName();
        String tagName = searchParams.getTagName();
//        String sortByName = allRequestParams.get("sortByName");
//        String sortByDate = allRequestParams.get("sortByDate");
//        String name = allRequestParams.get("name");
//        String tagName = allRequestParams.get("tagName");

        if (name != null && name.length() > maxLength) {
                errors.rejectValue("name", "invalid length", "Name: invalid length");
        }

        if (tagName != null && tagName.length() > maxLength) {
            errors.rejectValue("tagName", "invalid length", "TagName: invalid length");
        }

        if (sortByDate != null && !(sortByDate.equalsIgnoreCase("asc") || sortByDate.equalsIgnoreCase("desc"))) {
            errors.rejectValue("sortByDate", "invalid value", "sortByDate: invalid value. Can be asc or desc");
        }


        if (sortByName != null && !(sortByName.equalsIgnoreCase("asc") || sortByName.equalsIgnoreCase("desc"))) {
            errors.rejectValue("sortByName", "invalid value", "sortByName: invalid value. Can be asc or desc");
        }
    }


}


