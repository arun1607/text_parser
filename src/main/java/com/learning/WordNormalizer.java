package com.learning;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

public class WordNormalizer implements WordFilter {
    private Map<String, String> rules = new HashMap<>();

    @Override
    public List<String> filter(List<String> words) {
        if (words == null) {
            throw new NullPointerException("Word list cannot be null");
        }
        return words.stream().map(this::normalize).collect(Collectors.toList());
    }

    public void init() throws ParsingException {
        String normalizationRuleLocation = "normalization_rule.txt";
        try {
            Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(normalizationRuleLocation)).toURI());
            Files.readAllLines(path).stream().filter(StringUtils::isNoneBlank).map(str -> str.trim().toLowerCase()).forEach(str -> {
                final String[] parts = str.split("=>");

                if (parts.length == 2 && isNoneBlank(parts[0]) && isNoneBlank(parts[1])) {
                    final String key = parts[0].trim();
                    final String val = parts[1].trim();
                    rules.put(key, val);
                }
                if (parts.length == 1 && isNoneBlank(parts[0])) {
                    final String key = parts[0].trim();
                    final String val = "";
                    rules.put(key, val);
                }
            });
        } catch (IOException | URISyntaxException e) {
            throw new ParsingException("Error occurred in reading Normalization rule file.");
        }
    }

    public String normalize(String word) {
        if (StringUtils.isBlank(word)) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        String result = word.toLowerCase();
        String temp;
        for (int i = 1; i <= word.length() - 1; i++) {
            temp = StringUtils.right(word, (word.length() - i));
            if (rules.containsKey(temp)) {
                result = StringUtils.left(word, i) + rules.get(temp);
                break;
            }
        }
        return result.toLowerCase();
    }
}
