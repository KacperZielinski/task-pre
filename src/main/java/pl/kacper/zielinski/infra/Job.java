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
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Component
public class Job {

    @Value("${directory.source}")
    private String directory;

    private String homeDirectory;

    @PostConstruct
    public void initCatalogStructure() {
        homeDirectory = directory + "HOME";

        String initialDirectoryWithDelimiter = directory + File.separator;
        ensureDirectoryExistence(directory);
        ensureDirectoryExistence(initialDirectoryWithDelimiter + "HOME");
        ensureDirectoryExistence(initialDirectoryWithDelimiter + "DEV");
        ensureDirectoryExistence(initialDirectoryWithDelimiter + "TEST");

    }

    private void ensureDirectoryExistence(String folderPath) {
        File dir = new File(folderPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void moveFile() throws IOException {
//        System.out.println(homeDirectory);

        Files.list(Paths.get(homeDirectory))
                .filter(Files::isRegularFile)
                .forEach(path -> segregateFiles(path));

        // todo here some logic
    }

    private void segregateFiles(Path path) {
        // zrobic hashmape i piewrszy napotkany ktory zwroci prawde wykonac
        System.out.println(path);
        System.out.println(fileEndsWith(path, ".xml"));

        if(fileEndsWith(path, ".xml")) {
            // move to DEV

        } else if(fileEndsWith(path, ".jar")) {
            try {
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

                Instant creationDate = attr.creationTime().toInstant();
                LocalDateTime ldt = LocalDateTime.ofInstant(creationDate, ZoneId.systemDefault());

                System.out.println(ldt.getHour());
                int creationHour = ldt.getHour();

                if(creationHour % 2 == 0) {
                    // move to DEV
                } else {
                    // move to TEST
                }

            } catch (IOException e) {
                throw new RuntimeException("Cannot read data file creation time for path: " + path);
            }
        }
    }

    private boolean fileEndsWith(Path path, String suffix) {
        return path.toString().toLowerCase().endsWith(suffix);
    }
}
