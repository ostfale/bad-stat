package de.ostfale.qk.app;

import de.ostfale.qk.app.config.DirectoryManagerService;
import de.ostfale.qk.ui.dashboard.DashboardService;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ApplicationInitializer {

    private static final String APP_NAME = "BadStat";

    @Inject
    DashboardService dashboardService;

    @Inject
    DirectoryManagerService directoryManager;

    void onStartup(@Observes StartupEvent ev) {
        Log.infof("Starting %s...", APP_NAME);
        initializeApplication();
    }

    private void initializeApplication() {
        try {
            // Validate and create directory structure
            List<String> errors = directoryManager.validateAndCreateDirectoryStructure();

            if (!errors.isEmpty()) {
                Log.errorf("Directory validation failed with {} errors:", errors.size());
                errors.forEach(Log::error);

                // Decide whether to continue or fail fast
                boolean hasCriticalErrors = errors.stream()
                        .anyMatch(error -> error.contains("Required directory"));

                if (hasCriticalErrors) {
                    throw new RuntimeException("Critical directory structure validation failed");
                }
            } else {
                Log.info("Directory structure validation completed successfully");
            }

            dashboardService.updateCurrentRankingStatus();

        } catch (Exception e) {
            Log.error("Application initialization failed", e);
            throw new RuntimeException("Application startup failed", e);
        }
    }
}
