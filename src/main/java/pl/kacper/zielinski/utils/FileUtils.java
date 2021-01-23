package pl.kacper.zielinski.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

}
