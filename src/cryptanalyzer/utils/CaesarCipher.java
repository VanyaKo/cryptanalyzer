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
                int srcCharIdx = Const.ALPHABET.indexOf(srcChar);
                if(srcCharIdx == -1) {
                    continue;
                }
                int destCharIdx = getCipheredChar(srcCharIdx, key);
                bufferedWriter.write(Const.ALPHABET.get(destCharIdx));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static int getCipheredChar(int idx, int key) {
        int alphabetLength = Const.ALPHABET.size();
        return Math.abs(alphabetLength + idx + key) % alphabetLength;
    }
}
