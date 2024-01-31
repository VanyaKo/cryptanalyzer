package cryptanalyzer.controllers;

import cryptanalyzer.commands.Action;
import cryptanalyzer.consts.Actions;
import cryptanalyzer.entity.Result;
import cryptanalyzer.exception.AppException;

import java.util.Arrays;

public class CryptController {
    public Result run(String[] params) {
        if(params.length == 0) {
            throw new AppException("No args");
        }
        String actionInput = params[0];
        Action action = Actions.find(actionInput);
        return action.execute(Arrays.copyOfRange(params, 1, params.length));
    }
}
