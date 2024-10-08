package com.example.assignment.appdomain.sanctionedperson;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.assignment.appdomain.sanctionedperson.CheckSanctionedNameMatch.SanctionedPersonCheckResponse;
import com.example.assignment.test.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

class CheckSanctionedNameMatchIntegrationTest extends BaseIntegrationTest {

    private static final String SANCTIONED_NAME = "Osama Bin Laden";

    @Autowired
    private CheckSanctionedNameMatch useCase;

    @Test
    void execute_withNameNull_returnsFalse() {
        SanctionedPersonCheckResponse result = useCase.execute(CheckSanctionedNameMatch.Request.of(null));

        assertThat(result).extracting(SanctionedPersonCheckResponse::isNameSuspicious).isEqualTo(false);
    }

    @ParameterizedTest
    @CsvSource({
        "Osama Bin Laden",
        "'Bin Laden, Osama'",
        "Laden Osama Bin",
        "to the osama bin laden",
        "osama and bin laden",
        "Osama bin Ladin",
        "O. Bin Ladan",
        "Usama Bin Ladin",
        "Osama Laden",
        "Bin Laden",
        "Ben Osama Ladn"
    })
    @Sql("/testdata/match_sanctioned_person_data.sql")
    void execute_withSanctionedName_returnsTrueAndNameMatch(String name) {
        SanctionedPersonCheckResponse result = useCase.execute(CheckSanctionedNameMatch.Request.of(name));

        assertThat(result).extracting(SanctionedPersonCheckResponse::isNameSuspicious).isEqualTo(true);
        assertThat(result).extracting(SanctionedPersonCheckResponse::getMatchingSanctionedName).isEqualTo(SANCTIONED_NAME);
    }

    @ParameterizedTest
    @CsvSource({
        "Ben Watch",
        "Well hello",
        "Osama False alarm",
        "Ozam Lezin Ven",
        "Osama Smith",
        "Bin Smith Osman"
    })
    @Sql("/testdata/match_sanctioned_person_data.sql")
    void execute_withPartialMatches_returnsFalseAndNameMatchNull(String name) {
        SanctionedPersonCheckResponse result = useCase.execute(CheckSanctionedNameMatch.Request.of(name));

        assertThat(result).extracting(SanctionedPersonCheckResponse::isNameSuspicious).isEqualTo(false);
        assertThat(result).extracting(SanctionedPersonCheckResponse::getMatchingSanctionedName).isNull();
    }
}
