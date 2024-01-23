package cryptanalyzer;

import cryptanalyzer.entity.Result;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

@Command(name = "cypher", subcommands = {CommandLine.HelpCommand.class},
        description = "Caesar cypher command")
public class PicocliRunner implements Runnable {
    private final Application application = new Application();
    @Spec
    CommandSpec spec;

    @Command(name = "encrypt", description = "Encrypt from file to file using key")
    void encrypt(
            @Parameters(paramLabel = "<src file>", description = "source file with text to encrypt") String src,
            @Parameters(paramLabel = "<dest file>", description = "dest file which should have encrypted text") String dest,
            @Parameters(paramLabel = "<key>", description = "key for encryption") String key) {
        processInput("encode", src, dest, key);
    }

    private void processInput(String... args) {
        Result result = application.run(args);
        System.out.println(result);
    }

    @Command(name = "brute force", description = "Decrypt from file to file using brute force")
        // |3|
    void bruteForce(
            @Parameters(paramLabel = "<src file>", description = "source file with encrypted text") String src,
            @Option(names = {"-r", "--representative"}, description = "file with unencrypted representative text") String representative,
            @Parameters(paramLabel = "<dest file>", description = "dest file which should have decrypted text") String dest) {
        processInput("brute_force", src, representative, dest);
    }

    @Command(name = "statistical decryption", description = "Decrypt from file to file using statistical analysis")
        // |3|
    void statisticalDecrypt(
            @Parameters(paramLabel = "<src file>", description = "source file with encrypted text") String src,
            @Option(names = {"-r", "--representative"}, description = "file with unencrypted representative text") String representative,
            @Parameters(paramLabel = "<dest file>", description = "dest file which should have decrypted text") String dest) {
        processInput("analyze", src, representative, dest);

    }


    @Command(name = "decrypt", description = "Decrypt from file to file using statistical analysis")
        // |3|
    void decrypt(
            @Parameters(paramLabel = "<src file>", description = "source file with encrypted text") String src,
            @Parameters(paramLabel = "<dest file>", description = "dest file which should have decrypted text") String dest,
            @Parameters(paramLabel = "<key>", description = "key for encryption") String key) {
        processInput("decode", src, dest, key);

    }

    @Override
    public void run() {
        throw new ParameterException(spec.commandLine(), "Specify a subcommand");
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new PicocliRunner()).execute(args);
        System.exit(exitCode);
    }
}