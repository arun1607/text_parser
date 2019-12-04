package com.learning;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TextParserTest {
    private TextParser textParser;


    @Before
    public void setup() throws Exception {
        final String input = "Zebras are several species of African equids (horse family) united by their distinctive black and white stripes. Their stripes come in different patterns, unique to each individual. They are generally social animals that live in small harems to large herds. Unlike their closest relatives, horses and donkeys, zebras have never been truly domesticated. There are three species of zebras: the plains zebra, the Grévy's zebra and the mountain zebra. The plains zebra and the mountain zebra belong to the subgenus Hippotigris, but Grévy's zebra is the sole species of subgenus Dolichohippus. The latter resembles an ass, to which it is closely related, while the former two are morehorse-like. All three belong to the genus Equus, along with other living equids. The unique stripes of zebras make them one of the animals most familiar to people. They occur in a variety of habitats, such as grasslands, savannas, woodlands, thorny scrublands, mountains, and coastal hills. However, various anthropogenic factors have had a severe impact on zebra populations, in particular hunting for skins and habitat destruction. Grévy's zebra and the mountain zebra are endangered. While plains zebras are much more plentiful, one subspecies - the Quagga - became extinct in the late 19th century. Though there is currently a plan, called the Quagga Project, that aims to breed zebras that are phenotypically similar to the Quagga, in a process called breeding back.";
        final LineTokenizer lineTokenizer = new LineTokenizer("[?.]");
        final StopWordFilter stopWordFilter = new StopWordFilter("src/main/resources/stop_words.txt");
        stopWordFilter.init();
        final WordNormalizer wordNormalizer = new WordNormalizer("src/main/resources/normalization_rule.txt");
        wordNormalizer.init();
        textParser = new TextParser(lineTokenizer, stopWordFilter, wordNormalizer);
        textParser.parse(input);
    }

    @Test
    public void getAnswer() throws Exception {
        final String answers = "subgenus Hippotigris; the plains zebra, the Grévy's zebra and the mountain zebra;horses and donkeys;aims to breed zebras that are phenotypically similar to the quagga; Grévy's zebra and the mountain zebra";
        final List<String> questions = new ArrayList<>();
        questions.add("Which Zebras are endangered?");
        questions.add("What is the aim of the Quagga Project?");
        questions.add("Which animals are some of their closest relatives?");
        questions.add("Which are the three species of zebras?");
        questions.add("Which subgenus do the plains zebra and the mountain zebra belong to?");

        List<String> answerInCorrectOrder = textParser.getAnswerInCorrectOrder(questions, answers);
        Assert.assertNotNull(answerInCorrectOrder);
        Assert.assertTrue(answerInCorrectOrder.size() > 0);
    }

    @Test
    public void getMatchingPhrase() throws Exception {
        Set<String> matchingPhrases = textParser.getMatchingPhrases("Which Zebras are endangered?");
        Assert.assertNotNull(matchingPhrases);
    }


}