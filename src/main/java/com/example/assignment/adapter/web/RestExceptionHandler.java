package com.example.assignment.adapter.web;

import com.example.assignment.appdomain.sanctionedperson.exception.NoSuchPersonException;
import com.example.assignment.appdomain.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(Exception e) {
        log.error("Unhandled exception", e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.warn("Data integrity violation", e);
    }

    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorsDto> handleValidationException(ValidationException e) {
        log.info("Request produced validation errors: {}", e.getErrors(), e);
        return new ResponseEntity<>(ValidationErrorsDto.ofErrorCodes(e.getErrors()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchPersonException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchPersonException(NoSuchPersonException e, WebRequest request) {
        log.warn("No processable person was present", e);
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
            e.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
