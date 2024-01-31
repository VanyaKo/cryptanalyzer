package cryptanalyzer.entity;

import java.util.Map;

public class SumOfSquaredDeviations {
    public double computeResult(Map<Character, Double> representativeMap, Map<Character, Double> encodedMap) {
        double deviation = 0;
        for(Map.Entry<Character, Double> entry : representativeMap.entrySet()) {
            char symbol = entry.getKey();
            if(encodedMap.containsKey(symbol)) {
                deviation += Math.pow(entry.getValue() - encodedMap.get(symbol), 2);
            }
        }
        return deviation;
    }

    private double computeAverage(Map<Character, Double> map) {
        double sum = sumValues(map);
        return sum / map.size();
    }

    private double sumValues(Map<Character, Double> map) {
        double sum = 0;
        for(Map.Entry<Character, Double> entry : map.entrySet()) {
            sum += entry.getValue();
        }
        return sum;
    }
}
