package com.example.assignment.adapter.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.assignment.appdomain.sanctionedperson.exception.NoSuchPersonException;
import com.example.assignment.appdomain.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

class RestExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        var restExceptionHandler = new RestExceptionHandler();
        var dummyController = new DummyController();
        mockMvc = MockMvcBuilders.standaloneSetup(dummyController)
            .setControllerAdvice(restExceptionHandler)
            .build();
    }

    @Test
    void handleValidationException_withValidationException_shouldRespondWithBadRequestAndErrorCodes() throws Exception {
        mockMvc.perform(get("/validation-exception"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCodes.length()").value(1))
            .andExpect(jsonPath("$.errorCodes[0].name").value(ValidationErrors.INVALID_1.name()));
    }

    @Test
    void handleNoSuchPersonException_withNoSuchPersonException_shouldRespondWithUnprocessableEntity() throws Exception {
        mockMvc.perform(get("/no-such-person-exception"))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void handleDataIntegrityViolationException_withDataIntegrityViolationException_shouldRespondWithBadRequest() throws Exception {
        mockMvc.perform(get("/data-integrity-violation"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void handleException_withUnhandledException_shouldRespondWithInternalServerError() throws Exception {
        mockMvc.perform(get("/unhandled-exception"))
            .andExpect(status().isInternalServerError());
    }

    @RestController
    private static class DummyController {

        @GetMapping("/validation-exception")
        public void validationException() {
            throw ValidationException.of(Set.of(ValidationErrors.INVALID_1));
        }

        @GetMapping("/no-such-person-exception")
        public void noSuchPersonException() {
            throw new NoSuchPersonException();
        }

        @GetMapping("/data-integrity-violation")
        public void dataIntegrityViolation() {
            throw new DataIntegrityViolationException("Data integrity violation");
        }

        @GetMapping("/unhandled-exception")
        public void unhandledException() {
            throw new RuntimeException("Unexpected error");
        }
    }

    private enum ValidationErrors implements ValidationErrorCode {
        INVALID_1
    }
}
