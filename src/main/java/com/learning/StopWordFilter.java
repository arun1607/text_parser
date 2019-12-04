package com.learning;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class StopWordFilter {
    private Set<String> stopWords = new HashSet<>();
    private final String stopWordLocation;

    public StopWordFilter(String stopWordLocation) {
        if (StringUtils.isBlank(stopWordLocation)) {
            throw new IllegalArgumentException("Stop location cannot be null or empty");
        }
        this.stopWordLocation = stopWordLocation;
    }

    public void init() throws ParsingException {
        File stopWordFile = new File(stopWordLocation);
        if (!stopWordFile.exists()) {
            throw new ParsingException("Stop word file not found");
        }
        try {
            stopWords = Files.readAllLines(stopWordFile.toPath()).stream().map(String::toLowerCase).collect(Collectors.toSet());
        } catch (IOException e) {
            throw new ParsingException("Error occurred in reading stop word file.");
        }
    }

    public List<String> filterStopWords(String line, String wordSeparator) {
        return Arrays.stream(line.split(wordSeparator)).filter(str -> !stopWords.contains(str.toLowerCase())).collect(Collectors.toList());
    }
}
