package de.ostfale.qk.data.json;

import de.ostfale.qk.app.io.json.ConfiguredObjectMapper;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Dependent
public class DefaultJsonFileReader<T> implements JsonFileReader<T> {

    private static final Logger log = Logger.getLogger(DefaultJsonFileReader.class);

    private final ConfiguredObjectMapper configuredObjectMapper;

    @Inject
    public DefaultJsonFileReader(ConfiguredObjectMapper configuredObjectMapper) {
        this.configuredObjectMapper = configuredObjectMapper;
    }

    @Override
    public Optional<T> readFromFile(String path, Class<T> targetClass) {
        return readFromFile(Paths.get(path), targetClass);
    }

    @Override
    public Optional<T> readFromFile(Path path, Class<T> targetClass) {
        try {
            if (!Files.exists(path)) {
                log.warnf("File not found: %s", path);
                return Optional.empty();
            }
            String jsonContent = Files.readString(path);
            return configuredObjectMapper.mapToClass(jsonContent, targetClass);
        } catch (Exception e) {
            log.errorf("Failed to read object from file: %s", path, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<T>> readListFromFile(String path, Class<T> elementClass) {
        return readListFromFile(Paths.get(path), elementClass);
    }

    @Override
    public Optional<List<T>> readListFromFile(Path path, Class<T> elementClass) {
        try {
            if (!Files.exists(path)) {
                log.warnf("File not found: %s", path);
                return Optional.empty();
            }

            String jsonContent = Files.readString(path);
            return configuredObjectMapper.mapToList(jsonContent, elementClass);
        } catch (IOException e) {
            log.errorf("Failed to read list from file: %s", path, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Map<String, T>> readJsonFileToMap(String filePath, Class<T> valueType) {
        try {
            String jsonContent = Files.readString(Path.of(filePath));
            return getObjectMapper().mapToStringMap(jsonContent, valueType);
        } catch (IOException e) {
            log.errorf("Error reading JSON file: %s", filePath, e);
            return Optional.empty();
        }
    }


    @Override
    public Optional<Map<String, T>> readJsonFileToMap(Path path, Class<T> valueType) {
        try {
            String jsonContent = Files.readString(path);
            return getObjectMapper().mapToStringMap(jsonContent, valueType);
        } catch (IOException e) {
            log.errorf("Error reading JSON file: %s", path, e);
            return Optional.empty();
        }
    }

    @Override
    public ConfiguredObjectMapper getObjectMapper() {
        return configuredObjectMapper;
    }
}
