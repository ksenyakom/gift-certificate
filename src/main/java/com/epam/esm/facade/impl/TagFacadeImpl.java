package com.epam.esm.facade.impl;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.TagFacade;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class TagFacadeImpl implements TagFacade {
    @Autowired
    TagService tagService;

    @Override
    public JsonResult<Tag> getTag(int id) {
        Tag tag = tagService.findById(id);
        return new JsonResult.Builder<Tag>()
                .withSuccess(true)
                .withResult(Collections.singletonList(tag))
                .build();
    }

    @Override
    public JsonResult<Tag> save(Tag tag) {
        tagService.save(tag);
        return new JsonResult.Builder<Tag>()
                .withSuccess(true)
                .withResult(Collections.singletonList(tag))
                .build();
    }

    @Override
    public JsonResult<Tag> delete(int id) {
        tagService.delete(id);
        return new JsonResult.Builder<Tag>()
                .withSuccess(true)
                .build();
    }

    @Override
    public JsonResult<Tag> getAllTags() {
        List<Tag> tags = tagService.findAll();
        return  new JsonResult.Builder<Tag>()
                .withSuccess(true)
                .withResult(tags)
                .build();


    }
}