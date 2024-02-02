package cryptanalyzer.commands;

import cryptanalyzer.FileService;
import cryptanalyzer.consts.Actions;
import cryptanalyzer.consts.Const;
import cryptanalyzer.entity.Result;
import cryptanalyzer.exception.AppException;
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

public class BruteForce extends Action {
    @Override
    public Result execute(String[] params) {
        Path srcFile = Path.of(params[0]);
        Path destFile = Path.of(params[2]);
        if(params[1] != null) {
            return executeWithRepresentative(srcFile, Path.of(params[1]), destFile);
        } else {
            return executeWithoutRepresentative(srcFile, destFile);
        }
    }

    /**
     * Based on a dictionary of the most frequent word beginnings (first 3 letters)
     */
    private Result executeWithRepresentative(Path srcFile, Path representFile, Path destFile) {
        Map<String, Integer> wordBeginsRepresentativeMap = new TreeMap<>();
        List<String> representText = fileService.readFrom(representFile);
        initMap(wordBeginsRepresentativeMap, representText);
        List<Map.Entry<String, Integer>> sortedRepresentative = getSortedList(wordBeginsRepresentativeMap);
        TreeMap<Integer, Integer> scorePerKey = new TreeMap<>(Comparator.reverseOrder());
        List<String> srcText = fileService.readFrom(srcFile);
        for(int key = 0; key < Const.ALPHABET.length; key++) {
            List<String> decryptedText = caesarCipher.doCipher(srcText, -key, false);
            Map<String, Integer> wordBeginsBruteForceMap = new TreeMap<>();
            initMap(wordBeginsBruteForceMap, decryptedText);
            List<Map.Entry<String, Integer>> sortedBruteForce = getSortedList(wordBeginsBruteForceMap);
            addScore(scorePerKey, key, sortedRepresentative, sortedBruteForce);
        }
        int resultKey = scorePerKey.firstEntry().getValue();
        List<String> resultText = caesarCipher.doCipher(srcText, resultKey, false);
        fileService.writeTo(destFile, resultText);
        return new Result(Result.SUCCESS_MESSAGE_UNKNOWN_KEY.formatted(Actions.BRUTE_FORCE.getCommandName(), resultKey));
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

    private void initMap(Map<String, Integer> map, List<String> text) {
        for(String line : text) {
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
    }

    private Result executeWithoutRepresentative(Path srcFile, Path destFile) {
        for(int key = 1; key < Const.ALPHABET.length; key++) {
            List<String> srcText = fileService.readFrom(srcFile);
            List<String> decodedText = caesarCipher.doCipher(srcText, -key, false);
            fileService.writeTo(destFile, decodedText);
            if(keyIsValidated(destFile)) {
                return new Result(Result.SUCCESS_MESSAGE_UNKNOWN_KEY.formatted(Actions.BRUTE_FORCE.getCommandName(), key));
            }
        }
        return new Result(Result.FAIL_MESSAGE.formatted(Actions.BRUTE_FORCE.getCommandName()));
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
            throw new AppException(e.getMessage(), e.getCause());
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
