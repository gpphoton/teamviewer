package com.teamviewer.controller.advice;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiErrorDto {

  private String message;
}
