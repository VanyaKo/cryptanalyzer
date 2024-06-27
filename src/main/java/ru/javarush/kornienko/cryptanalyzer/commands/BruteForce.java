package ru.javarush.kornienko.cryptanalyzer.commands;

import ru.javarush.kornienko.cryptanalyzer.consts.Const;
import ru.javarush.kornienko.cryptanalyzer.entities.ActionType;
import ru.javarush.kornienko.cryptanalyzer.entities.CryptResult;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class BruteForce extends Action {

    @Override
    public CryptResult execute(String[] params) {
        Path srcFile = Path.of(params[0]);
        Path destFile = Path.of(params[2]);
        if(params[1] != null) {
            return executeWithRepresentative(srcFile, Path.of(params[1]), destFile);
        } else {
            return executeWithoutRepresentative(srcFile, destFile);
        }
    }

    /**
     * Based on a dictionary of the most frequent word beginnings (first N letters)
     */
    private CryptResult executeWithRepresentative(Path srcFile, Path representFile, Path destFile) {
        List<String> representText = fileService.readFrom(representFile);
        List<String> srcText = fileService.readFrom(srcFile);
        Map<String, Integer> wordBeginsRepresentativeMap = getWordBeginFrequencyMap(representText);
        List<Map.Entry<String, Integer>> sortedRepresentative = getSortedList(wordBeginsRepresentativeMap);
        TreeMap<Integer, Integer> scorePerKey = new TreeMap<>(Comparator.reverseOrder());
        for(int key = 0; key < Const.ALPHABET.length; key++) {
            List<String> decryptedText = caesarCipher.doCipher(srcText, -key, false);
            Map<String, Integer> wordBeginsBruteForceMap = getWordBeginFrequencyMap(decryptedText);
            List<Map.Entry<String, Integer>> sortedBruteForce = getSortedList(wordBeginsBruteForceMap);
            addScore(scorePerKey, key, sortedRepresentative, sortedBruteForce);
        }
        int resultKey = scorePerKey.firstEntry().getValue();
        List<String> resultText = caesarCipher.doCipher(srcText, resultKey, false);
        fileService.writeTo(destFile, resultText);
        return new CryptResult(CryptResult.SUCCESS_MESSAGE_UNKNOWN_KEY.formatted(ActionType.BRUTE_FORCE.getCommandName(), resultKey));
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

    private Map<String, Integer> getWordBeginFrequencyMap(List<String> text) {
        Map<String, Integer> map = new TreeMap<>();
        for(String line : text) {
            StringTokenizer tokenizer = new StringTokenizer(line, " ");
            while(tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                if(word.length() >= Const.WORD_BEGINNING_LENGTH) {
                    String wordBegin = word.toLowerCase().substring(0, Const.WORD_BEGINNING_LENGTH);
                    map.put(wordBegin, (int) getKeyCount(map, wordBegin));
                }
            }
        }
        return map;
    }

    private CryptResult executeWithoutRepresentative(Path srcFile, Path destFile) {
        for(int key = 1; key < Const.ALPHABET.length; key++) {
            List<String> srcText = fileService.readFrom(srcFile);
            List<String> decodedText = caesarCipher.doCipher(srcText, -key, false);
            if(keyIsValidated(decodedText)) {
                fileService.writeTo(destFile, decodedText);
                return new CryptResult(CryptResult.SUCCESS_MESSAGE_UNKNOWN_KEY.formatted(ActionType.BRUTE_FORCE.getCommandName(), key));
            }
        }
        return new CryptResult(CryptResult.FAIL_MESSAGE.formatted(ActionType.BRUTE_FORCE.getCommandName()));
    }

    private boolean keyIsValidated(List<String> destFile) {
        for(String line : destFile) {
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
    }

    private boolean endsWithForbiddenPunctuation(String word) {
        return word.charAt(word.length() - 1) == Const.FORBIDDEN_END_PUNCTUATION_CHAR;
    }

    private boolean startsWithForbiddenPunctuation(String word) {
        char startSymbol = word.charAt(0);
        return Const.FORBIDDEN_START_PUNCTUATION.contains(startSymbol);
    }
}
