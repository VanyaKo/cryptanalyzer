package cryptanalyzer.utils;

import cryptanalyzer.consts.Const;
import cryptanalyzer.exception.AppException;
import cryptanalyzer.model.CryptModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CaesarCipher {
    public String doCipher(String srcText, int key, boolean isDecodeMode) {
        StringBuilder cipheredText = new StringBuilder();
        for(char srcChar : srcText.toLowerCase().toCharArray()) {
            Integer srcCharIdx = Const.ALPHABET_INDEXES.get(srcChar);
            if(srcCharIdx == null) {
                if(isDecodeMode) {
                    throw new AppException("Char " + srcChar + " does not exist in the alphabet");
                }
                continue;
            }
            int destCharIdx = getCipheredChar(srcCharIdx, key);
            cipheredText.append(Const.ALPHABET[destCharIdx]);
        }
        return cipheredText.toString();
    }

    private int getCipheredChar(int idx, int key) {
        int alphabetLength = Const.ALPHABET.length;
        return Math.abs(alphabetLength + idx + key) % alphabetLength;
    }
}
