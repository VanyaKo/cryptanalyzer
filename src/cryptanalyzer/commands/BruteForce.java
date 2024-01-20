package cryptanalyzer.commands;

import cryptanalyzer.consts.Const;
import cryptanalyzer.entity.Result;
import cryptanalyzer.entity.ResultCode;
import cryptanalyzer.utils.CaesarCipher;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class BruteForce implements Action{
    @Override
    public Result execute(String[] params) {
        if(params.length > 3) {
            executeWithRepresentative(params);
        }
        return new Result("", ResultCode.OK);
    }

    /**
     *
     * @param params src file, file with unencrypted representative text, dest file
     */
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
