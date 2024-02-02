package cryptanalyzer.controllers;

import cryptanalyzer.commands.Action;
import cryptanalyzer.entities.ActionType;
import cryptanalyzer.entities.Result;
import cryptanalyzer.exceptions.AppException;

import java.util.Arrays;

public class CryptController {
    public Result run(String[] params) {
        if(params.length == 0) {
            throw new AppException("No args");
        }
        String actionInput = params[0];
        Action action = ActionType.find(actionInput);
        return action.execute(Arrays.copyOfRange(params, 1, params.length));
    }
}
