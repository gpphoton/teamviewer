package com.teamviewer.controller.advice;

import com.teamviewer.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class DefaultRestControllerAdviceTest {

  private DefaultRestControllerAdvice advice;

  @Mock private BindingResult bindingResult;

  @BeforeAll
  void setUp() {
    advice = new DefaultRestControllerAdvice();
    bindingResult = Mockito.mock(BindingResult.class);
  }

  @AfterEach
  void afterEach() {
    reset(bindingResult);
  }

  @Test
  void handleGeneralException() {
    Exception ex = new Exception("Test exception");
    ApiErrorDto response = advice.handleGeneralException(ex);
    assertEquals("Internal server error", response.getMessage());
  }

  @Test
  void handleResourceNotFound() {
    ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
    ApiErrorDto response = advice.handleResourceNotFound(ex);
    assertEquals("Resource not found", response.getMessage());
  }
}
