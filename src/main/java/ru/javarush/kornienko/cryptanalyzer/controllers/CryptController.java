package ru.javarush.kornienko.cryptanalyzer.controllers;


import ru.javarush.kornienko.cryptanalyzer.commands.Action;
import ru.javarush.kornienko.cryptanalyzer.entities.ActionType;
import ru.javarush.kornienko.cryptanalyzer.entities.CryptResult;
import ru.javarush.kornienko.cryptanalyzer.exceptions.AppException;
import ru.javarush.kornienko.cryptanalyzer.services.FileService;
import ru.javarush.kornienko.cryptanalyzer.services.Logger;

import java.util.Arrays;

public class CryptController {
    public void run(String[] params) {
        try {
            if(params.length == 0) {
                throw new AppException("No command line arguments");
            }
            String actionInput = params[0];
            Action action = ActionType.find(actionInput);
            CryptResult result = action.execute(Arrays.copyOfRange(params, 1, params.length));
            System.out.println(result);
        } catch(Exception e) {
            FileService fileService = new FileService();
            Logger logger = new Logger(fileService);
            logger.logException(e);
            System.out.println(e.getLocalizedMessage());
        }
    }
}
