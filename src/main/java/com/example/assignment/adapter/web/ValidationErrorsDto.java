package com.example.assignment.adapter.web;

import lombok.Value;

import java.util.Set;
import java.util.stream.Collectors;

@Value(staticConstructor = "of")
public class ValidationErrorsDto {
    Set<ValidationErrorDto> errorCodes;

    public static ValidationErrorsDto ofErrorCodes(Set<ValidationErrorCode> validationErrorCodes) {
        Set<ValidationErrorDto> errors = validationErrorCodes.stream()
            .map(error -> ValidationErrorDto.of(error.name()))
            .collect(Collectors.toUnmodifiableSet());
        return ValidationErrorsDto.of(errors);
    }

    @Value(staticConstructor = "of")
    public static class ValidationErrorDto {
        String name;
    }
}
