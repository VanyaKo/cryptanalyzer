package cryptanalyzer.consts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Const {
    private Const() {
    }

    /**
     * Alphabet regex is [а-еж-я.,«»"':!? ]+
     */
    private static final char[] ALPHABET_ARRAY = {'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з',
            'и', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
            'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};
    public static final List<Character> ALPHABET = new ArrayList<>();

    static {
        for(char c : ALPHABET_ARRAY) {
            ALPHABET.add(c);
        }
    }

    public static final int WORD_DELIMITERS_START_INDEX = 32;
    public static final List<Character> FORBIDDEN_START_PUNCTUATION = new ArrayList<>(
            List.of('.', ',', '»', ':', '!', '?'));
    public static final char FORBIDDEN_END_PUNCTUATION_CHAR = '«';
    public static final int STATISTICS_RANGE = 1_000;
    public static final String TXT_FOLDER = System.getProperty("user.dir") + File.separator + "text" + File.separator;
    public static final String DEFAULT_ENCRYPTED_FILE_NAME = "encrypt.txt";
    public static final String DEFAULT_DECRYPTED_FILE_NAME = "decrypt.txt";
    public static final String DEFAULT_BRUTE_FORCED_FILE_NAME = "bruteForce.txt";
    public static final String DEFAULT_ANALYZED_FILE_NAME = "analyze.txt";
}
