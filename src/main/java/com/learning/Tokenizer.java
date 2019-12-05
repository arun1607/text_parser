package com.learning;

import java.util.List;

public interface Tokenizer {
    List<String> tokenize(final String input, final String separator);
}
