package de.ostfale.qk.app.config;

import de.ostfale.qk.app.FileSystemFacade;
import de.ostfale.qk.app.io.json.ConfiguredObjectMapper;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.util.Optional;

@Singleton
public class ConfigPersistenceService implements JsonPersistenceService {

    @Inject
    ConfiguredObjectMapper objectMapper;

    @Override
    public void writeConfiguration(String targetFilePath, AppConfiguration appConfiguration) {
        Log.debugf("Write configuration to %s", targetFilePath);
        try {
            FileSystemFacade.writeToFile(targetFilePath, objectMapper.toPrettyJsonString(appConfiguration));
        } catch (IOException e) {
            Log.error("Failed to write configuration to file", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AppConfiguration> readConfiguration(String targetFilePath) {
        try {
            Log.debugf("Read configuration from %s", targetFilePath);
            String configJson = FileSystemFacade.readFileContentAsText(targetFilePath);
            return objectMapper.mapToClass(configJson, AppConfiguration.class);
        } catch (IOException e) {
            Log.error("Failed to read configuration from file", e);
            return Optional.empty();
        }
    }
}
