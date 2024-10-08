package com.example.assignment.adapter.database;

import com.example.assignment.appdomain.sanctionedperson.GetSanctionedPersonByIdPort;
import com.example.assignment.appdomain.sanctionedperson.model.SanctionedPerson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetSanctionedPersonByIdAdapter implements GetSanctionedPersonByIdPort {

    private final SanctionedPersonRepository sanctionedPersonRepository;

    @Override
    public Optional<SanctionedPerson> execute(Request request) {
        if (request.getPersonId() == null) {
            return Optional.empty();
        }
        return sanctionedPersonRepository.findById(request.getPersonId())
            .map(SanctionedPersonEntity::toDomain);
    }
}
