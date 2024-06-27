package ru.javarush.kornienko.cryptanalyzer.commands;

import ru.javarush.kornienko.cryptanalyzer.entities.CryptResult;

public class Encoder extends Action {
    @Override
    public CryptResult execute(String[] params) {
        return encode(params);
    }
}
