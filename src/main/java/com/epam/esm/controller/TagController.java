package com.epam.esm.controller;

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

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping()
    public List<Tag> index() throws ServiceException {
        return tagService.findAll();
    }

    @GetMapping("/{id}")
    public Tag show(@PathVariable("id") int id) throws ServiceException {
        return tagService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Tag create(@RequestBody Tag tag, BindingResult result) throws ServiceException {
        TagValidator validator = new TagValidator();
        validator.validate(tag,result);
        if (!result.hasErrors()) {
            tagService.save(tag);
            return tag;
        } else {
            throw new ServiceException(message(result), "20");
        }
    }

    @DeleteMapping("/{id}")
    public List<Tag> delete(@PathVariable("id") int id) throws ServiceException {
        tagService.delete(id);
        return tagService.findAll();
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
