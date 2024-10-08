package com.example.assignment.adapter.database;

import com.example.assignment.appdomain.sanctionedperson.FindSanctionedPersonNamesPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FindSanctionedPersonNamesRepositoryAdapter implements FindSanctionedPersonNamesPort {

    private final SanctionedPersonRepository sanctionedPersonRepository;

    @Override
    public Set<SanctionedNameDetails> execute() {
        return sanctionedPersonRepository.findAllSanctionedNameDetailsByIsDeletedFalse().stream()
            .map(entity -> SanctionedNameDetails.of(entity.getName(), entity.getNormalizedName()))
            .collect(Collectors.toUnmodifiableSet());
    }
}
