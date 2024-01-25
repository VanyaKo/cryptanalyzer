package cryptanalyzer.commands;

import cryptanalyzer.consts.Actions;
import cryptanalyzer.consts.Const;
import cryptanalyzer.entity.Result;
import cryptanalyzer.utils.CaesarCipher;
import cryptanalyzer.utils.Statistics;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Analyzer implements Action {
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


    private Result executeWithRepresentative(Path srcFile, Path representativeFile, Path destFile) {
        Map<Character, Double> representativeFrequency = computeFrequency(representativeFile);
        int resultKey = 0;
        for(int key = 0; key < Const.ALPHABET.size(); key++) {
            CaesarCipher.applyCipherToText(srcFile, destFile, -key, false);
            Map<Character, Double> destFrequency = computeFrequency(destFile);
        }
        return new Result(Result.SUCCESS_MESSAGE_UNKNOWN_KEY.formatted(Actions.ANALYZE.getCommandName(), resultKey));
    }

    private Map<Character, Double> computeFrequency(Path representativeFile) {
        return Statistics.computeFrequency(representativeFile);
    }

    private Result executeWithoutRepresentative(Path srcFile, Path destFile) {
        Map<Character, Double> srcFrequency = computeFrequency(srcFile);
        List<Map.Entry<Character, Double>> sortedFrequency = getSortedList(srcFrequency);
        int resultKey = computeKey(sortedFrequency);
        CaesarCipher.applyCipherToText(srcFile, destFile, resultKey, false);
        return new Result(Result.SUCCESS_MESSAGE_UNKNOWN_KEY.formatted(Actions.ANALYZE.getCommandName(), resultKey));
    }

    private int computeKey(List<Map.Entry<Character, Double>> sortedFrequency) {
        char resultChar = sortedFrequency.get(0).getKey();
        int resultCharIdx = Const.ALPHABET.indexOf(resultChar);
        int spaceIdx = Const.ALPHABET.indexOf(' ');
        return computeOffset(resultCharIdx, spaceIdx);
    }

    private int computeOffset(int resultCharIdx, int spaceIdx) {
        int alphabetLength = Const.ALPHABET.size();
        return Math.abs(alphabetLength + resultCharIdx - spaceIdx) % alphabetLength;
    }

    private List<Map.Entry<Character, Double>> getSortedList(Map<Character, Double> map) {
        return map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .toList();
    }
}
