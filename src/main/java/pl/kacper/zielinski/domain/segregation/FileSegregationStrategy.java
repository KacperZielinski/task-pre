package pl.kacper.zielinski.domain.segregation;

import java.nio.file.Path;

public interface FileSegregationStrategy {

    default boolean pathEndsWith(Path path) {
        return false;
    }
}
