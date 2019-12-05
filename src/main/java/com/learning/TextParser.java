package com.learning;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextParser {

    private final Tokenizer lineTokenizer;
    private final List<WordFilter> filterList;
    private final Map<String, Word> wordMap = new HashMap<>();

    public TextParser(Tokenizer lineTokenizer, List<WordFilter> filterList) {
        if (lineTokenizer == null) {
            throw new NullPointerException("line tokenizer cannot be null");
        }
        if (filterList == null) {
            throw new NullPointerException("Filter list cannot be null");
        }
        this.lineTokenizer = lineTokenizer;
        this.filterList = filterList;
    }

    public void init() throws ParsingException {
        for (WordFilter filter : filterList) {
            filter.init();
        }
    }

    public void parse(final String input, final Tokenizer wordTokenizer) {
        if (StringUtils.isBlank(input)) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        if (wordTokenizer == null) {
            throw new NullPointerException("Word tokenizer cannot be null");
        }
        final List<String> lines = lineTokenizer.tokenize(input, null);
        lines.forEach(line -> buildDocument(line, wordTokenizer));
    }

    public List<Word> getWords(final String question) {
        if (StringUtils.isBlank(question)) {
            throw new IllegalArgumentException("Question cannot be null or empty");
        }
        final String cleansedStr = question.replaceAll(Pattern.quote("?"), "");
        List<String> words = lineTokenizer.tokenize(cleansedStr, "[\\s]");
        for (WordFilter wordFilter : filterList) {
            words = wordFilter.filter(words);
        }
        return words.stream().map(wordMap::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private void buildDocument(final String line, final Tokenizer wordTokenizer) {
        List<String> words = new ArrayList<>(wordTokenizer.tokenize(line, "[(),\\s]"));
        for (WordFilter wordFilter : filterList) {
            words = wordFilter.filter(words);
        }
        final Sentence sentence = new Sentence(line, words);
        sentence.calculateWeight();
        words.forEach(str -> {
            Word word = wordMap.get(str);
            if (word == null) {
                word = new Word(str);
            }
            word.addLine(sentence);
            wordMap.put(str, word);
        });
    }

}
