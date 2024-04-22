package com.teamviewer.controller.advice;

import com.teamviewer.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class DefaultRestControllerAdvice {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiErrorDto handleGeneralException(Exception ex) {
    // Log the exception
    log.error("An unexpected error occurred", ex);
    return ApiErrorDto.builder().message("Internal server error").build();
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiErrorDto handleResourceNotFound(ResourceNotFoundException ex) {

    return ApiErrorDto.builder().message(ex.getMessage()).build();
  }
}
