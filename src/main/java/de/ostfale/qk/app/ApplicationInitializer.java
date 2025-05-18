package de.ostfale.qk.app;

import de.ostfale.qk.data.dashboard.RankingPlayerCacheHandler;
import de.ostfale.qk.ui.dashboard.DashboardService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@ApplicationScoped
public class ApplicationInitializer implements FileSystemFacade {

    private static final Logger log = Logger.getLogger(ApplicationInitializer.class);

    private static final String DIRECTORY_CREATION_ERROR = "Failed to create application directory: %s";

    @Inject
    RankingPlayerCacheHandler rankingPlayerCacheHandler;

    @Inject
    DashboardService dashboardService;

    void onStartup(@Observes StartupEvent ev) {
        log.infof("Starting %s...", APP_NAME);
        initializeApplication();
    }

    private void initializeApplication() {
        String applicationHomeDir = getApplicationHomeDir();
        if (applicationHomeDir == null) {
            throw new IllegalStateException("Application home directory path cannot be null");
        }

        if (directoryExists(applicationHomeDir)) {
            initializeExistingDirectory(applicationHomeDir);
        } else {
            initializeNewDirectory(applicationHomeDir);
        }
    }

    private void initializeExistingDirectory(String applicationHomeDir) {
        log.infof("Application home directory exists: %s", applicationHomeDir);
        createRequiredDirectories(applicationHomeDir);
        initializeRankingData();
        dashboardService.updateCurrentRankingStatus();
    }

    private void initializeNewDirectory(String applicationHomeDir) {
        log.infof("Application home directory does not exist: %s -> is going to be created", applicationHomeDir);
        createDirectory(Paths.get(applicationHomeDir));
        createRequiredDirectories(applicationHomeDir);
    }

    private void initializeRankingData() {
        log.info("Loading existing ranking file");
        if (rankingPlayerCacheHandler.loadLocalRankingFileIntoCache()) {
            log.info("No existing ranking file found -> no player data at startup");
        }
    }

    private void createRequiredDirectories(String applicationHomeDir) {
        Arrays.stream(DirTypes.values()).forEach(dirType -> {
            log.infof("Ensure directory '%s' exists", dirType.displayName);
            createDirectoryIfNotExists(applicationHomeDir, dirType.displayName);
        });
    }

    private void createDirectoryIfNotExists(String parentDirectory, String directoryName) {
        Path directoryPath = Paths.get(parentDirectory, directoryName);
        if (Files.isDirectory(directoryPath)) {
            log.infof("Directory already exists: %s", directoryPath);
            return;
        }
        createDirectory(directoryPath);
    }

    private void createDirectory(Path directoryPath) {
        try {
            Files.createDirectory(directoryPath);
            log.infof("Directory created: %s", directoryPath);
        } catch (IOException e) {
            String errorMessage = String.format(DIRECTORY_CREATION_ERROR, directoryPath);
            log.errorf(errorMessage + ": %s", e.getMessage());
            throw new RuntimeException(errorMessage, e);
        }
    }

}
