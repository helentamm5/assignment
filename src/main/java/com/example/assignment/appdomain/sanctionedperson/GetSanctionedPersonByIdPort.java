package com.example.assignment.appdomain.sanctionedperson;

import com.example.assignment.appdomain.sanctionedperson.model.SanctionedPerson;
import lombok.Value;

import java.util.Optional;

public interface GetSanctionedPersonByIdPort {

    Optional<SanctionedPerson> execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        Long personId;
    }
}
