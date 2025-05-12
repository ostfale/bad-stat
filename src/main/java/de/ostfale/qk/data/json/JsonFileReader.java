package de.ostfale.qk.data.json;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JsonFileReader<T> {

    Optional<T> readFromFile(String path, Class<T> targetClass);

    Optional<T> readFromFile(Path path, Class<T> targetClass);

    Optional<List<T>> readListFromFile(String path, Class<T> elementClass);

    Optional<List<T>> readListFromFile(Path path, Class<T> elementClass);

    Optional<Map<String, T>> readJsonFileToMap(String filePath, Class<T> valueType);

    Optional<Map<String, T>> readJsonFileToMap(Path path, Class<T> valueType);

    ConfiguredObjectMapper getObjectMapper();
}
