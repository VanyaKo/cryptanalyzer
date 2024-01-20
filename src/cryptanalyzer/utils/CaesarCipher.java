package cryptanalyzer.utils;

import cryptanalyzer.consts.Const;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class CaesarCipher {
    private CaesarCipher() {
    }

    public static void applyCipherToText(Path src, Path dest, int key) {
        try(BufferedReader bufferedReader = Files.newBufferedReader(src);
            BufferedWriter bufferedWriter = Files.newBufferedWriter(dest)) {
            while(bufferedReader.ready()) {
                char srcChar = (char) bufferedReader.read();
                int srcCharIdx = getAlphabetIdx(srcChar);
                if(srcCharIdx < 0) {
                    continue;
                }
                int destCharIdx = applyCipherToChar(srcCharIdx, key);
                bufferedWriter.write(Const.ALPHABET[destCharIdx]);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static int getAlphabetIdx(char srcChar) {
        for(int i = 0; i < Const.ALPHABET.length; i++) {
            if(Character.toLowerCase(srcChar) == Const.ALPHABET[i]) {
                return i;
            }
        }
        return -1;
    }

    private static int applyCipherToChar(int idx, int key) {
        return Math.abs(idx + key) % Const.ALPHABET.length;
    }
}
