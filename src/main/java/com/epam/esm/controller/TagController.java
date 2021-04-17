package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.TagFacade;
import com.epam.esm.model.Tag;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagFacade tagFacade;

    @GetMapping()
    public JsonResult<Tag> index() throws ServiceException {
        return tagFacade.getAllTags();

    }

    @GetMapping("/{id}")
    public JsonResult<Tag> show(@PathVariable("id") int id) throws ServiceException {
        return tagFacade.getTag(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<Tag> create(@RequestBody Tag tag, BindingResult result) throws ServiceException {
        TagValidator validator = new TagValidator();
        validator.validate(tag,result);
        if (!result.hasErrors()) {
            return tagFacade.save(tag);
        } else {
            throw new ServiceException(message(result), "20");
        }
    }

    @DeleteMapping("/{id}")
    public JsonResult<Tag> delete(@PathVariable("id") int id) throws ServiceException {
        return tagFacade.delete(id);
    }

    private String message(BindingResult result) {
        StringBuilder sb = new StringBuilder();
        result.getFieldErrors()
                .forEach(fieldError -> sb.append(" ")
                        .append(fieldError.getField())
                        .append(" :")
                        .append(fieldError.getCode())
                        .append(";"));
        return sb.toString();
    }
}
