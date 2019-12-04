package com.learning;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextParser {

    private List<String> sentences;
    private final Map<String, Document> documentMap = new HashMap<>();
    private final LineTokenizer lineTokenizer;
    private final StopWordFilter stopWordFilter;
    private final WordNormalizer wordNormalizer;

    public TextParser(LineTokenizer lineTokenizer, StopWordFilter stopWordFilter, WordNormalizer wordNormalizer) {
        this.lineTokenizer = lineTokenizer;
        this.stopWordFilter = stopWordFilter;
        this.wordNormalizer = wordNormalizer;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public void parse(String input) {
        final List<String> lines = lineTokenizer.tokenize(input);
        lines.forEach(this::buildDocument);
    }

    public Set<String> getMatchingPhrases(final String question) {
        final String cleansedStr = question.replaceAll(Pattern.quote("?"), "");
        final List<String> filteredQuestions = stopWordFilter.filterStopWords(cleansedStr, "[():,\\s]").stream().map(wordNormalizer::normalize)
                .map(String::toLowerCase).collect(Collectors.toList());
        final Set<String> matchingPhrases = filteredQuestions.stream()
                .flatMap(str -> documentMap.get(str).getSentences().stream()).collect(Collectors.toSet());

        Set<String> result = new HashSet<>();
        for (String matchingPhrase : matchingPhrases) {
            boolean matchFound = true;
            for (String filteredQuestion : filteredQuestions) {
                if (!matchingPhrase.contains(filteredQuestion.toLowerCase())) {
                    matchFound = false;
                    break;
                }
            }
            if (matchFound) {
                result.add(matchingPhrase);
            }
        }
        return result;
    }

    public List<String> getAnswerInCorrectOrder(final List<String> questions, final String answerStr) {
        final List<String> answers = Arrays.stream(answerStr.split(";")).map(String::trim).collect(Collectors.toList());

        final List<String> result = new ArrayList<>();
        for (String question : questions) {
            result.addAll(getMatchingPhrases(question));
        }
        List<String> finalResult = new ArrayList<>();

        for (String str : result) {
            boolean matchFound = true;
            for (String answer : answers) {
                if (!str.contains(answer) || !answer.contains(str)) {
                    matchFound = false;
                    break;
                }
            }
            if (matchFound) {
                finalResult.add(str);
            }
        }

//        List<String> filteredQuestions = questions.stream()
//                .map(str -> str.replaceAll(Pattern.quote("?"), ""))
//                .flatMap(str -> stopWordFilter.filterStopWords(str, "[():,\\s]").stream())
//                .map(wordNormalizer::normalize)
//                .map(String::toLowerCase).collect(Collectors.toList());
//
//
//        return filteredQuestions.stream().map(str -> findMatchingAnswer(str, answers)).collect(Collectors.toList());
        return finalResult;
    }

    private String getAnswer(String question) {
        final String cleansedStr = question.replaceAll(Pattern.quote("?"), "");
        final List<String> filteredQuestions = stopWordFilter.filterStopWords(cleansedStr, "[():,\\s]");
        final List<String> normalizedQuestionParts = filteredQuestions.stream().map(wordNormalizer::normalize).map(String::toLowerCase).collect(Collectors.toList());

        final Set<String> matchingLines = normalizedQuestionParts.stream().flatMap(str -> documentMap.get(str).getSentences().stream()).collect(Collectors.toSet());


//        Set<String> matchingAnswer = findMatchingAnswer(normalizedQuestionParts);
//        Set<String> matchingSentences = normalizedQuestionParts.stream().flatMap(str -> documentMap.get(str).stream()).collect(Collectors.toSet());

        return null;


    }

    //    private Set<String> findMatchingAnswer(final String word, final List<String> answers) {
//        Set<String> matchingSentences = documentMap.get(word);
//        Set<String> resultSentences = new HashSet<>();
//        for (String answer : answers) {
//            for (String matchingSentence : matchingSentences) {
//                if (matchingSentence.contains(answer)) {
//                    resultSentences.add(answer);
//                }
//            }
//        }
//        return resultSentences;
//    }

    private void buildDocument(final String line) {
        final List<String> filteredLines = stopWordFilter.filterStopWords(line, "[():,\\s]");
        final List<String> normalizedLines = filteredLines.stream().map(wordNormalizer::normalize).collect(Collectors.toList());
        normalizedLines.stream().filter(StringUtils::isNotBlank).forEach(word -> {
            Document document = documentMap.get(word);
            if (document == null) {
                document = new Document(word);
            }
            document.addSentence(line.toLowerCase());
            documentMap.put(word, document);
        });
    }

}
