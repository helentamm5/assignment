package com.example.assignment.appdomain.sanctionedperson.change;

import static com.example.assignment.appdomain.sanctionedperson.validation.ValidateChangePerson.SanctionedPersonChangeErrorCode.PERSON_CHANGE_DETAILS_ARE_MISSING;
import static com.example.assignment.appdomain.sanctionedperson.validation.ValidateSanctionedPersonCreation.PersonCreationErrorCode.PERSON_CREATION_DETAILS_ARE_MISSING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.assignment.appdomain.sanctionedperson.GetNormalizedName;
import com.example.assignment.appdomain.sanctionedperson.GetSanctionedPersonByIdPort;
import com.example.assignment.appdomain.sanctionedperson.SaveSanctionedPersonPort;
import com.example.assignment.appdomain.sanctionedperson.creation.CreateSanctionedPerson;
import com.example.assignment.appdomain.sanctionedperson.creation.SanctionedPersonCreationDetails;
import com.example.assignment.appdomain.sanctionedperson.exception.NoSuchPersonException;
import com.example.assignment.appdomain.sanctionedperson.model.SanctionedPerson;
import com.example.assignment.appdomain.sanctionedperson.validation.ValidateChangePerson;
import com.example.assignment.appdomain.sanctionedperson.validation.ValidateSanctionedPersonCreation;
import com.example.assignment.appdomain.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ChangeSanctionedPersonTest {

    private static final Long PERSON_ID = 1L;
    private static final String NAME = "Sample name";

    @Mock
    private SaveSanctionedPersonPort saveSanctionedPersonPort;
    @Mock
    private GetSanctionedPersonByIdPort getSanctionedPersonByIdPort;
    @Mock
    private ValidateChangePerson validateChangePerson;
    @Mock
    private GetNormalizedName getNormalizedName;

    private ChangeSanctionedPerson useCase;

    @BeforeEach
    void setUp() {
        useCase = new ChangeSanctionedPerson(getSanctionedPersonByIdPort, saveSanctionedPersonPort, validateChangePerson, getNormalizedName);
    }

    @Test
    void execute_withPersonIdNotPresent_throwsNoSuchPersonException() {
        when(getSanctionedPersonByIdPort.execute(GetSanctionedPersonByIdPort.Request.of(PERSON_ID)))
            .thenReturn(Optional.empty());

        assertThrows(NoSuchPersonException.class, () -> useCase.execute(ChangeSanctionedPerson.Request.builder()
            .personId(PERSON_ID)
            .changeDetails(SanctionedPersonChangeDetails.of(NAME))
            .build()));
    }

    @Test
    void execute_withValidationErrors_throwsValidationException() {
        when(validateChangePerson.execute(ValidateChangePerson.Request.of(SanctionedPersonChangeDetails.of(NAME))))
            .thenReturn(Set.of(PERSON_CHANGE_DETAILS_ARE_MISSING));
        when(getSanctionedPersonByIdPort.execute(GetSanctionedPersonByIdPort.Request.of(PERSON_ID)))
            .thenReturn(Optional.of(SanctionedPerson.builder()
                .build()));

        assertThrows(ValidationException.class, () -> useCase.execute(ChangeSanctionedPerson.Request.builder()
            .personId(PERSON_ID)
            .changeDetails(SanctionedPersonChangeDetails.of(NAME))
            .build()));
    }

    @Test
    void execute_withValidRequest_savesUpdatedPerson() {
        String name = "Sample name";
        String normalizedName = "Sample normalized name";
        when(getNormalizedName.execute(GetNormalizedName.Request.of(name))).thenReturn(normalizedName);
        when(validateChangePerson.execute(ValidateChangePerson.Request.of(SanctionedPersonChangeDetails.of(name))))
            .thenReturn(Set.of());
        when(getSanctionedPersonByIdPort.execute(GetSanctionedPersonByIdPort.Request.of(PERSON_ID)))
            .thenReturn(Optional.of(SanctionedPerson.builder()
                .id(PERSON_ID)
                .normalizedName("old normalized name")
                .name("old name")
                .isDeleted(false)
                .build()));

        useCase.execute(ChangeSanctionedPerson.Request.builder()
            .personId(PERSON_ID)
            .changeDetails(SanctionedPersonChangeDetails.of(NAME))
            .build());

        verify(saveSanctionedPersonPort).execute(SaveSanctionedPersonPort.Request.of(SanctionedPerson.builder()
            .id(PERSON_ID)
            .name(name)
            .normalizedName(normalizedName)
            .isDeleted(false)
            .build()));
    }
}
