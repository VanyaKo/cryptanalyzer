package ru.javarush.kornienko.cryptanalyzer.commands;


import ru.javarush.kornienko.cryptanalyzer.entities.ActionType;
import ru.javarush.kornienko.cryptanalyzer.entities.CryptResult;

import java.nio.file.Path;
import java.util.List;

public class Decoder extends Action {
    @Override
    public CryptResult execute(String[] params) {
        Path srcPath = fileService.getPath(params[0]);
        Path destPath = fileService.getPath(params[1]);
        int key = -Integer.parseInt(params[2]);

        List<String> srcText = fileService.readFrom(srcPath);
        List<String> cipheredText = caesarCipher.doCipher(srcText, key, true);
        fileService.writeTo(destPath, cipheredText);

        return new CryptResult(CryptResult.SUCCESS_MESSAGE_KNOWN_KEY.formatted(ActionType.DECODE.getCommandName()));
    }
}
