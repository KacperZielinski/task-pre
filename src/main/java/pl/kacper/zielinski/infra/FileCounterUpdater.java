package pl.kacper.zielinski.infra;

import pl.kacper.zielinski.utils.FileUtils;

import java.io.File;

public class FileCounterUpdater {
    private static final String COUNTER_FILE = "count.txt";

    private final FileUtils fileUtils = new FileUtils();
    private String counterPath;

    public FileCounterUpdater(String homePath) {
        this.counterPath = homePath + File.separator + COUNTER_FILE;

        fileUtils.createFileIfNotExists(counterPath);
        fileUtils.writeToFileOnLine(counterPath, 1, "Overall counter line 2, dev counter line 3, test counter line 4");
    }

    public void updateDevCounter() {
        int counter = fileUtils.getCounterFromLine(counterPath, 3);
        counter++;

        fileUtils.writeToFileOnLine(counterPath, 3,  String.valueOf(counter));
        updateOverallCounter(counter);
    }

    public void updateTestCounter() {
        int counter = fileUtils.getCounterFromLine(counterPath, 4);
        counter++;

        fileUtils.writeToFileOnLine(counterPath, 4,  String.valueOf(counter));
        updateOverallCounter(counter);
    }

    public void updateOverallCounter(int counter) {
        fileUtils.writeToFileOnLine(counterPath, 2,  String.valueOf(counter));
    }
}
