package com.learning;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

public class WordNormalizer {
    private Map<String, String> rules = new HashMap<>();
    private final String normalizationRuleLocation;

    public WordNormalizer(String normalizationRuleLocation) {
        if (StringUtils.isBlank(normalizationRuleLocation)) {
            throw new IllegalArgumentException("Normalization rule location cannot be null or empty");
        }
        this.normalizationRuleLocation = normalizationRuleLocation;
    }

    public void init() throws ParsingException {
        File normalizationRuleFile = new File(normalizationRuleLocation);
        if (!normalizationRuleFile.exists()) {
            throw new ParsingException("Normalization rule file not found");
        }
        try {
            Files.readAllLines(normalizationRuleFile.toPath()).stream().filter(StringUtils::isNoneBlank).map(str -> str.trim().toLowerCase()).forEach(str -> {
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
        } catch (IOException e) {
            throw new ParsingException("Error occurred in reading Normalization rule file.");
        }
    }

    public String normalize(String word) {
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
