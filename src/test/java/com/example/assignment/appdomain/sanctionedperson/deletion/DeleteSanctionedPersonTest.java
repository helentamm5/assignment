package com.example.assignment.appdomain.sanctionedperson.deletion;

import static org.mockito.Mockito.verifyNoInteractions;

import com.example.assignment.appdomain.sanctionedperson.GetSanctionedPersonByIdPort;
import com.example.assignment.appdomain.sanctionedperson.SaveSanctionedPersonPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteSanctionedPersonTest {

    @Mock
    private SaveSanctionedPersonPort saveSanctionedPersonPort;
    @Mock
    private GetSanctionedPersonByIdPort getSanctionedPersonByIdPort;

    private DeleteSanctionedPerson useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteSanctionedPerson(saveSanctionedPersonPort, getSanctionedPersonByIdPort);
    }

    @Test
    void execute_withNullPersonId_doesNotRetrievePerson() {
        useCase.execute(DeleteSanctionedPerson.Request.of(null));

        verifyNoInteractions(getSanctionedPersonByIdPort);
    }

    @Test
    void execute_withPersonIdNull_doesNotDeleteSanctionedPerson() {
        useCase.execute(DeleteSanctionedPerson.Request.of(null));

        verifyNoInteractions(saveSanctionedPersonPort);
    }
}
