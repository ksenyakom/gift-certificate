package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.model.Entity;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ExceptionInterceptor {
    private static Logger logger = LogManager.getLogger(ExceptionInterceptor.class);

    @ExceptionHandler(ServiceException.class)
    public @ResponseBody
    JsonResult<? extends Entity> someError(ServiceException e) {
        logger.error("Error code:{}. Error message:{}",e.getErrorCode(),e.getMessage(), e.getCause());
        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(false)
                .withMessage(e.getMessage())
                .withStatus(e.getErrorCode())
                .build();
    }

}
