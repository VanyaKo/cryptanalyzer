package cryptanalyzer.consts;

import cryptanalyzer.commands.Action;
import cryptanalyzer.commands.Analyzer;
import cryptanalyzer.commands.BruteForce;
import cryptanalyzer.commands.Decoder;
import cryptanalyzer.commands.Encoder;
import cryptanalyzer.exception.AppException;

public enum Actions {
    ENCODE(new Encoder()),
    DECODE(new Decoder()),
    BRUTE_FORCE(new BruteForce()),
    ANALYZE(new Analyzer());

    private final Action action;

    Actions(Action action) {
        this.action = action;
    }

    public static Action find(String actionName) {
        try {
            return Actions.valueOf(actionName.toUpperCase()).action;
        } catch(IllegalArgumentException e) {
            throw new AppException("\"" + actionName + "\"" + " command not found");
        }
    }
}
