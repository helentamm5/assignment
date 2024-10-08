package com.example.assignment.appdomain.sanctionedperson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetNormalizedNameTest {

    private GetNormalizedName useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetNormalizedName();
    }

    @Test
    void execute_withMissingName_returnsNull() {
        assertNull(useCase.execute(GetNormalizedName.Request.of(null)));
    }

    @Test
    void execute_withMultipleNamePartsSeparatedByWhitespaces_returnsLowercasedNamePartsAlphabeticallyOrdered() {
        String result = useCase.execute(GetNormalizedName.Request.of("Osama Bin Laden"));

        assertThat(result).isEqualTo("bin laden osama");
    }

    @Test
    void execute_withEnglishNoiseWords_returnsLowercasedNamePartsAlphabeticallyOrderedAndNoiseWordsRemoved() {
        String result = useCase.execute(GetNormalizedName.Request.of("Osama The And Bin Laden"));

        assertThat(result).isEqualTo("bin laden osama");
    }
}
