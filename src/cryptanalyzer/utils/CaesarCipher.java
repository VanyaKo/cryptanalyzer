package cryptanalyzer.utils;

import cryptanalyzer.consts.Const;
import cryptanalyzer.exception.AppException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class CaesarCipher {
    private CaesarCipher() {
    }

    public static void applyCipherToText(Path src, Path dest, int key, boolean isDecodeMode) {
        try(BufferedReader bufferedReader = Files.newBufferedReader(src);
            BufferedWriter bufferedWriter = Files.newBufferedWriter(dest)) {
            while(bufferedReader.ready()) {
                char srcChar = (char) Character.toLowerCase(bufferedReader.read());
                int srcCharIdx = Const.ALPHABET.indexOf(srcChar);
                if(srcCharIdx == -1) {
                    if(isDecodeMode) {
                        throw new AppException("Char " + srcChar + " does not exist in the alphabet");
                    }
                    continue;
                }
                int destCharIdx = getCipheredChar(srcCharIdx, key);
                bufferedWriter.write(Const.ALPHABET.get(destCharIdx));
            }
        } catch(Exception e) {
            throw new AppException(e.getMessage(), e.getCause());
        }
    }

    private static int getCipheredChar(int idx, int key) {
        int alphabetLength = Const.ALPHABET.size();
        return Math.abs(alphabetLength + idx + key) % alphabetLength;
    }
}
