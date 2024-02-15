package ru.javarush.kornienko.cryptanalyzer.consts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Const {
    private Const() {
    }

    /**
     * Alphabet regex is [а-еж-я.,«»"':!? ]+
     */
    public static final char[] ALPHABET;
    public static final Map<Character, Integer> ALPHABET_INDEXES;

    static {
        ALPHABET = new char[] {'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з',
                'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
                'ъ', 'ы', 'ь', 'э', 'ю', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};
        ALPHABET_INDEXES = new HashMap<>();
        int index = 0;
        for(Character c : ALPHABET) {
            ALPHABET_INDEXES.put(c, index++);
        }
    }

    public static final List<Character> FORBIDDEN_START_PUNCTUATION = new ArrayList<>(
            List.of('.', ',', '»', ':', '!', '?'));
    public static final char FORBIDDEN_END_PUNCTUATION_CHAR = '«';
    public static final int WORD_BEGINNING_LENGTH = 3;
    public static final int STATISTICS_RANGE = 1_000;
    public static final int ONE_HUNDRED_PERCENT = 100;
}
