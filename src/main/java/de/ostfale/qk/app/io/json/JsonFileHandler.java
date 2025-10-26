package de.ostfale.qk.app.io.json;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Generic interface for JSON file operations.
 * Extends FileHandler to add JSON-specific serialization and deserialization capabilities.
 *
 * @param <T> the type of objects to read/write
 */
public interface JsonFileHandler<T> {

    Optional<T> readJson(Path path, Class<T> targetClass);

    Optional<T> readJson(String path, Class<T> targetClass);

    Optional<List<T>> readJsonList(Path path, Class<T> elementClass);

    Optional<List<T>> readJsonList(String path, Class<T> elementClass);

    Optional<Map<String, T>> readJsonMap(Path path, Class<T> valueType);

    Optional<Map<String, T>> readJsonMap(String path, Class<T> valueType);

    boolean writeJson(Path path, T object);

    boolean writeJson(String path, T object);

    Optional<Path> writeJsonWithResult(Path path, T object);

    Optional<Path> writeJsonWithResult(String path, T object);

    ConfiguredObjectMapper getObjectMapper();
}
