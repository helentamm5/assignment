package com.example.assignment.appdomain.sanctionedperson;

import com.example.assignment.appdomain.sanctionedperson.model.SanctionedPerson;

import java.util.Set;

public interface FindAllSanctionedPeoplePort {

    Set<SanctionedPerson> execute();
}
