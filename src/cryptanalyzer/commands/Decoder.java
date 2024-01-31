package cryptanalyzer.commands;

import cryptanalyzer.FileService;
import cryptanalyzer.consts.Actions;
import cryptanalyzer.entity.Result;
import cryptanalyzer.model.CryptModel;
import cryptanalyzer.utils.CaesarCipher;

import java.nio.file.Path;

public class Decoder implements Action{
    @Override
    public Result execute(String[] params) {
        FileService fileService = new FileService();
        Path srcPath = fileService.getPath(params[0]);
        Path destPath = fileService.getPath(params[1]);
        int key = -Integer.parseInt(params[2]);

        String srcText = fileService.readFrom(srcPath);
        CaesarCipher caesarCipher = new CaesarCipher();
        String cipheredText = caesarCipher.doCipher(srcText, key, false);
        fileService.writeTo(destPath, cipheredText);

        return new Result(Result.SUCCESS_MESSAGE_KNOWN_KEY.formatted(Actions.DECODE.getCommandName()));
    }
}
