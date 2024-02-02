package cryptanalyzer.utils;

import cryptanalyzer.consts.Const;
import cryptanalyzer.exception.AppException;

import java.util.ArrayList;
import java.util.List;

public class CaesarCipher {
    public List<String> doCipher(List<String> srcText, int key, boolean isDecodeMode) {
        List<String> cipheredText = new ArrayList<>();
        for(String line : srcText) {
            StringBuilder resultLine = new StringBuilder();
            for(char srcChar : line.toLowerCase().toCharArray()) {
                Integer srcCharIdx = Const.ALPHABET_INDEXES.get(srcChar);
                if(srcCharIdx == null) {
                    if(isDecodeMode) {
                        throw new AppException("Symbol '" + srcChar + "' does not exist in the alphabet");
                    }
                    continue;
                }
                int destCharIdx = getCipheredChar(srcCharIdx, key);
                resultLine.append(Const.ALPHABET[destCharIdx]);
            }
            cipheredText.add(resultLine.toString());
        }
        return cipheredText;
    }

    private int getCipheredChar(int idx, int key) {
        int alphabetLength = Const.ALPHABET.length;
        return Math.abs(alphabetLength + idx + key) % alphabetLength;
    }
}
