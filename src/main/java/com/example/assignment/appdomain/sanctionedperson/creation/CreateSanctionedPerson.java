package com.example.assignment.appdomain.sanctionedperson.creation;

import com.example.assignment.adapter.web.ValidationErrorCode;
import com.example.assignment.appdomain.sanctionedperson.GetNormalizedName;
import com.example.assignment.appdomain.sanctionedperson.SaveSanctionedPersonPort;
import com.example.assignment.appdomain.sanctionedperson.model.SanctionedPerson;
import com.example.assignment.appdomain.sanctionedperson.validation.ValidateSanctionedPersonCreation;
import com.example.assignment.appdomain.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateSanctionedPerson {

    private final SaveSanctionedPersonPort saveSanctionedPersonPort;
    private final ValidateSanctionedPersonCreation validateSanctionedPersonCreation;
    private final GetNormalizedName getNormalizedName;

    public void execute(Request request) {
        validateCreation(request);
        saveSanctionedPersonPort.execute(SaveSanctionedPersonPort.Request.of(SanctionedPerson.builder()
            .name(request.getCreationDetails().getName())
            .normalizedName(getNormalizedName.execute(GetNormalizedName.Request.of(request.getCreationDetails().getName())))
            .build()));
    }

    private void validateCreation(Request request) {
        Set<ValidationErrorCode> validationErrors = validateSanctionedPersonCreation.execute(
            ValidateSanctionedPersonCreation.Request.of(request.getCreationDetails()));
        if (!validationErrors.isEmpty()) {
            throw ValidationException.of(validationErrors);
        }
    }

    @Value(staticConstructor = "of")
    public static class Request {
        SanctionedPersonCreationDetails creationDetails;
    }
}
