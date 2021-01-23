package pl.kacper.zielinski.infra;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CatalogStructureInitializer {

    public void initialize(String directory) {
        ensureDirectoryExistence(directory);
    }

    private void ensureDirectoryExistence(String folderPath) {
        if (!Files.isDirectory(Paths.get(folderPath))) {
            new File(folderPath).mkdirs();
        }
    }
}
