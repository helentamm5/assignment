package com.example.assignment.appdomain.sanctionedperson.creation;

import static com.example.assignment.appdomain.sanctionedperson.validation.ValidateSanctionedPersonCreation.PersonCreationErrorCode.PERSON_CREATION_DETAILS_ARE_MISSING;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.assignment.appdomain.sanctionedperson.GetNormalizedName;
import com.example.assignment.appdomain.sanctionedperson.SaveSanctionedPersonPort;
import com.example.assignment.appdomain.sanctionedperson.model.SanctionedPerson;
import com.example.assignment.appdomain.sanctionedperson.validation.ValidateSanctionedPersonCreation;
import com.example.assignment.appdomain.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
class CreateSanctionedPersonTest {

    @Mock
    private SaveSanctionedPersonPort saveSanctionedPersonPort;
    @Mock
    private ValidateSanctionedPersonCreation validateSanctionedPersonCreation;
    @Mock
    private GetNormalizedName getNormalizedName;

    private CreateSanctionedPerson useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateSanctionedPerson(saveSanctionedPersonPort, validateSanctionedPersonCreation, getNormalizedName);
    }

    @Test
    void execute_withValidRequest_savesPersonWithNormalizedName() {
        String name = "Sample name";
        String normalizedName = "Sample normalized name";
        SanctionedPersonCreationDetails creationDetails = SanctionedPersonCreationDetails.of(name);
        when(getNormalizedName.execute(GetNormalizedName.Request.of(name))).thenReturn(normalizedName);
        when(validateSanctionedPersonCreation.execute(ValidateSanctionedPersonCreation.Request.of(creationDetails)))
            .thenReturn(Set.of());

        useCase.execute(CreateSanctionedPerson.Request.of(creationDetails));

        verify(saveSanctionedPersonPort).execute(SaveSanctionedPersonPort.Request.of(SanctionedPerson.builder()
            .name(name)
            .normalizedName(normalizedName)
            .build()));
    }

    @Test
    void execute_withValidationErrors_throwsValidationException() {
        String name = "";
        SanctionedPersonCreationDetails creationDetails = SanctionedPersonCreationDetails.of(name);
        when(validateSanctionedPersonCreation.execute(ValidateSanctionedPersonCreation.Request.of(creationDetails)))
            .thenReturn(Set.of(PERSON_CREATION_DETAILS_ARE_MISSING));

        assertThrows(ValidationException.class, () -> useCase.execute(CreateSanctionedPerson.Request.of(creationDetails)));
    }
}
