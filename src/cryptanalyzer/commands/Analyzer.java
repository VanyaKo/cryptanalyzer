package cryptanalyzer.commands;

import cryptanalyzer.FileService;
import cryptanalyzer.consts.Actions;
import cryptanalyzer.consts.Const;
import cryptanalyzer.entity.Result;
import cryptanalyzer.entity.SumOfSquaredDeviations;
import cryptanalyzer.utils.CaesarCipher;
import cryptanalyzer.utils.Statistics;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Analyzer extends Action {
    private final CaesarCipher caesarCipher;
    private final FileService fileService;
    private final Statistics statistics;

    public Analyzer() {
        caesarCipher = new CaesarCipher();
        fileService = new FileService();
        statistics = new Statistics();
    }

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
        List<String> representText = fileService.readFrom(representFile);
        Map<Character, Double> representFrequency = statistics.computeFrequency(representText);
        SumOfSquaredDeviations formula = new SumOfSquaredDeviations();
        List<String> decryptedText = null;

        int minDeviationKey = 0;
        double minDeviationValue = Double.MAX_VALUE;
        for(int key = 0; key < Const.ALPHABET.length; key++) {
            List<String> srcText = fileService.readFrom(srcFile);
            decryptedText = caesarCipher.doCipher(srcText, -key, false);
            Map<Character, Double> destFrequency = statistics.computeFrequency(decryptedText);
            double decodedMetric = formula.computeResult(representFrequency, destFrequency);
            double currentDeviation = Math.abs(decodedMetric - 0);
            System.out.println("key=" + key + "deviation=" + currentDeviation);
            if(currentDeviation < minDeviationValue) {
                minDeviationValue = currentDeviation;
                minDeviationKey = key;
            }
        }
        int resultKey = minDeviationKey;
        System.out.println("Found key is " + resultKey);
        return new Result(Result.SUCCESS_MESSAGE_UNKNOWN_KEY.formatted(Actions.ANALYZE.getCommandName(), resultKey));
    }

    private Result executeWithoutRepresentative(Path srcFile, Path destFile) {
        List<String> srcText = fileService.readFrom(srcFile);
        Map<Character, Double> srcFrequency = statistics.computeFrequency(srcText);
        List<Map.Entry<Character, Double>> sortedFrequency = getSortedList(srcFrequency);
        int resultKey = computeKey(sortedFrequency);
        caesarCipher.doCipher(srcText, resultKey, false);
        System.out.println("Key is " + resultKey);
        System.out.println("Sorted frequency is " + sortedFrequency);
        return new Result(Result.SUCCESS_MESSAGE_UNKNOWN_KEY.formatted(Actions.ANALYZE.getCommandName(), resultKey));
    }

    private int computeKey(List<Map.Entry<Character, Double>> sortedFrequency) {
        char resultChar = sortedFrequency.get(0).getKey();
        int resultCharIdx = Const.ALPHABET_INDEXES.get(resultChar);
        int spaceIdx = Const.ALPHABET_INDEXES.get(' ');
        return computeOffset(resultCharIdx, spaceIdx);
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
