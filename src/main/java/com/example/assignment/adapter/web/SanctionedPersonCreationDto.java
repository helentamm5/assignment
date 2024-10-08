package com.example.assignment.adapter.web;

import com.example.assignment.appdomain.sanctionedperson.creation.SanctionedPersonCreationDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SanctionedPersonCreationDto {
    String name;

    public SanctionedPersonCreationDetails toDomain() {
        return SanctionedPersonCreationDetails.of(getName());
    }
}
