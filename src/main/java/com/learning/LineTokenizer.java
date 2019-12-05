package com.learning;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class LineTokenizer implements Tokenizer {
    /**
     * @see Tokenizer#tokenize(String, String)
     */
    public List<String> tokenize(final String input, final String separator) {
        if (StringUtils.isNotBlank(separator)) {
            return Arrays.stream(input.split(separator)).filter(StringUtils::isNotBlank).map(String::trim).map(String::toLowerCase).collect(Collectors.toList());
        } else {
            final String separatorToUse = "[?.]";
            return Arrays.stream(input.split(separatorToUse)).filter(StringUtils::isNotBlank).map(String::trim).map(String::toLowerCase).collect(Collectors.toList());
        }
    }
}
