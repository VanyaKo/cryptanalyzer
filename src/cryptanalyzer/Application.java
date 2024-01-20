package cryptanalyzer;

import cryptanalyzer.controllers.MainController;
import cryptanalyzer.entity.Result;
import cryptanalyzer.exception.AppException;

import java.util.Arrays;

public class Application {
    private final MainController mainController;
    public Application() {
        mainController = new MainController();
    }

    public Result run(String[] params) {
        if(params.length > 0) {
            String action = params[0];
            String[] args = Arrays.copyOfRange(params, 1, params.length);
            return mainController.doAction(action, args);
        }
        throw new AppException("No args");
    }
}
