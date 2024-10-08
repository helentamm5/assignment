package com.example.assignment.adapter.database;

import com.example.assignment.appdomain.sanctionedperson.SaveSanctionedPersonPort;
import com.example.assignment.appdomain.sanctionedperson.model.SanctionedPerson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveSanctionedPersonsRepositoryAdapter implements SaveSanctionedPersonPort {

    private final SanctionedPersonRepository sanctionedPersonRepository;

    @Override
    public void execute(SaveSanctionedPersonPort.Request request) {
        saveSanctionedPerson(request.getSanctionedPerson());
    }

    private void saveSanctionedPerson(SanctionedPerson person) {
        if (person == null) {
            return;
        }
        sanctionedPersonRepository.save(SanctionedPersonEntity.builder()
            .id(person.getId())
            .name(person.getName())
            .normalizedName(person.getNormalizedName())
            .isDeleted(person.isDeleted())
            .build());
    }
}
