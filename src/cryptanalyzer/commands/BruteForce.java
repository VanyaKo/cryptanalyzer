package cryptanalyzer.commands;

import cryptanalyzer.consts.Const;
import cryptanalyzer.entity.Result;
import cryptanalyzer.entity.ResultCode;
import cryptanalyzer.utils.CaesarCipher;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class BruteForce implements Action {
    @Override
    public Result execute(String[] params) {
        Path srcFile = Path.of(params[0]);
        Path destFile = Path.of(params[2]);
        if(params[1] != null) {
            executeWithRepresentative(srcFile, Path.of(params[1]), destFile);
        } else {
            executeWithoutRepresentative(srcFile, destFile);
        }
        return new Result("", ResultCode.OK);
    }

    /**
     * словарь наиболее частых начал слова (3 буквы)
     */
    private void executeWithRepresentative(Path srcFile, Path representativeFile, Path destFile) {
        Map<String, Integer> wordBeginsRepresentativeMap = new TreeMap<>();
        initMap(wordBeginsRepresentativeMap, representativeFile);
        List<Map.Entry<String, Integer>> sortedRepresentative = getSortedList(wordBeginsRepresentativeMap);
        TreeMap<Integer, Integer> scorePerKey = new TreeMap<>(Comparator.reverseOrder());
        for(int key = 1; key < Const.ALPHABET.length; key++) {
            CaesarCipher.applyCipherToText(srcFile, destFile, -key);
            Map<String, Integer> wordBeginsBruteForceMap = new TreeMap<>();
            initMap(wordBeginsBruteForceMap, destFile);
            List<Map.Entry<String, Integer>> sortedBruteForce = getSortedList(wordBeginsBruteForceMap);
            addScore(scorePerKey, key, sortedRepresentative, sortedBruteForce);
        }
        int resultKey = scorePerKey.firstEntry().getValue();
        CaesarCipher.applyCipherToText(srcFile, destFile, resultKey);
    }

    private void addScore(Map<Integer, Integer> scorePerKey, int key,
                          List<Map.Entry<String, Integer>> representative, List<Map.Entry<String, Integer>> bruteForce) {
        int score = 0;
        for(Map.Entry<String, Integer> entryRepresentative : representative) {
            for(Map.Entry<String, Integer> entryBruteForce : bruteForce) {
                if(entryBruteForce.getKey().equals(entryRepresentative.getKey())) {
                    score += entryRepresentative.getValue();
                }
            }
        }
        scorePerKey.put(score, key);
    }

    private List<Map.Entry<String, Integer>> getSortedList(Map<String, Integer> map) {
        return map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .toList();
    }

    private void initMap(Map<String, Integer> map, Path file) {
        try(BufferedReader bufferedReader = Files.newBufferedReader(file)) {
            while(bufferedReader.ready()) {
                putWords(map, bufferedReader.readLine());
            }
        } catch(Exception e) {
            System.out.println("BruteForce.addMetric");
        }
    }

    private void putWords(Map<String, Integer> map, String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, " ");
        while(tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            if(word.length() >= 3) {
                String wordBegin = word.toLowerCase().substring(0, 3);
                if(map.containsKey(wordBegin)) {
                    map.put(wordBegin, map.get(wordBegin) + 1);
                } else {
                    map.put(wordBegin, 1);
                }
            }
        }
    }

    private void executeWithoutRepresentative(Path srcFile, Path destFile) {
        for(int key = 1; key < Const.ALPHABET.length; key++) {
            CaesarCipher.applyCipherToText(srcFile, destFile, -key);
            if(keyIsValidated(destFile)) {
                return;
            }
        }
    }

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
}
