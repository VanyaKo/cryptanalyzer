package cryptanalyzer.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    public static final String PATH_TO_LOG_DIR = "logs";
    public static final String PATH_TO_LOG_FILE = PATH_TO_LOG_DIR + "/%s.log";
    private final FileService fileService;

    public Logger(FileService fileService) {
        this.fileService = fileService;
    }

    public void logException(Exception e) {
        DateTimeFormatter logFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'hh-mm-ss");
        String path = String.format(PATH_TO_LOG_FILE, LocalDateTime.now().format(logFormatter));
        fileService.createPath(PATH_TO_LOG_DIR, false);
        fileService.createPath(path, true);
        fileService.writeTo(fileService.getPath(path), getList(e));
    }

    private List<String> getList(Exception e) {
        List<String> list = new ArrayList<>();
        list.add("ERROR: " + e.getMessage() + "\n");
        for(StackTraceElement element : e.getStackTrace()) {
            list.add(element.toString());
        }
        return list;
    }
}
