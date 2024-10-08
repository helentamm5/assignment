package com.example.assignment.appdomain.sanctionedperson.creation;

import lombok.Value;

@Value(staticConstructor = "of")
public class SanctionedPersonCreationDetails {
    String name;
}
