package de.ostfale.qk.app;

import de.ostfale.qk.ui.dashboard.DashboardService;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@ApplicationScoped
public class ApplicationInitializer implements FileSystemFacade {

    private static final String DIRECTORY_CREATION_ERROR = "Failed to create application directory: %s";

    @Inject
    DashboardService dashboardService;

    void onStartup(@Observes StartupEvent ev) {
        Log.infof("Starting %s...", APP_NAME);
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
        Log.debugf("Startup :: Application home directory exists: %s", applicationHomeDir);
        createRequiredDirectories(applicationHomeDir);
        dashboardService.updateCurrentRankingStatus();
    }

    private void initializeNewDirectory(String applicationHomeDir) {
        Log.infof("Application home directory does not exist: %s -> is going to be created", applicationHomeDir);
        createDirectoryTree(Paths.get(applicationHomeDir));
        createRequiredDirectories(applicationHomeDir);
    }

    private void createRequiredDirectories(String applicationHomeDir) {
        Arrays.stream(DirTypes.values()).forEach(dirType -> {
            Log.debugf("Startup :: Ensure directory '%s' exists", dirType.displayName);
            createDirectoryTreeIfNotExists(applicationHomeDir, dirType.displayName);
        });
    }

    private void createDirectoryTreeIfNotExists(String parentDirectory, String directoryPath) {
        Path fullPath = Paths.get(parentDirectory, directoryPath);
        if (Files.exists(fullPath)) {
            Log.debugf("Directory tree already exists: %s", fullPath);
            return;
        }
        createDirectoryTree(fullPath);
    }
    
    private void createDirectoryTree(Path directoryPath) {
        try {
            Files.createDirectories(directoryPath); // This creates the entire directory tree
            Log.infof("Directory tree created: %s", directoryPath);
        } catch (IOException e) {
            String errorMessage = String.format(DIRECTORY_CREATION_ERROR, directoryPath);
            Log.errorf(errorMessage + ": %s", e.getMessage());
            throw new RuntimeException(errorMessage, e);
        }
    }
}
