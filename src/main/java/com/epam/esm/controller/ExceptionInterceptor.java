package com.epam.esm.controller;

import com.epam.esm.service.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler(ServiceException.class)
 //   @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "not found")
    public @ResponseBody
    Map<String, String> someError(ServiceException e) {
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", e.getMessage());
        error.put("errorCode", "" + e.getErrorCode());
        return error;
    }
}
