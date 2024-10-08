package com.example.assignment.appdomain.sanctionedperson;

import com.example.assignment.appdomain.sanctionedperson.FindSanctionedPersonNamesPort.SanctionedNameDetails;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CheckSanctionedNameMatch {

    private static final LevenshteinDistance levenshtein = new LevenshteinDistance();
    private static final JaroWinklerSimilarity jaroWinkler = new JaroWinklerSimilarity();
    private static final double JARO_WINKLER_SIMILARITY_THRESHOLD = 0.8;
    private final FindSanctionedPersonNamesPort findSanctionedPersonNamesPort;
    private final GetNormalizedName getNormalizedName;

    public SanctionedPersonCheckResponse execute(Request request) {
        if (request.getName() == null) {
            return SanctionedPersonCheckResponse.of(false, null);
        }
        String nameToCheck = getNormalizedName.execute(GetNormalizedName.Request.of(request.getName()));
        Set<SanctionedNameDetails> normalizedSanctionedNames = findSanctionedPersonNamesPort.execute();

        String similarityMatch = getSimilarityMatch(normalizedSanctionedNames, nameToCheck);
        if (similarityMatch != null) {
            return SanctionedPersonCheckResponse.of(true, similarityMatch);
        }

        return SanctionedPersonCheckResponse.of(false, null);
    }

    private String getSimilarityMatch(Set<SanctionedNameDetails> sanctionedNameDetails, String nameToCheck) {
        for (SanctionedNameDetails nameDetails : sanctionedNameDetails) {
            String normalizedName = nameDetails.getNormalizedName() != null
                ? nameDetails.getNormalizedName()
                : getNormalizedName.execute(GetNormalizedName.Request.of(nameDetails.getName()));
            int distance = levenshtein.apply(normalizedName, nameToCheck);
            int threshold = (int) Math.ceil(Math.min(normalizedName.length(), nameToCheck.length()) * 0.25);

            if (distance <= threshold || jaroWinkler.apply(nameToCheck, normalizedName) > JARO_WINKLER_SIMILARITY_THRESHOLD) {
                return nameDetails.getName();
            }
        }
        return null;
    }

    @Value(staticConstructor = "of")
    public static class Request {
        String name;
    }

    @Value(staticConstructor = "of")
    public static class SanctionedPersonCheckResponse {
        boolean isNameSuspicious;
        String matchingSanctionedName;
    }
}

