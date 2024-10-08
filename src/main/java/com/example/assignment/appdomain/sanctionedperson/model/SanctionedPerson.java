package com.example.assignment.appdomain.sanctionedperson.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class SanctionedPerson {
    Long id;
    String name;
    String normalizedName;
    boolean isDeleted;
}
