package de.ostfale.qk.app;

import de.ostfale.qk.app.config.AppConfiguration;
import de.ostfale.qk.app.config.ConfigPersistenceService;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;

@ApplicationScoped
public class ApplicationInitializer implements FileSystemFacade {

    private static final Logger log = Logger.getLogger(ApplicationInitializer.class);

    @Inject
    DevSimulation devSimulation;

    @Inject
    ConfigPersistenceService configPersistenceService;

    void onShutdown(@Observes @Priority(1) ShutdownEvent ev) {
        log.infof("Shutting down %s...", APP_NAME);
        writeApplicationStartTimestampToConfiguration();
    }

    void onStartup(@Observes StartupEvent ev) {
        log.infof("Starting %s...", APP_NAME);
        String applicationHomeDir = getApplicationHomeDir();
        if (directoryExists(applicationHomeDir)) {
            log.infof("Application home directory exists: %s", applicationHomeDir);
            ensureDirectoriesExist(applicationHomeDir);
            checkForDevProfileActions();
            ensureConfigurationFileExists(applicationHomeDir);
        } else {
            log.infof("Application home directory does not exist: %s -> is going to be created", applicationHomeDir);
        }
    }

    private void ensureDirectoriesExist(String applicationHomeDir) {
        Arrays.stream(DirTypes.values()).forEach(dirType -> {
            log.infof("Ensure directory '%s' exists", dirType.displayName);
            ensureDirectoryExists(applicationHomeDir, dirType.displayName);
        });
    }

    private void ensureDirectoryExists(String parentDirectory, String directoryName) {
        Path directoryPath = Paths.get(parentDirectory, directoryName);

        if (Files.isDirectory(directoryPath)) {
            log.infof("Directory already exists: %s", directoryPath);
            return;
        }

        try {
            Files.createDirectory(directoryPath);
            log.infof("Directory created: %s", directoryPath);
        } catch (IOException e) {
            log.errorf("Failed to create directory '%s': %s", directoryPath, e.getMessage());
            throw new RuntimeException("Exception creating application directory!", e);
        }
    }

    private void ensureConfigurationFileExists(String applicationHomeDir) {
        String configurationFilePath = applicationHomeDir + SEP + CONFIGURATION_DIR_NAME + SEP + CONFIGURATION_FILE_NAME;
        Path path = Paths.get(configurationFilePath);
        if (Files.exists(path)) {
            log.infof("Configuration file already exists: %s", configurationFilePath);
            return;
        }
        AppConfiguration appConfiguration = new AppConfiguration();
        configPersistenceService.writeConfiguration(configurationFilePath, appConfiguration);
        log.infof("Configuration file created: %s", configurationFilePath);
    }

    private void writeApplicationStartTimestampToConfiguration() {
        String configurationFilePath = getApplicationHomeDir() + SEP + CONFIGURATION_DIR_NAME + SEP + CONFIGURATION_FILE_NAME;
        var dd = configPersistenceService.readConfiguration(configurationFilePath);

        configPersistenceService.readConfiguration(configurationFilePath).ifPresent(config -> {
            log.infof("Write last application start time to configuration file: %s", configurationFilePath);
            config.setLastApplicationStart(LocalDateTime.now());
            configPersistenceService.writeConfiguration(configurationFilePath, config);
        });
    }

    private void checkForDevProfileActions() {
        var profile = ConfigUtils.isProfileActive("dev");
        if (profile) {
            log.info("Running Application in DEV mode. Load Simulation!");
            devSimulation.loadSimulationData();
        }
    }
}
