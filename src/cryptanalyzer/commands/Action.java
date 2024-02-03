package cryptanalyzer.commands;

import cryptanalyzer.services.FileService;
import cryptanalyzer.entities.CryptResult;
import cryptanalyzer.services.CaesarCipher;

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

}
