package cryptanalyzer.commands;

import cryptanalyzer.entity.Actions;
import cryptanalyzer.entity.Result;

import java.nio.file.Path;
import java.util.List;

public class Decoder extends Action {
    @Override
    public Result execute(String[] params) {
        Path srcPath = fileService.getPath(params[0]);
        Path destPath = fileService.getPath(params[1]);
        int key = -Integer.parseInt(params[2]);

        List<String> srcText = fileService.readFrom(srcPath);
        List<String> cipheredText = caesarCipher.doCipher(srcText, key, false);
        fileService.writeTo(destPath, cipheredText);

        return new Result(Result.SUCCESS_MESSAGE_KNOWN_KEY.formatted(Actions.DECODE.getCommandName()));
    }
}
