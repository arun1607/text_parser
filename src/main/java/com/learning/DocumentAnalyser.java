package com.learning;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DocumentAnalyser {
    private final TextParser textParser;
    private final Tokenizer wordTokenizer;

    public DocumentAnalyser() {
        List<WordFilter> filters = new ArrayList<>();
        filters.add(new StopWordFilter());
        filters.add(new WordNormalizer());
        Tokenizer lineTokenizer = new LineTokenizer();
        wordTokenizer = new WordTokenizer();
        textParser = new TextParser(lineTokenizer, filters);
    }

    public void init() throws ParsingException {
        textParser.init();
    }

    public void read(String input) {
        if (StringUtils.isBlank(input)) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        textParser.parse(input, wordTokenizer);
    }

    public List<String> getAnswerInCorrectOrder(final List<String> questions, final String answerStr) {
        if (StringUtils.isBlank(answerStr)) {
            throw new IllegalArgumentException("Answer string cannot be null or empty");
        }
        if (questions == null) {
            throw new NullPointerException("Questions cannot be null");
        }
        final List<String> answers = wordTokenizer.tokenize(answerStr, ";");
        final Map<String, String> result = new HashMap<>();
        questions.forEach(q -> getAnswerForQuestion(q).ifPresent(getMatchingSentence(q, answers, result)));
        return questions.stream().map(result::get).collect(Collectors.toList());
    }

    private Consumer<Sentence> getMatchingSentence(String question, List<String> answers, Map<String, String> result) {
        return matchingSentence -> {
            String matchingAnswer = matchingSentence.getLine();
            for (String answer : answers) {
                if (matchingAnswer.contains(answer)) {
                    result.put(question, answer);
                    break;
                }
            }
        };
    }

    private Optional<Sentence> getAnswerForQuestion(String question) {
        final List<Word> words = textParser.getWords(question);
        final Set<Sentence> possibleSentences = words.stream().flatMap(word -> word.getSentences().stream()).collect(Collectors.toSet());

        int maxMatchCount = 0;
        Optional<Sentence> maxWeightSentence = Optional.empty();
        for (Sentence sentence : possibleSentences) {
            int matchCount = 0;
            for (Word word : words) {
                final Integer weightForKey = sentence.getWeightForKey(word.getKey());
                if (weightForKey != null && weightForKey > 0) {
                    matchCount++;
                }
            }
            if (matchCount > maxMatchCount) {
                maxMatchCount = matchCount;
                maxWeightSentence = Optional.of(sentence);
            }
        }

        return maxWeightSentence;
    }
}
