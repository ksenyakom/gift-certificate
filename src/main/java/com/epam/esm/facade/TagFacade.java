package com.epam.esm.facade;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

public interface TagFacade {
    JsonResult<Tag> getTag(int id);

    JsonResult<Tag> save(Tag tag);

    JsonResult<Tag> delete(int id);

    JsonResult<Tag> getAllTags();
}
