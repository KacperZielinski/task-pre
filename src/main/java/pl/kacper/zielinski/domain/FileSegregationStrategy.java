package pl.kacper.zielinski.domain;

import java.nio.file.Path;

public interface FileSegregationStrategy {

    default boolean pathEndsWith(Path path) {
        return false;
    }
}
