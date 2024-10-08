package com.example.assignment.appdomain.sanctionedperson.validation;

import static com.example.assignment.appdomain.sanctionedperson.validation.ValidatePersonName.PersonNameChangeErrorCode.PERSON_NAME_HAS_INVALID_FORMAT;
import static com.example.assignment.appdomain.sanctionedperson.validation.ValidatePersonName.PersonNameChangeErrorCode.PERSON_NAME_IS_EMPTY;
import static com.example.assignment.appdomain.sanctionedperson.validation.ValidatePersonName.PersonNameChangeErrorCode.PERSON_NAME_IS_TOO_LONG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.assignment.adapter.web.ValidationErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ValidatePersonNameTest {

    private ValidatePersonName validator;

    @BeforeEach
    void setUp() {
        validator = new ValidatePersonName();
    }

    @Test
    void execute_withEmptyName_returnsPersonNameIsEmptyErrorCode() {
        Set<ValidationErrorCode> errorCodes = validator.execute(ValidatePersonName.Request.of(""));

        assertThat(errorCodes).containsExactly(PERSON_NAME_IS_EMPTY);
    }

    @Test
    void execute_withTooLongName_returnsPersonNameIsTooLongErrorCode() {
        Set<ValidationErrorCode> errorCodes = validator.execute(ValidatePersonName.Request.of("a".repeat(1001)));

        assertThat(errorCodes).containsExactly(PERSON_NAME_IS_TOO_LONG);
    }

    @Test
    void execute_withForbiddenCharacters_returnsPersonNameHasInvalidFormatErrorCode() {
        Set<ValidationErrorCode> errorCodes = validator.execute(ValidatePersonName.Request.of("#$sample"));

        assertThat(errorCodes).containsExactly(PERSON_NAME_HAS_INVALID_FORMAT);
    }

    @Test
    void execute_withValidName_returnsEmpty() {
        Set<ValidationErrorCode> errorCodes = validator.execute(ValidatePersonName.Request.of("a".repeat(1000)));

        assertThat(errorCodes).isEmpty();
    }
}
