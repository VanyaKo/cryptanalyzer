package cryptanalyzer.commands;

import cryptanalyzer.consts.Actions;
import cryptanalyzer.entity.Result;
import cryptanalyzer.utils.CaesarCipher;

import java.nio.file.Path;

public class Decoder implements Action{
    @Override
    public Result execute(String[] params) {
        CaesarCipher.applyCipherToText(Path.of(params[0]), Path.of(params[1]),
                -Integer.parseInt(params[2]), true);
        return new Result(Result.SUCCESS_MESSAGE_KNOWN_KEY.formatted(Actions.DECODE.getCommandName()));
    }
}
