package de.ostfale.qk.app.config;

import de.ostfale.qk.app.FileSystemFacade;
import de.ostfale.qk.data.json.ConfiguredObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.Optional;

@Singleton
public class ConfigPersistenceService implements JsonPersistenceService {

    Logger log = Logger.getLogger(FileSystemFacade.class);

    @Inject
    ConfiguredObjectMapper objectMapper;

    @Override
    public void writeConfiguration(String targetFilePath, AppConfiguration appConfiguration) {
        log.debugf("Write configuration to %s", targetFilePath);
        try {
            FileSystemFacade.writeToFile(targetFilePath, objectMapper.toPrettyJsonString(appConfiguration));
        } catch (IOException e) {
            log.error("Failed to write configuration to file", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AppConfiguration> readConfiguration(String targetFilePath) {
        try {
            log.debugf("Read configuration from %s", targetFilePath);
            String configJson = FileSystemFacade.readFileContentAsText(targetFilePath);
            return objectMapper.mapToClass(configJson, AppConfiguration.class);
        } catch (IOException e) {
            log.error("Failed to read configuration from file", e);
            return Optional.empty();
        }
    }
}
