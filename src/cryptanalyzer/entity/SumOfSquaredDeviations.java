package cryptanalyzer.entity;

import java.util.Map;

public class SumOfSquaredDeviations {
    public double computeResult(Map<Character, Double> map) {
        double average = computeAverage(map);
        double squaredSum = 0;
        for(Map.Entry<Character, Double> entry : map.entrySet()) {
            squaredSum += Math.pow(entry.getValue() - average, 2);
        }
        return squaredSum / (map.size() - 1);
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
