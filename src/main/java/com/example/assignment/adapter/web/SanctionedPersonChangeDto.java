package com.example.assignment.adapter.web;

import com.example.assignment.appdomain.sanctionedperson.change.SanctionedPersonChangeDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SanctionedPersonChangeDto {
    String name;

    public SanctionedPersonChangeDetails toDomain() {
        return SanctionedPersonChangeDetails.of(getName());
    }
}
