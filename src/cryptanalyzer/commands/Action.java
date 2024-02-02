package cryptanalyzer.commands;

import cryptanalyzer.FileService;
import cryptanalyzer.entity.Result;
import cryptanalyzer.utils.CaesarCipher;

public abstract class Action {
    protected final CaesarCipher caesarCipher;
    protected final FileService fileService;

    protected Action() {
        this.fileService = new FileService();
        this.caesarCipher = new CaesarCipher();
    }

    public abstract Result execute(String[] params);
}
