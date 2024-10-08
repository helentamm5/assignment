package com.example.assignment.appdomain.sanctionedperson;

import com.example.assignment.appdomain.sanctionedperson.model.SanctionedPerson;
import lombok.Value;

public interface SaveSanctionedPersonPort {

    void execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        SanctionedPerson sanctionedPerson;
    }
}
