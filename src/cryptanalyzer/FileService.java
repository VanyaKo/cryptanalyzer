package cryptanalyzer;

import cryptanalyzer.exception.AppException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileService {
    public Path getPath(String inputPath) {
        Path path = Path.of(inputPath);
        if(Files.isExecutable(path)) {
            throw new AppException("Cannot operator with " + path.getFileName() + " executable file!");
        }
        return path;
    }

    public String readFrom(Path file) {
        try {
            return Files.readString(file);
        } catch(IOException e) {
            throw new AppException("Cannot read data from " + file.getFileName() + " file!", e.getCause());
        }
    }

    public void writeTo(Path file, String text) {
        try {
            Files.writeString(file, text);
        } catch(IOException e) {
            throw new AppException("Cannot write data to " + file.getFileName() + "file!", e.getCause());
        }
    }
}
