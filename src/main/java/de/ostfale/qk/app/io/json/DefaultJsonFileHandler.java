package de.ostfale.qk.app.io.json;

import de.ostfale.qk.app.io.FileHandler;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of JsonFileHandler that combines file I/O and JSON operations.
 * Delegates basic file operations to FileHandler and adds JSON serialization capabilities.
 *
 * @param <T> the type of objects to read/write
 */
@Dependent
public class DefaultJsonFileHandler<T> implements JsonFileHandler<T> {
    
    private final ConfiguredObjectMapper objectMapper;
    private final FileHandler fileHandler;

    @Inject
    public DefaultJsonFileHandler(ConfiguredObjectMapper objectMapper, FileHandler fileHandler) {
        this.objectMapper = objectMapper;
        this.fileHandler = fileHandler;
    }

    // FileHandler delegate methods

    public Optional<String> read(Path path) {
        return fileHandler.read(path);
    }

    public Optional<String> read(String path) {
        return fileHandler.read(path);
    }

    public boolean write(Path path, String content) {
        return fileHandler.write(path, content);
    }

    public boolean write(String path, String content) {
        return fileHandler.write(path, content);
    }

    public boolean exists(Path path) {
        return fileHandler.exists(path);
    }

    public boolean exists(String path) {
        return fileHandler.exists(path);
    }

    // JSON-specific methods

    @Override
    public Optional<T> readJson(Path path, Class<T> targetClass) {
        try {
            Optional<String> content = read(path);
            if (content.isEmpty()) {
                return Optional.empty();
            }
            return objectMapper.mapToClass(content.get(), targetClass);
        } catch (Exception e) {
            Log.errorf(e, "DefaultJsonFileHandler :: Failed to read JSON object from file: %s", path);
            return Optional.empty();
        }
    }

    @Override
    public Optional<T> readJson(String path, Class<T> targetClass) {
        return readJson(Paths.get(path), targetClass);
    }

    @Override
    public Optional<List<T>> readJsonList(Path path, Class<T> elementClass) {
        try {
            Optional<String> content = read(path);
            if (content.isEmpty()) {
                return Optional.empty();
            }
            return objectMapper.mapToList(content.get(), elementClass);
        } catch (Exception e) {
            Log.errorf(e, "DefaultJsonFileHandler :: Failed to read JSON list from file: %s", path);
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<T>> readJsonList(String path, Class<T> elementClass) {
        return readJsonList(Paths.get(path), elementClass);
    }

    @Override
    public Optional<Map<String, T>> readJsonMap(Path path, Class<T> valueType) {
        try {
            Optional<String> content = read(path);
            if (content.isEmpty()) {
                return Optional.empty();
            }
            return objectMapper.mapToStringMap(content.get(), valueType);
        } catch (Exception e) {
            Log.errorf(e, "DefaultJsonFileHandler :: Failed to read JSON map from file: %s", path);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Map<String, T>> readJsonMap(String path, Class<T> valueType) {
        return readJsonMap(Paths.get(path), valueType);
    }

    @Override
    public boolean writeJson(Path path, T object) {
        String jsonContent = objectMapper.toPrettyJsonString(object);
        boolean result = write(path, jsonContent);
        if (result) {
            Log.infof("DefaultJsonFileHandler :: Successfully wrote JSON object to file: %s", path);
        }
        return result;
    }

    @Override
    public boolean writeJson(String path, T object) {
        return writeJson(Paths.get(path), object);
    }

    @Override
    public Optional<Path> writeJsonWithResult(Path path, T object) {
        return writeJson(path, object) ? Optional.of(path) : Optional.empty();
    }

    @Override
    public Optional<Path> writeJsonWithResult(String path, T object) {
        Path filePath = Paths.get(path);
        return writeJsonWithResult(filePath, object);
    }

    @Override
    public ConfiguredObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
