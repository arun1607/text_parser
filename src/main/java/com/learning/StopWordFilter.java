package com.learning;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class StopWordFilter implements WordFilter {
    private Set<String> stopWords = new HashSet<>();

    public void init() throws ParsingException {
        try {
            String stopWordFileName = "stop_words.txt";
            Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(stopWordFileName)).toURI());
            stopWords = Files.readAllLines(path).stream().map(String::toLowerCase).collect(Collectors.toSet());
        } catch (IOException | URISyntaxException e) {
            throw new ParsingException("Error occurred in reading stop word file.");
        }
    }

    @Override
    public List<String> filter(List<String> words) {
        if (words == null) {
            throw new NullPointerException("word list cannot be null");
        }
        return words.stream().filter(str -> !stopWords.contains(str.toLowerCase())).collect(Collectors.toList());
    }
}
