package cryptanalyzer.commands;

import cryptanalyzer.consts.Const;
import cryptanalyzer.entities.ActionType;
import cryptanalyzer.entities.Result;
import cryptanalyzer.services.SumOfSquaredDeviations;
import cryptanalyzer.exceptions.AppException;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cryptanalyzer.consts.Const.ONE_HUNDRED_PERCENT;
import static cryptanalyzer.consts.Const.STATISTICS_RANGE;

public class Analyzer extends Action {
    @Override
    public Result execute(String[] params) {

        String pathFrom = params[0];
        String pathTo = params[2];
        Path srcFile = fileService.getPath(pathFrom);
        Path destFile = fileService.getPath(pathTo);
        if(params[1] != null) {
            Path representFile = fileService.getPath(params[1]);
            return executeWithRepresentative(srcFile, representFile, destFile);
        } else {
            return executeWithoutRepresentative(srcFile, destFile);
        }
    }


    private Result executeWithRepresentative(Path srcFile, Path representFile, Path destFile) {
        List<String> srcText = fileService.readFrom(srcFile);
        List<String> representText = fileService.readFrom(representFile);
        Map<Character, Double> representFrequency = computeFrequency(representText);
        SumOfSquaredDeviations formula = new SumOfSquaredDeviations();
        List<String> decryptedText;

        int minDeviationKey = 0;
        double minDeviationValue = Double.MAX_VALUE;
        for(int key = 0; key < Const.ALPHABET.length; key++) {
            decryptedText = caesarCipher.doCipher(srcText, -key, false);
            Map<Character, Double> destFrequency = computeFrequency(decryptedText);
            double currentDeviation = Math.abs(formula.computeResult(destFrequency,
                    representFrequency));
            if(currentDeviation < minDeviationValue) {
                minDeviationValue = currentDeviation;
                minDeviationKey = key;
            }
        }
        fileService.writeTo(destFile, caesarCipher.doCipher(srcText, -minDeviationKey, false));
        return new Result(Result.SUCCESS_MESSAGE_UNKNOWN_KEY.formatted(ActionType.ANALYZE.getCommandName(), minDeviationKey));
    }

    private Result executeWithoutRepresentative(Path srcFile, Path destFile) {
        List<String> srcText = fileService.readFrom(srcFile);
        Map<Character, Double> srcFrequency = computeFrequency(srcText);
        List<Map.Entry<Character, Double>> sortedFrequency = getSortedList(srcFrequency);
        int resultKey = computeKeyBySpace(sortedFrequency);
        List<String> decodedText = caesarCipher.doCipher(srcText, -resultKey, false);
        fileService.writeTo(destFile, decodedText);
        return new Result(Result.SUCCESS_MESSAGE_UNKNOWN_KEY.formatted(ActionType.ANALYZE.getCommandName(), resultKey));
    }

    public Map<Character, Double> computeFrequency(List<String> text) {
        Map<Character, Double> frequencyMap = new HashMap<>();
        int thousandsCnt = 0;

        try(BufferedReader bufferedReader = new BufferedReader(new CharArrayReader(text.toString().toLowerCase().toCharArray()))) {
            while(bufferedReader.ready()) {
                char[] buffer = new char[STATISTICS_RANGE];
                int charsRead = bufferedReader.read(buffer);
                if(charsRead < STATISTICS_RANGE) {
                    continue;
                }
                thousandsCnt++;
                putUniqueSymbols(buffer, frequencyMap);
            }
            if(thousandsCnt < 1) {
                throw new AppException("Cannot provide statistics since text size < 1 000");
            } else if(thousandsCnt > 1) {
                for(Map.Entry<Character, Double> entry : frequencyMap.entrySet()) {
                    entry.setValue(entry.getValue() * ONE_HUNDRED_PERCENT / (thousandsCnt * STATISTICS_RANGE));
                }
            }
        } catch(Exception e) {
            throw new AppException(e.getMessage(), e.getCause());
        }

        return frequencyMap;
    }

    private void putUniqueSymbols(char[] buffer, Map<Character, Double> frequencyMap) {
        for(char ch : buffer) {
            frequencyMap.put(ch, getKeyCount(frequencyMap, ch));
        }
    }

    private int computeKeyBySpace(List<Map.Entry<Character, Double>> sortedFrequency) {
        char mostFrequentChar = sortedFrequency.get(0).getKey();
        int mostFrequentCharIdx = Const.ALPHABET_INDEXES.get(mostFrequentChar);
        int spaceIdx = Const.ALPHABET_INDEXES.get(' ');
        return computeOffset(mostFrequentCharIdx, spaceIdx);
    }

    private int computeOffset(int resultCharIdx, int spaceIdx) {
        int alphabetLength = Const.ALPHABET.length;
        return Math.abs(alphabetLength + resultCharIdx - spaceIdx) % alphabetLength;
    }

    private List<Map.Entry<Character, Double>> getSortedList(Map<Character, Double> map) {
        return map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .toList();
    }
}
