package de.ostfale.qk.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.jboss.logging.Logger;

import de.ostfale.qk.db.service.PlayerServiceProvider;
import de.ostfale.qk.parser.ranking.api.RankingParser;
import de.ostfale.qk.ui.dashboard.DashboardService;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class ApplicationInitializer implements FileSystemFacade {

    private static final Logger log = Logger.getLogger(ApplicationInitializer.class);

    @Inject
    DevSimulation devSimulation;

    @Inject
    RankingParser rankingParser;

    @Inject
    PlayerServiceProvider playerService;

    @Inject
    DashboardService dashboardService;

    void onStartup(@Observes StartupEvent ev) {
        log.infof("Starting %s...", APP_NAME);
        String applicationHomeDir = getApplicationHomeDir();
        if (directoryExists(applicationHomeDir)) {
            log.infof("Application home directory exists: %s", applicationHomeDir);
            ensureDirectoriesExist(applicationHomeDir);
            dashboardService.updateCurrentRankingStatus();
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
}
