package com.learning;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Word {
    private final String key;
    private Set<Sentence> sentences = new HashSet<>();

    public Word(final String key) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        this.key = key;
    }

    public void addLine(final Sentence sentence) {
        if (sentence == null) {
            throw new NullPointerException("Sentence cannot be null");
        }
        sentences.add(sentence);
    }

    public Set<Sentence> getSentences() {
        return Collections.unmodifiableSet(sentences);
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "Word{" +
                "key='" + key + '\'' +
                '}';
    }
}
