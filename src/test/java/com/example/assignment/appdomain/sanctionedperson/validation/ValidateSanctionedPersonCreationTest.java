package com.example.assignment.appdomain.sanctionedperson.validation;

import static com.example.assignment.appdomain.sanctionedperson.validation.ValidatePersonName.PersonNameChangeErrorCode.PERSON_NAME_IS_EMPTY;
import static com.example.assignment.appdomain.sanctionedperson.validation.ValidateSanctionedPersonCreation.PersonCreationErrorCode.PERSON_CREATION_DETAILS_ARE_MISSING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.assignment.adapter.web.ValidationErrorCode;
import com.example.assignment.appdomain.sanctionedperson.change.SanctionedPersonChangeDetails;
import com.example.assignment.appdomain.sanctionedperson.creation.SanctionedPersonCreationDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ValidateSanctionedPersonCreationTest {

    @Mock
    private ValidatePersonName validatePersonName;

    private ValidateSanctionedPersonCreation validator;

    @BeforeEach
    void setUp() {
        validator = new ValidateSanctionedPersonCreation(validatePersonName);
    }

    @Test
    void execute_withMissingCreationDetails_returnsPersonCreationDetailsAreMissingErrorCode() {
        Set<ValidationErrorCode> errorCodes = validator.execute(ValidateSanctionedPersonCreation.Request.of(null));

        assertThat(errorCodes).containsExactly(PERSON_CREATION_DETAILS_ARE_MISSING);
    }

    @Test
    void execute_withNameValidationError_returns() {
        String name = "";
        when(validatePersonName.execute(ValidatePersonName.Request.of(name))).thenReturn(Set.of(PERSON_NAME_IS_EMPTY));

        Set<ValidationErrorCode> errorCodes =
            validator.execute(ValidateSanctionedPersonCreation.Request.of(SanctionedPersonCreationDetails.of(name)));

        assertThat(errorCodes).containsExactly(PERSON_NAME_IS_EMPTY);
    }

    @Test
    void execute_withValidRequest_returnsEmpty() {
        Set<ValidationErrorCode> errorCodes =
            validator.execute(ValidateSanctionedPersonCreation.Request.of(SanctionedPersonCreationDetails.of("test")));

        assertThat(errorCodes).isEmpty();
    }
}
