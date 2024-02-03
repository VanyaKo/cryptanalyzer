package cryptanalyzer.commands;

import cryptanalyzer.entities.ActionType;
import cryptanalyzer.entities.CryptResult;

import java.nio.file.Path;
import java.util.List;

public class Encoder extends Action {
    @Override
    public CryptResult execute(String[] params) {
        Path srcPath = fileService.getPath(params[0]);
        Path destPath = fileService.getPath(params[1]);
        int key = Integer.parseInt(params[2]);

        List<String> srcText = fileService.readFrom(srcPath);
        List<String> cipheredText = caesarCipher.doCipher(srcText, key, false);
        fileService.writeTo(destPath, cipheredText);

        return new CryptResult(CryptResult.SUCCESS_MESSAGE_KNOWN_KEY.formatted(ActionType.ENCODE.getCommandName()));
    }
}
