package com.example.assignment.adapter.database;

import com.example.assignment.appdomain.sanctionedperson.FindAllSanctionedPeoplePort;
import com.example.assignment.appdomain.sanctionedperson.model.SanctionedPerson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FindAllSanctionedPeoplePortAdapter implements FindAllSanctionedPeoplePort {

    private final SanctionedPersonRepository sanctionedPersonRepository;

    @Override
    public Set<SanctionedPerson> execute() {
        return sanctionedPersonRepository.findAllByIsDeletedFalse().stream()
            .map(SanctionedPersonEntity::toDomain)
            .collect(Collectors.toUnmodifiableSet());
    }
}
