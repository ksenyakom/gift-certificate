package com.epam.esm.controller;

import com.epam.esm.dto.Dto;
import com.epam.esm.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionInterceptor {
    private static Logger logger = LogManager.getLogger(ExceptionInterceptor.class);

    @ExceptionHandler(ServiceException.class)
    public @ResponseBody
    Dto someError(ServiceException e) {
        Dto dto = new Dto("fail",e.getMessage(), null );
        logger.error("Error code:{}. Error message:{}",e.getErrorCode(),e.getMessage(), e.getCause());
        return dto;
    }

}
