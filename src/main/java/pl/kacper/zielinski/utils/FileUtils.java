package pl.kacper.zielinski.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class FileUtils {

    public boolean fileEndsWith(Path path, String suffix) {
        return path.toString().toLowerCase().endsWith(suffix);
    }

    public void moveFileToFolder(Path source, String folderPath) {
        try {
            String movedFileFullPath = folderPath +
                    File.separator +
                    source.getFileName().toString();

            Files.move(source, Paths.get(movedFileFullPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Cannot move file:" + source + " to: " + folderPath);
        }
    }

    public int getCreationFileHour(Path path) throws IOException {
        Instant creationDate = Files.readAttributes(path, BasicFileAttributes.class).creationTime().toInstant();
        LocalDateTime ldt = LocalDateTime.ofInstant(creationDate, ZoneId.systemDefault());
        return ldt.getHour();
    }

    public void createFileIfNotExists(String counterPath) {
        File f = new File(counterPath);

        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public int getCounterFromLine(String filepath, int lineNumber) {
        Path path = Paths.get(filepath);
        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            return Integer.parseInt(lines.get(lineNumber));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void writeToFileOnLine(String filepath, int lineNumber, String data) {
        Path path = Paths.get(filepath);
        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            if(lines.isEmpty()) {
                lines.add(data);
                lines.add("0");
                lines.add("0");
                lines.add("0");
            }
            lines.set(lineNumber - 1, data);
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
