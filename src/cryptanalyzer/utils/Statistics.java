package cryptanalyzer.utils;

import cryptanalyzer.exception.AppException;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static cryptanalyzer.consts.Const.STATISTICS_RANGE;

public class Statistics {
    public static Map<Character, Double> computeFrequency(Path file) {
        Map<Character, Double> frequencyMap = new HashMap<>();
        int thousandsCnt = 0;
        try(BufferedReader bufferedReader = Files.newBufferedReader(file)) {
            while(bufferedReader.ready()) {
                char[] buffer = new char[STATISTICS_RANGE];
                int charsRead = bufferedReader.read(buffer);
                if(charsRead < STATISTICS_RANGE) {
                    continue;
                }
                thousandsCnt++;
                for(char ch : buffer) {
                    ch = Character.toLowerCase(ch);
                    if(frequencyMap.containsKey(ch)) {
                        frequencyMap.put(ch, frequencyMap.get(ch) + 1);
                    } else {
                        frequencyMap.put(ch, 1.0);
                    }
                }
            }
            System.out.println("Full thousands is " + thousandsCnt);
            if(thousandsCnt < 1) {
                throw new AppException("Cannot provide statistics since text size < 1 000");
            } else if(thousandsCnt > 1) {
                for(Map.Entry<Character, Double> entry : frequencyMap.entrySet()) {
                    entry.setValue(entry.getValue() / (thousandsCnt * STATISTICS_RANGE));
                }
                System.out.println(frequencyMap);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return frequencyMap;
    }
}
