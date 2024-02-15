package ru.javarush.kornienko.cryptanalyzer.services;

import ru.javarush.kornienko.cryptanalyzer.consts.Const;
import ru.javarush.kornienko.cryptanalyzer.exceptions.AppException;

import java.util.ArrayList;
import java.util.List;

public class CaesarCipher {
    public List<String> doCipher(List<String> srcText, int key, boolean isDecodeMode) {
        List<String> cipheredText = new ArrayList<>();
        for(String line : srcText) {
            StringBuilder resultLine = getCipheredLine(key, isDecodeMode, line);
            cipheredText.add(resultLine.toString());
        }
        return cipheredText;
    }

    private StringBuilder getCipheredLine(int key, boolean isDecodeMode, String line) {
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
        return resultLine;
    }

    private int getCipheredChar(int idx, int key) {
        int alphabetLength = Const.ALPHABET.length;
        return Math.abs(alphabetLength + idx + key) % alphabetLength;
    }
}
