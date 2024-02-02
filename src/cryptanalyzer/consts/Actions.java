package cryptanalyzer.consts;

import cryptanalyzer.commands.Action;
import cryptanalyzer.commands.Analyzer;
import cryptanalyzer.commands.BruteForce;
import cryptanalyzer.commands.Decoder;
import cryptanalyzer.commands.Encoder;
import cryptanalyzer.exception.AppException;

public enum Actions {
    ENCRYPT(new Encoder(), "Encoding"),
    DECRYPT(new Decoder(), "Decoding"),
    BRUTE_FORCE(new BruteForce(), "Brute force"),
    ANALYZE(new Analyzer(), "Statistical analysis");

    private final Action action;
    private final String commandName;

    Actions(Action action, String commandName) {
        this.action = action;
        this.commandName = commandName;
    }

    public static Action find(String actionName) {
        try {
            return Actions.valueOf(actionName.toUpperCase()).action;
        } catch(IllegalArgumentException e) {
            throw new AppException("\"" + actionName + "\"" + " command not found");
        }
    }

    public String getCommandName() {
        return commandName;
    }
}
