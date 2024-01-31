package cryptanalyzer.utils;

import cryptanalyzer.exception.AppException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static cryptanalyzer.consts.Const.ONE_HUNDRED_PERCENT;
import static cryptanalyzer.consts.Const.STATISTICS_RANGE;

public class Statistics {
    public Map<Character, Double> computeFrequency(String text) {
        Map<Character, Double> frequencyMap = new HashMap<>();
        int thousandsCnt = 0;

        try(BufferedReader bufferedReader = new BufferedReader(new CharArrayReader(text.toLowerCase().toCharArray()))) {
            while(bufferedReader.ready()) {
                char[] buffer = new char[STATISTICS_RANGE];
                int charsRead = bufferedReader.read(buffer);
                if(charsRead < STATISTICS_RANGE) {
                    continue;
                }
                thousandsCnt++;
                for(char ch : buffer) {
                    if(frequencyMap.containsKey(ch)) {
                        frequencyMap.put(ch, frequencyMap.get(ch) + 1);
                    } else {
                        frequencyMap.put(ch, 1.0);
                    }
                }
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
}
