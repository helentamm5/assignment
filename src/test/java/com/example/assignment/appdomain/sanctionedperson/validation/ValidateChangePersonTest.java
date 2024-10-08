package com.example.assignment.appdomain.sanctionedperson.validation;

import static com.example.assignment.appdomain.sanctionedperson.validation.ValidateChangePerson.SanctionedPersonChangeErrorCode.PERSON_CHANGE_DETAILS_ARE_MISSING;
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
class ValidateChangePersonTest {

    @Mock
    private ValidatePersonName validatePersonName;

    private ValidateChangePerson validator;

    @BeforeEach
    void setUp() {
        validator = new ValidateChangePerson(validatePersonName);
    }

    @Test
    void execute_withMissingChangeDetails_returnsPersonChangeDetailsAreMissingErrorCode() {
        Set<ValidationErrorCode> errorCodes = validator.execute(ValidateChangePerson.Request.of(null));

        assertThat(errorCodes).containsExactly(PERSON_CHANGE_DETAILS_ARE_MISSING);
    }

    @Test
    void execute_withNameValidationError_returns() {
        String name = "";
        when(validatePersonName.execute(ValidatePersonName.Request.of(name))).thenReturn(Set.of(PERSON_NAME_IS_EMPTY));

        Set<ValidationErrorCode> errorCodes = validator.execute(ValidateChangePerson.Request.of(SanctionedPersonChangeDetails.of(name)));

        assertThat(errorCodes).containsExactly(PERSON_NAME_IS_EMPTY);
    }

    @Test
    void execute_withValidRequest_returnsEmpty() {
        Set<ValidationErrorCode> errorCodes = validator.execute(ValidateChangePerson.Request.of(SanctionedPersonChangeDetails.of("test")));

        assertThat(errorCodes).isEmpty();
    }
}
