package ru.javarush.kornienko.cryptanalyzer.commands;

import ru.javarush.kornienko.cryptanalyzer.entities.ActionType;
import ru.javarush.kornienko.cryptanalyzer.entities.CryptResult;
import ru.javarush.kornienko.cryptanalyzer.services.CaesarCipher;
import ru.javarush.kornienko.cryptanalyzer.services.FileService;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public abstract class Action {
    protected final CaesarCipher caesarCipher;
    protected final FileService fileService;

    protected Action() {
        this.fileService = new FileService();
        this.caesarCipher = new CaesarCipher();
    }

    public abstract CryptResult execute(String[] params);

    protected <K, V extends Number> double getKeyCount(Map<K, V> map, K wordBegin) {
        return map.containsKey(wordBegin) ? (Double) map.get(wordBegin) + 1 : 1;
    }

    protected CryptResult encode(String[] params) {
        Path srcPath = fileService.getPath(params[0]);
        Path destPath = fileService.getPath(params[1]);
        int key = parseKey(params);

        List<String> srcText = fileService.readFrom(srcPath);
        List<String> cipheredText = caesarCipher.doCipher(srcText, key, isDecodeMode());
        fileService.writeTo(destPath, cipheredText);

        return new CryptResult(CryptResult.SUCCESS_MESSAGE_KNOWN_KEY.formatted(ActionType.ENCODE.getCommandName()));
    }

    protected boolean isDecodeMode() {
        return false;
    }

    protected int parseKey(String[] params) {
        return Integer.parseInt(params[2]);
    }
}
