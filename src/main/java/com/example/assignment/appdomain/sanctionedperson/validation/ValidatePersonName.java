package com.example.assignment.appdomain.sanctionedperson.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.example.assignment.adapter.web.ValidationErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidatePersonName {

    private static final int NAME_MAX_LENGTH = 1000;

    public Set<ValidationErrorCode> execute(Request request) {
        Set<ValidationErrorCode> errors = new HashSet<>();
        String name = request.getFullName();
        if (isBlank(name)) {
            errors.add(PersonNameChangeErrorCode.PERSON_NAME_IS_EMPTY);
            return errors;
        }
        if (name.length() > NAME_MAX_LENGTH) {
            errors.add(PersonNameChangeErrorCode.PERSON_NAME_IS_TOO_LONG);
        }
        if (!isValidName(name)) {
            errors.add(PersonNameChangeErrorCode.PERSON_NAME_HAS_INVALID_FORMAT);
        }
        return errors;
    }

    public boolean isValidName(String name) {
        return name.matches("^[\\p{L} .'-]+$");
    }

    @Value(staticConstructor = "of")
    public static class Request {
        String fullName;
    }

    public enum PersonNameChangeErrorCode implements ValidationErrorCode {
        PERSON_NAME_IS_EMPTY,
        PERSON_NAME_IS_TOO_LONG,
        PERSON_NAME_HAS_INVALID_FORMAT
    }

}
