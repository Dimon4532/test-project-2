package ru.learning.java.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.learning.java.model.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(SalaryException.class)
  public ResponseEntity<ErrorResponse> handleSalaryException(
    SalaryException ex,
    HttpServletRequest request) {

    ErrorResponse errorResponse = new ErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      "Salary Validation Error",
      ex.getMessage(),
      request.getRequestURI()
    );

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(errorResponse);
  }

  @ExceptionHandler(TrainingHoursException.class)
  public ResponseEntity<ErrorResponse> handleTrainingHoursException(
    TrainingHoursException ex,
    HttpServletRequest request) {

    ErrorResponse errorResponse = new ErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      "Training Hours Validation Error",
      ex.getMessage(),
      request.getRequestURI()
    );

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(errorResponse);
  }

  @ExceptionHandler(InvalidEmployeeException.class)
  public ResponseEntity<ErrorResponse> handleInvalidEmployeeException(
    InvalidEmployeeException ex,
    HttpServletRequest request) {

    ErrorResponse errorResponse = new ErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      "Invalid Employee Error",
      ex.getMessage(),
      request.getRequestURI()
    );

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(
    Exception ex,
    HttpServletRequest request) {

    ErrorResponse errorResponse = new ErrorResponse(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      "Internal Server Error",
      "Произошла непредвиденная ошибка: " + ex.getMessage(),
      request.getRequestURI()
    );

    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(errorResponse);
  }
}