package com.example.assignment.adapter.web;

import com.example.assignment.appdomain.sanctionedperson.CheckSanctionedNameMatch;
import com.example.assignment.appdomain.sanctionedperson.change.ChangeSanctionedPerson;
import com.example.assignment.appdomain.sanctionedperson.creation.CreateSanctionedPerson;
import com.example.assignment.appdomain.sanctionedperson.deletion.DeleteSanctionedPerson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sanctioned-person")
public class SanctionedPersonController {

    private final CreateSanctionedPerson createSanctionedPerson;
    private final ChangeSanctionedPerson changeSanctionedPerson;
    private final DeleteSanctionedPerson deleteSanctionedPerson;
    private final CheckSanctionedNameMatch checkSanctionedNameMatch;

    @PostMapping("/check")
    public SanctionedPersonCheckResponseDto validate(@RequestBody SanctionedPersonCheckRequestDto request) {
        return SanctionedPersonCheckResponseDto.of(
            checkSanctionedNameMatch.execute(CheckSanctionedNameMatch.Request.of(request.getName()))
        );
    }

    @PostMapping
    public void create(@RequestBody SanctionedPersonCreationDto sanctionedPersonCreationDto) {
        createSanctionedPerson.execute(CreateSanctionedPerson.Request.of(sanctionedPersonCreationDto.toDomain()));
    }

    @PutMapping("/{id}")
    public void change(@PathVariable Long id,
                       @RequestBody SanctionedPersonChangeDto sanctionedPersonChangeDto) {
        changeSanctionedPerson.execute(ChangeSanctionedPerson.Request.builder()
            .personId(id)
            .changeDetails(sanctionedPersonChangeDto.toDomain())
            .build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deleteSanctionedPerson.execute(DeleteSanctionedPerson.Request.of(id));
    }
}
