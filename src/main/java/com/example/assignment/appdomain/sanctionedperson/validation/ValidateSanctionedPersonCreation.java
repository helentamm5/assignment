package com.example.assignment.appdomain.sanctionedperson.validation;

import com.example.assignment.adapter.web.ValidationErrorCode;
import com.example.assignment.appdomain.sanctionedperson.creation.SanctionedPersonCreationDetails;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidateSanctionedPersonCreation {

    private final ValidatePersonName validatePersonName;

    public Set<ValidationErrorCode> execute(Request request) {
        if (request.getCreationDetails() == null) {
            return Set.of(PersonCreationErrorCode.PERSON_CREATION_DETAILS_ARE_MISSING);
        }
        return new HashSet<>(validatePersonName.execute(ValidatePersonName.Request.of(request.getCreationDetails()
            .getName())));
    }

    @Value(staticConstructor = "of")
    public static class Request {
        SanctionedPersonCreationDetails creationDetails;
    }

    public enum PersonCreationErrorCode implements ValidationErrorCode {
        PERSON_CREATION_DETAILS_ARE_MISSING
    }
}
