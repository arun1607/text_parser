package com.learning;

import java.util.List;

public interface Tokenizer {
    /**
     * Splits {@code input} using give {@code separator}.
     *
     * @param input     The string which need to be split.
     * @param separator The string used to split given input.
     * @return List containing part of original string.
     */
    List<String> tokenize(final String input, final String separator);
}
