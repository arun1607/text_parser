package com.learning;

import java.util.*;
import java.util.stream.Collectors;

public final class Document {
    private final String key;
    private final Set<String> sentences = new HashSet<>();

    public Document(String key) {
        this.key = key;
    }

    public void addSentence(final String sentence) {
        sentences.add(sentence);
    }

    public Set<String> getSentencesForKey(final String key) {
        return sentences.stream().filter(str -> str.contains(key)).collect(Collectors.toSet());
    }

    public Map<String, Integer> calculateWeight(final String key) {
        Map<String, Integer> weightMap = new HashMap<>();
        sentences.forEach(str -> {
            int counter = 0;
            for (int idx = 0; (idx = str.indexOf(key, idx)) >= 0; idx++) {
                counter++;
            }
            weightMap.put(str, counter);
        });
        return weightMap;
    }

    public String getKey() {
        return key;
    }

    public Set<String> getSentences() {
        return Collections.unmodifiableSet(sentences);
    }

}
