package com.example.assignment.appdomain.sanctionedperson;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetNormalizedName {

    public String execute(Request request) {
        if (request.getName() == null) {
            return null;
        }
        return normalizeAndSortParts(request.getName());
    }

    private String removeNoiseWords(String name) {
        try (Analyzer analyzer = new EnglishAnalyzer()) {
            TokenStream tokenStream = analyzer.tokenStream(null, new StringReader(name));
            CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();

            StringBuilder result = new StringBuilder();
            while (tokenStream.incrementToken()) {
                result.append(charTermAttribute.toString()).append(" ");
            }
            tokenStream.end();
            tokenStream.close();

            return result.toString().toLowerCase().trim();
        } catch (IOException e) {
            log.warn("Error during noise word removal: " + e.getMessage());
            return null;
        }
    }

    private String normalizeAndSortParts(String name) {
        String cleanedName = removeNoiseWords(name).toLowerCase();
        String[] nameParts = cleanedName.split("\\s+");
        Arrays.sort(nameParts);
        return String.join(" ", nameParts).trim();
    }

    @Value(staticConstructor = "of")
    public static class Request {
        String name;
    }
}
