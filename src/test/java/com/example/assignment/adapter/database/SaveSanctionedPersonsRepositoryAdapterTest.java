package com.example.assignment.adapter.database;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.assignment.appdomain.sanctionedperson.SaveSanctionedPersonPort;
import com.example.assignment.appdomain.sanctionedperson.model.SanctionedPerson;
import com.example.assignment.test.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SaveSanctionedPersonsRepositoryAdapterTest extends BaseIntegrationTest {

    private static final String SAMPLE_NAME = "Bob Bubble";
    private static final String SAMPLE_NORMALIZED_NAME = "bob bubble";

    @Autowired
    private SanctionedPersonRepository repository;

    @Autowired
    private SaveSanctionedPersonsRepositoryAdapter adapter;

    @Test
    void execute_withPersonToSaveNull_doesNotSavePerson() {
        adapter.execute(SaveSanctionedPersonPort.Request.of(null));

        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void execute_withValidPersonToSaveWithoutId_savesPerson() {
        SanctionedPerson personToSave = SanctionedPerson.builder()
            .name(SAMPLE_NAME)
            .normalizedName(SAMPLE_NORMALIZED_NAME)
            .build();

        adapter.execute(SaveSanctionedPersonPort.Request.of(personToSave));

        assertThat(repository.findAll())
            .singleElement()
            .satisfies(person -> {
                assertThat(person.getName()).isEqualTo(SAMPLE_NAME);
                assertThat(person.getNormalizedName()).isEqualTo(SAMPLE_NORMALIZED_NAME);
            });
    }

    @Test
    void execute_withValidPersonToSaveWithId_savesPerson() {
        long personId = repository.save(SanctionedPersonEntity.builder()
                .id(-10L)
                .name("Cecilia Cat")
                .normalizedName("cecilia cat")
            .build()).getId();
        SanctionedPerson personToSave = SanctionedPerson.builder()
            .id(personId)
            .name(SAMPLE_NAME)
            .normalizedName(SAMPLE_NORMALIZED_NAME)
            .isDeleted(false)
            .build();

        adapter.execute(SaveSanctionedPersonPort.Request.of(personToSave));

        assertThat(repository.findAll())
            .singleElement()
            .satisfies(person -> {
                assertThat(person.getId()).isEqualTo(personId);
                assertThat(person.getName()).isEqualTo(SAMPLE_NAME);
                assertThat(person.getNormalizedName()).isEqualTo(SAMPLE_NORMALIZED_NAME);
                assertThat(person.isDeleted()).isEqualTo(false);
            });
    }
}
