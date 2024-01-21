package cryptanalyzer.commands;

import cryptanalyzer.consts.Const;
import cryptanalyzer.entity.Result;
import cryptanalyzer.entity.ResultCode;
import cryptanalyzer.utils.CaesarCipher;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class BruteForce implements Action {
    @Override
    public Result execute(String[] params) {
        if(params[1] != null) {
            executeWithRepresentative(params);
        } else {
            executeWithoutRepresentative(Path.of(params[0]), Path.of(params[2]));
        }
        return new Result("", ResultCode.OK);
    }

    private void executeWithoutRepresentative(Path srcFile, Path destFile) {
        for(int key = 1; key < Const.ALPHABET.length; key++) {
            CaesarCipher.applyCipherToText(srcFile, destFile, -key);
            if(keyIsValidated(destFile)) {
                return;
            }
        }
    }

    /**
     * Check correctness of spaces and punctuation marks
     */
    private boolean keyIsValidated(Path destFile) {
        try(BufferedReader bufferedReader = Files.newBufferedReader(destFile)) {
            while(bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if(!line.contains(" ")) {
                    return false;
                }
                StringTokenizer tokenizer = new StringTokenizer(line, " ");
                while(tokenizer.hasMoreTokens()) {
                    String word = tokenizer.nextToken();
                    if(startsWithForbiddenPunctuation(word) || endsWithForbiddenPunctuation(word)) {
                        return false;
                    }
                }
            }
            return true;
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean endsWithForbiddenPunctuation(String word) {
        return word.charAt(word.length() - 1) == Const.FORBIDDEN_END_PUNCTUATION_CHAR;
    }

    private boolean startsWithForbiddenPunctuation(String word) {
        char startSymbol = word.charAt(0);
        return Const.FORBIDDEN_START_PUNCTUATION.contains(startSymbol);
    }

    private void executeWithRepresentative(String[] params) {
        Map<String, Integer> representativeMap = new TreeMap<>();
        addMetric(representativeMap, Path.of(params[1]));
        Map<String, Integer> map = new TreeMap<>();
        for(int i = 0; i < Const.ALPHABET.length; i++) {
            CaesarCipher.applyCipherToText(Path.of(params[0]), Path.of(params[2]), -i);
            addMetric(map, Path.of(params[1]));
        }
    }

    private void addMetric(Map<String, Integer> map, Path dest) {
        try(BufferedReader bufferedReader = Files.newBufferedReader(dest)) {
            while(bufferedReader.ready()) {
                putUniqueWords(map, bufferedReader.readLine());
            }
        } catch(Exception e) {
            System.out.println("BruteForce.addMetric");
        }
    }

    private void putUniqueWords(Map<String, Integer> map, String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, " ");
        while(tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            if(word.length() >= 3) {
                String wordBegin = word.toLowerCase().substring(0, 3);
                if(map.containsKey(wordBegin)) {
                    map.put(wordBegin, map.get(wordBegin) + 1);
                } else {
                    map.put(wordBegin, 0);
                }
            }
        }
    }
}
