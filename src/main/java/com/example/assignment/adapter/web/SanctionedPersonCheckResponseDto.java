package com.example.assignment.adapter.web;

import com.example.assignment.appdomain.sanctionedperson.CheckSanctionedNameMatch.SanctionedPersonCheckResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SanctionedPersonCheckResponseDto {
    boolean hasSuspiciousName;
    String matchingName;

    public static SanctionedPersonCheckResponseDto of(SanctionedPersonCheckResponse sanctionedPersonCheckResponse) {
        return SanctionedPersonCheckResponseDto.builder()
            .hasSuspiciousName(sanctionedPersonCheckResponse.isNameSuspicious())
            .matchingName(sanctionedPersonCheckResponse.getMatchingSanctionedName())
            .build();
    }
}
