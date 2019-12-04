package com.learning;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WordNormalizerTest {

    private WordNormalizer wordNormalizer;

    @Before
    public void setup() throws Exception {
        wordNormalizer = new WordNormalizer("src/main/resources/normalization_rule.txt");
        wordNormalizer.init();
    }


    @Test
    public void normalize() {
        String result = wordNormalizer.normalize("Cuteness");
        Assert.assertEquals("cute", result);
    }
}
