package cryptanalyzer.commands;

import cryptanalyzer.entity.Result;

public interface Action {
    Result execute(String[] params);
}
