package ru.javarush.kornienko.cryptanalyzer.entities;

public class CryptResult {
    public static final String SUCCESS_MESSAGE_UNKNOWN_KEY = "%s is successfully completed. Found key is %d.";
    public static final String SUCCESS_MESSAGE_KNOWN_KEY = "%s is successfully completed.";
    public static final String FAIL_MESSAGE = "%s is failed.";
    private final String message;

    @Override
    public String toString() {
        return message;
    }

    public CryptResult(String message) {
        this.message = message;
    }
}
