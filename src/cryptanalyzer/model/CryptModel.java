package cryptanalyzer.model;

public class CryptModel {
    private final String pathFrom;
    private final String pathTo;
    private final int key;

    public String getPathFrom() {
        return pathFrom;
    }

    public String getPathTo() {
        return pathTo;
    }

    public int getKey() {
        return key;
    }

    public CryptModel(String pathFrom, String pathTo, int key) {
        this.pathFrom = pathFrom;
        this.pathTo = pathTo;
        this.key = key;
    }
}
