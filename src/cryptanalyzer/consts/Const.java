package cryptanalyzer.consts;

import java.io.File;

public class Const {
    private Const() {
    }

    public static final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з',
            'и','к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
            'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};

    public static final String TXT_FOLDER = System.getProperty("user.dir") + File.separator + "text" + File.separator;
    public static final String DEFAULT_ENCRYPTED_FILE_NAME = "encrypt.txt";
    public static final String DEFAULT_DECRYPTED_FILE_NAME = "decrypt.txt";
    public static final String DEFAULT_BRUTE_FORCED_FILE_NAME = "bruteForce.txt";
    public static final String DEFAULT_ANALYZED_FILE_NAME = "analyze.txt";
}
