package cryptanalyzer.controllers;

import cryptanalyzer.commands.Action;
import cryptanalyzer.consts.Actions;
import cryptanalyzer.entity.Result;

public class MainController {
    public Result doAction(String actionName, String[] params) {
        Action action = Actions.find(actionName);
        return action.execute(params);
    }
}
