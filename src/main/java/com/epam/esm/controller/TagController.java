package com.epam.esm.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Tag create(@RequestBody Tag tag) throws ServiceException {
        tagService.save(tag);

        return tagService.findById(tag.getId());
    }

    @DeleteMapping("/{id}")
    public List<Tag> delete(@PathVariable("id") int id) throws ServiceException {
        tagService.delete(id);
        return tagService.findAll();
    }
}
