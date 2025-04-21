package de.ostfale.qk.db.json;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Dependent
public class DefaultJsonFileWriter<T> implements JsonFileWriter<T> {

    private static final Logger log = Logger.getLogger(DefaultJsonFileWriter.class);

    private final ConfiguredObjectMapper objectMapper;

    @Inject
    public DefaultJsonFileWriter(ConfiguredObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean writeToFile(T object, String path) {
        return writeToFile(object, Paths.get(path));
    }

    @Override
    public boolean writeToFile(T object, Path path) {
        try {
            Files.createDirectories(path.getParent());   // Create parent directories if they don't exist
            String jsonContent = objectMapper.toPrettyJsonString(object);
            Files.writeString(path, jsonContent);
            log.infof("Successfully wrote object to file: %s", path);
            return true;
        } catch (IOException e) {
            log.errorf("Failed to write object to file: %s", path, e);
            return false;
        }
    }

    @Override
    public Optional<Path> writeToFileWithResult(T object, String path) {
        Path filePath = Paths.get(path);
        return writeToFile(object, filePath) ? Optional.of(filePath) : Optional.empty();
    }

    @Override
    public ConfiguredObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
