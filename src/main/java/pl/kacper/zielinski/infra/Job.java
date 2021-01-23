package pl.kacper.zielinski.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;

@Component
public class Job {

    @Value("${directory.source}")
    private String directory;

    private final HashMap<String, String> directoryToFullPathMap = new HashMap<>();

    @PostConstruct
    public void initCatalogStructure() {
        String initialDirectoryWithDelimiter = directory + File.separator; // todo zmienc na funkcje
        directoryToFullPathMap.put("HOME", initialDirectoryWithDelimiter + "HOME");
        directoryToFullPathMap.put("DEV", initialDirectoryWithDelimiter + "DEV" + File.separator);
        directoryToFullPathMap.put("TEST", initialDirectoryWithDelimiter + "TEST" + File.separator);

        ensureDirectoryExistence(directory);
        directoryToFullPathMap.values().forEach(this::ensureDirectoryExistence);
    }

    private void ensureDirectoryExistence(String folderPath) {
        File dir = new File(folderPath);
        // check is that really a directory not a file!
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void moveFile() throws IOException {
        Files.list(Paths.get(directoryToFullPathMap.get("HOME")))
                .filter(Files::isRegularFile)
                .forEach(this::segregateFiles);
    }

    private void segregateFiles(Path path) {
//        System.out.println(path);
//        System.out.println(fileEndsWith(path, ".xml"));

        if(fileEndsWith(path, ".xml")) {
            moveFileToFolder(path, "DEV");
        } else if(fileEndsWith(path, ".jar")) {
            try {
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

                Instant creationDate = attr.creationTime().toInstant();
                LocalDateTime ldt = LocalDateTime.ofInstant(creationDate, ZoneId.systemDefault());

//                System.out.println(ldt.getHour());
                int creationHour = ldt.getHour();

                if(creationHour % 2 == 0) {
                    moveFileToFolder(path, "DEV");
                } else {
                    moveFileToFolder(path, "TEST");
                }

            } catch (IOException e) {
                throw new RuntimeException("Cannot read data file creation time for path: " + path);
            }
        }
    }

    private boolean fileEndsWith(Path path, String suffix) {
        return path.toString().toLowerCase().endsWith(suffix);
    }

    private void moveFileToFolder(Path source, String folderName) {
        try {
            System.out.println(source);
            System.out.println(Paths.get(directoryToFullPathMap.get(folderName)));

            String movedFileFullPath = directoryToFullPathMap.get(folderName) +
                    File.separator +
                    source.getFileName().toString();

            Files.move(source, Paths.get(movedFileFullPath), StandardCopyOption.REPLACE_EXISTING);

//            source.toFile().renameTo(new File(
//                    directoryToFullPathMap.get(folderName) +
//                            File.separator +
//                            source.getFileName().toString())
//            );
        } catch (IOException e) {
            throw new RuntimeException("Cannot move file:" + source + " to: " + folderName);
        }
    }
}
