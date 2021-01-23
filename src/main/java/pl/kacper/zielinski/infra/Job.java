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
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;

@Component
public class Job {

    @Value("${directory.source}")
    private String initDirectory;

    private final HashMap<String, String> directoryToFullPathMap = new HashMap<>();

    @PostConstruct
    public void initCatalogStructure() {
        createDirectoriesPathMap();
        initializeCatalogStructure();
    }

    @Scheduled(fixedDelay = 1000)
    public void moveFile() throws IOException {
        Files.list(Paths.get(directoryToFullPathMap.get("HOME")))
                .filter(Files::isRegularFile)
                .forEach(this::segregateFiles);
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

    private void segregateFiles(Path path) {
        FileUtils fu = new FileUtils();

        if(fu.fileEndsWith(path, ".xml")) {
            fu.moveFileToFolder(path, directoryToFullPathMap.get("DEV"));
        } else if(fu.fileEndsWith(path, ".jar")) {
            try {
                Instant creationDate = Files.readAttributes(path, BasicFileAttributes.class).creationTime().toInstant();
                LocalDateTime ldt = LocalDateTime.ofInstant(creationDate, ZoneId.systemDefault());
                int creationHour = ldt.getHour();

                if(creationHour % 2 == 0) {
                    fu.moveFileToFolder(path, directoryToFullPathMap.get("DEV"));
                } else {
                    fu.moveFileToFolder(path, directoryToFullPathMap.get("TEST"));
                }
            } catch (IOException e) {
                throw new RuntimeException("Cannot read data file creation time for path: " + path);
            }
        }
    }
}
