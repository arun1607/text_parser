package com.learning;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class LineTokenizer {
    private final String separator;

    public LineTokenizer(String separator) {
        if (separator == null) {
            throw new NullPointerException("Separator can not be null");
        }
        this.separator = separator;
    }

    public List<String> tokenize(String input) {
        return Arrays.stream(input.split(separator)).filter(StringUtils::isNotBlank).map(String::trim).collect(Collectors.toList());
    }
}
