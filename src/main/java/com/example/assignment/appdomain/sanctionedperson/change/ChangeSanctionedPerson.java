package com.example.assignment.appdomain.sanctionedperson.change;

import com.example.assignment.adapter.web.ValidationErrorCode;
import com.example.assignment.appdomain.sanctionedperson.GetNormalizedName;
import com.example.assignment.appdomain.sanctionedperson.GetSanctionedPersonByIdPort;
import com.example.assignment.appdomain.sanctionedperson.SaveSanctionedPersonPort;
import com.example.assignment.appdomain.sanctionedperson.exception.NoSuchPersonException;
import com.example.assignment.appdomain.sanctionedperson.model.SanctionedPerson;
import com.example.assignment.appdomain.sanctionedperson.validation.ValidateChangePerson;
import com.example.assignment.appdomain.validation.ValidationException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ChangeSanctionedPerson {

    private final GetSanctionedPersonByIdPort getSanctionedPersonByIdPort;
    private final SaveSanctionedPersonPort saveSanctionedPersonPort;
    private final ValidateChangePerson validateChangePerson;
    private final GetNormalizedName getNormalizedName;

    public void execute(Request request) {
        SanctionedPerson existingPerson = getSanctionedPersonByIdPort.execute(GetSanctionedPersonByIdPort.Request.of(request.getPersonId()))
            .orElseThrow(NoSuchPersonException::new);

        validateChange(request);

        SanctionedPerson updatedPerson = updateSanctionedPerson(existingPerson, request.getChangeDetails());
        saveSanctionedPersonPort.execute(SaveSanctionedPersonPort.Request.of(updatedPerson));
    }

    private void validateChange(Request request) {
        Set<ValidationErrorCode> validationErrors = validateChangePerson.execute(ValidateChangePerson.Request.of(request.getChangeDetails()));
        if (!validationErrors.isEmpty()) {
            throw ValidationException.of(validationErrors);
        }
    }

    private SanctionedPerson updateSanctionedPerson(SanctionedPerson existingPerson, SanctionedPersonChangeDetails changeDetails) {
        return existingPerson.toBuilder()
            .name(changeDetails.getName())
            .normalizedName(getNormalizedName.execute(GetNormalizedName.Request.of(changeDetails.getName())))
            .build();
    }

    @Value
    @Builder
    public static class Request {
        Long personId;
        SanctionedPersonChangeDetails changeDetails;
    }
}
