package com.learning;

import java.util.List;

public interface WordFilter {

    List<String> filter(final List<String> words);

    void init() throws ParsingException;
}
