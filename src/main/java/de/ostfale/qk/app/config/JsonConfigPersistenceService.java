package de.ostfale.qk.app.config;

import de.ostfale.qk.app.FileSystemFacade;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static de.ostfale.qk.app.FileSystemFacade.SEP;
import static de.ostfale.qk.app.FileSystemFacade.getApplicationHome;

@Singleton
public class JsonConfigPersistenceService implements ConfigPersistenceService {

    Logger log = Logger.getLogger(FileSystemFacade.class);

    @Inject
    ConfiguredObjectMapper objectMapper;

    private static final String CONFIGURATION_DIR_NAME = "config";
    private static final String CONFIGURATION_FILE_NAME = "app-config.json";

    @Override
    public Path writeConfiguration(String targetFilePath, AppConfiguration appConfiguration) {
        log.debugf("Write configuration to %s", targetFilePath);
        try {
            return FileSystemFacade.writeToFile(targetFilePath, objectMapper.toPrettyJsonString(appConfiguration));
        } catch (IOException e) {
            log.error("Failed to write configuration to file", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AppConfiguration> readConfiguration() {
        try {
            log.debugf("Read configuration from %s", getTargetPath());
            String configJson = FileSystemFacade.readFileContentAsText(getTargetPath());
            return objectMapper.mapToClass(configJson, AppConfiguration.class);
        } catch (IOException e) {
            log.error("Failed to read configuration from file", e);
            return Optional.empty();
        }
    }

    private String getTargetPath() {
        return getApplicationHome() + SEP + CONFIGURATION_DIR_NAME + SEP + CONFIGURATION_FILE_NAME;
    }
}
