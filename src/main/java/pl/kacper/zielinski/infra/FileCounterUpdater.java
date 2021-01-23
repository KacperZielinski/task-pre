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
        getCounterAndIncrement(3);
        updateOverallCounter();
    }

    public void updateTestCounter() {
        getCounterAndIncrement(4);
        updateOverallCounter();
    }

    public void updateOverallCounter() {
        getCounterAndIncrement(2);
    }

    private void getCounterAndIncrement(int i) {
        int counter = fileUtils.getCounterFromLine(counterPath, i);
        counter++;

        fileUtils.writeToFileOnLine(counterPath, i, String.valueOf(counter));
    }
}
