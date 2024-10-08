package com.example.assignment.adapter.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.assignment.appdomain.sanctionedperson.FindAllSanctionedPeoplePort;
import com.example.assignment.appdomain.sanctionedperson.GetSanctionedPersonByIdPort;
import com.example.assignment.appdomain.sanctionedperson.model.SanctionedPerson;
import com.example.assignment.test.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

class SanctionedPersonControllerTest extends BaseIntegrationTest {
    private static final String BASE_URL = "/api/sanctioned-person";
    private static final String SAMPLE_NAME = "Sim Sa La Bim";
    private static final String SAMPLE_NAME_WITH_NOISE_WORD = "Bob The Builder";
    private static final long SAMPLE_ID = 1L;

    @Autowired
    GetSanctionedPersonByIdPort getSanctionedPersonByIdPort;
    @Autowired
    FindAllSanctionedPeoplePort findAllSanctionedPeoplePort;

    @Test
    @Sql("/testdata/sanctioned_person_data.sql")
    void check_withMatchingName_returnsTrueWithMatchingName() throws Exception {
        SanctionedPersonCheckRequestDto sanctionedPersonCreationDto = SanctionedPersonCheckRequestDto.builder()
            .name(SAMPLE_NAME_WITH_NOISE_WORD)
            .build();

        String jsonRequest = objectMapper.writeValueAsString(sanctionedPersonCreationDto);

        MvcResult result = mockMvc.perform(post(BASE_URL + "/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isOk())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        SanctionedPersonCheckResponseDto responseDto = objectMapper.readValue(responseBody, SanctionedPersonCheckResponseDto.class);

        assertTrue(responseDto.isHasSuspiciousName());
    }

    @Test
    @Sql("/testdata/sanctioned_person_data.sql")
    void check_withoutMatchingName_returnsFalseAndNameMatchNull() throws Exception {
        SanctionedPersonCheckRequestDto sanctionedPersonCreationDto = SanctionedPersonCheckRequestDto.builder()
            .name("Bibby the Bubbly")
            .build();

        String jsonRequest = objectMapper.writeValueAsString(sanctionedPersonCreationDto);

        MvcResult result = mockMvc.perform(post(BASE_URL + "/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isOk())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        SanctionedPersonCheckResponseDto responseDto = objectMapper.readValue(responseBody, SanctionedPersonCheckResponseDto.class);

        assertFalse(responseDto.isHasSuspiciousName());
    }

    @Test
    void create_withValidRequest_savesSanctionedPerson() throws Exception {
        SanctionedPersonCreationDto sanctionedPersonCreationDto = SanctionedPersonCreationDto.builder()
            .name(SAMPLE_NAME)
            .build();

        String jsonRequest = objectMapper.writeValueAsString(sanctionedPersonCreationDto);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isOk());

        assertThat(findAllSanctionedPeoplePort.execute())
            .extracting(SanctionedPerson::getName)
            .contains(SAMPLE_NAME);
    }

    @Test
    void create_withValidRequest_savesSanctionedPersonWithNormalizedName() throws Exception {
        SanctionedPersonCreationDto sanctionedPersonCreationDto = SanctionedPersonCreationDto.builder()
            .name(SAMPLE_NAME_WITH_NOISE_WORD)
            .build();

        String jsonRequest = objectMapper.writeValueAsString(sanctionedPersonCreationDto);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isOk());

        assertThat(findAllSanctionedPeoplePort.execute())
            .extracting(SanctionedPerson::getNormalizedName)
            .contains("bob builder");
    }

    @Test
    void create_withEmptyName_returnsBadRequest() throws Exception {
        SanctionedPersonCreationDto sanctionedPersonCreationDto = SanctionedPersonCreationDto.builder()
            .name("")
            .build();

        String jsonRequest = objectMapper.writeValueAsString(sanctionedPersonCreationDto);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isBadRequest());
    }


    @Test
    @Sql("/testdata/sanctioned_person_data.sql")
    void change_withPersonPresent_changesPersonName() throws Exception {
        SanctionedPersonChangeDto sanctionedPersonChangeDto = SanctionedPersonChangeDto.builder()
            .name(SAMPLE_NAME)
            .build();

        String jsonRequest = objectMapper.writeValueAsString(sanctionedPersonChangeDto);

        mockMvc.perform(put(BASE_URL + "/" + SAMPLE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isOk());

        assertThat(getSanctionedPersonByIdPort.execute(GetSanctionedPersonByIdPort.Request.of(SAMPLE_ID)))
            .isPresent()
            .hasValueSatisfying(person -> assertThat(person.getName()).isEqualTo(SAMPLE_NAME));
    }

    @Test
    @Sql("/testdata/sanctioned_person_data.sql")
    void delete_withPersonPresent_marksEntityAsDeleted() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + SAMPLE_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        assertThat(getSanctionedPersonByIdPort.execute(GetSanctionedPersonByIdPort.Request.of(SAMPLE_ID)))
            .isPresent()
            .hasValueSatisfying(person -> assertTrue(person.isDeleted()));
    }

    @Test
    void delete_withPersonIdNotPresent_returnsHttpStatusUnprocessableEntity() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + SAMPLE_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());
    }
}
