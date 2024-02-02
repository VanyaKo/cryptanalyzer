package cryptanalyzer.entity;

import java.util.Map;

public class SumOfSquaredDeviations {
    public double computeResult(Map<Character, Double> encodedMap, Map<Character, Double> representativeMap) {
        double deviation = 0;
        for(Map.Entry<Character, Double> entry : representativeMap.entrySet()) {
            char symbol = entry.getKey();
            if(encodedMap.containsKey(symbol)) {
                deviation += Math.pow(encodedMap.get(symbol) - entry.getValue(), 2);
            }
        }
        return deviation;
    }
}
