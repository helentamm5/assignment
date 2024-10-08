package com.example.assignment.appdomain.sanctionedperson;

import lombok.Value;

import java.util.Set;

public interface FindSanctionedPersonNamesPort {

    Set<SanctionedNameDetails> execute();

    @Value(staticConstructor = "of")
    class SanctionedNameDetails {
        String name;
        String normalizedName;
    }
}
