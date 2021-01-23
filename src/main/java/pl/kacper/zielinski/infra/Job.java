package pl.kacper.zielinski.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.kacper.zielinski.utils.FileUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Component
public class Job {

    @Value("${directory.source}")
    private String initDirectory;

    private final FileUtils fileUtils = new FileUtils();
    private final HashMap<String, String> directoryToFullPathMap = new HashMap<>();
    private FileCounterUpdater fileCounterUpdater;

    @PostConstruct
    public void initCatalogStructure() {
        createDirectoriesPathMap();
        initializeCatalogStructure();
        fileCounterUpdater = new FileCounterUpdater(directoryToFullPathMap.get("HOME"));
    }

    @Scheduled(fixedDelay = 1000)
    public void moveFilesFromHomeDirectory() {
        try {
            Files.list(Paths.get(directoryToFullPathMap.get("HOME")))
                    .filter(Files::isRegularFile)
                    .filter(file -> !fileUtils.fileEndsWith(file, ".txt"))
                    .forEach(this::segregateFile);
        } catch (IOException e) {
            throw new RuntimeException("Cannot move files from home directory");
        }
    }

    private void createDirectoriesPathMap() {
        String initialDirectoryWithDelimiter = initDirectory + File.separator;
        directoryToFullPathMap.put("HOME", initialDirectoryWithDelimiter + "HOME");
        directoryToFullPathMap.put("DEV", initialDirectoryWithDelimiter + "DEV");
        directoryToFullPathMap.put("TEST", initialDirectoryWithDelimiter + "TEST");
    }

    private void initializeCatalogStructure() {
        CatalogStructureInitializer csi = new CatalogStructureInitializer();
        csi.initialize(initDirectory);
        directoryToFullPathMap.values().forEach(csi::initialize);
    }

    private void segregateFile(Path filepath) {
        if(fileUtils.fileEndsWith(filepath, ".xml")) {
            fileUtils.moveFileToFolder(filepath, directoryToFullPathMap.get("DEV"));
            fileCounterUpdater.updateDevCounter();
        } else if(fileUtils.fileEndsWith(filepath, ".jar")) {
            try {
                if(fileUtils.getCreationFileHour(filepath) % 2 == 0) {
                    fileUtils.moveFileToFolder(filepath, directoryToFullPathMap.get("DEV"));
                    fileCounterUpdater.updateDevCounter();
                } else {
                    fileUtils.moveFileToFolder(filepath, directoryToFullPathMap.get("TEST"));
                    fileCounterUpdater.updateTestCounter();
                }
            } catch (IOException e) {
                throw new RuntimeException("Cannot read data file creation time for path: " + filepath);
            }
        }
    }
}
