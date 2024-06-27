package ru.javarush.kornienko.cryptanalyzer.commands;

import ru.javarush.kornienko.cryptanalyzer.entities.CryptResult;

public class Decoder extends Action {
    @Override
    public CryptResult execute(String[] params) {
        return encode(params);
    }

    @Override
    protected boolean isDecodeMode() {
        return true;
    }

    @Override
    protected int parseKey(String[] params) {
        return -super.parseKey(params);
    }
}
