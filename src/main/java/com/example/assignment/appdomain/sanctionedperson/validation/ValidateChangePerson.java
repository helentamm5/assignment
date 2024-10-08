package com.example.assignment.appdomain.sanctionedperson.validation;

import com.example.assignment.adapter.web.ValidationErrorCode;
import com.example.assignment.appdomain.sanctionedperson.change.SanctionedPersonChangeDetails;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidateChangePerson {

    private final ValidatePersonName validatePersonName;

    public Set<ValidationErrorCode> execute(Request request) {
        if (request.getChangeDetails() == null) {
            return Set.of(SanctionedPersonChangeErrorCode.PERSON_CHANGE_DETAILS_ARE_MISSING);
        }
        return new HashSet<>(validatePersonName.execute(ValidatePersonName.Request.of(request.getChangeDetails()
            .getName())));
    }

    @Value(staticConstructor = "of")
    public static class Request {
        SanctionedPersonChangeDetails changeDetails;
    }

    public enum SanctionedPersonChangeErrorCode implements ValidationErrorCode {
        PERSON_CHANGE_DETAILS_ARE_MISSING
    }
}
