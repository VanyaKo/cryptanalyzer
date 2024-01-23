package cryptanalyzer;

import cryptanalyzer.entity.Result;

public class ConsoleRunner {
    public static void main(String[] args) {
        // encode text.txt encode.txt 12
        Application application = new Application();
        Result result = application.run(args);
        System.out.println(result);
    }
}