package de.ostfale.qk.db.json;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface JsonFileReader<T> {

    Optional<T> readFromFile(String path, Class<T> targetClass);

    Optional<T> readFromFile(Path path, Class<T> targetClass);

    Optional<List<T>> readListFromFile(String path, Class<T> elementClass);

    Optional<List<T>> readListFromFile(Path path, Class<T> elementClass);

    ConfiguredObjectMapper getObjectMapper();
}
