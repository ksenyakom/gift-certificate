package com.epam.esm.controller;

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
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody
    Map<String, String> someError(ServiceException e) {
        logger.error("Error code:{}. Error message:{}",e.getErrorCode(),e.getMessage(), e.getCause());
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", e.getMessage());
        error.put("errorCode", "" + e.getErrorCode());
        return error;
    }

}
