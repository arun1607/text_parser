package com.learning;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Sentence {

    private final String line;
    private final List<String> words;
    private final Map<String, Integer> weightMap = new HashMap<>();

    public Sentence(final String line, final List<String> words) {
        if (StringUtils.isBlank(line)) {
            throw new IllegalArgumentException("Line cannot be null or empty");
        }
        if (words == null) {
            throw new NullPointerException("Word list cannot be null");
        }
        this.line = line;
        this.words = words;
    }

    public void calculateWeight() {
        words.forEach(str -> {
            int counter = 0;
            for (int idx = 0; (idx = line.indexOf(str, idx)) >= 0; idx++) {
                counter++;
            }
            weightMap.put(str, counter);
        });

    }

    public Integer getWeightForKey(final String key) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        return weightMap.get(key);
    }

    public String getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "line='" + line + '\'' +
                '}';
    }
}
