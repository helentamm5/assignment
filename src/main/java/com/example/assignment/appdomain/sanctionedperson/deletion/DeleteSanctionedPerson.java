package com.example.assignment.appdomain.sanctionedperson.deletion;

import com.example.assignment.appdomain.sanctionedperson.GetSanctionedPersonByIdPort;
import com.example.assignment.appdomain.sanctionedperson.SaveSanctionedPersonPort;
import com.example.assignment.appdomain.sanctionedperson.exception.NoSuchPersonException;
import com.example.assignment.appdomain.sanctionedperson.model.SanctionedPerson;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteSanctionedPerson {

    private final SaveSanctionedPersonPort saveSanctionedPersonPort;
    private final GetSanctionedPersonByIdPort getSanctionedPersonByIdPort;

    public void execute(Request request) {
        if (request.getPersonId() == null) {
            return;
        }
        SanctionedPerson existingPerson = getSanctionedPersonByIdPort.execute(GetSanctionedPersonByIdPort.Request.of(request.getPersonId()))
            .orElseThrow(NoSuchPersonException::new);
        saveSanctionedPersonPort.execute(SaveSanctionedPersonPort.Request.of(existingPerson.toBuilder()
            .isDeleted(true)
            .build()));
    }

    @Value(staticConstructor = "of")
    public static class Request {
        Long personId;
    }
}
