package de.ostfale.qk.app;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@ApplicationScoped
public class ApplicationSetup {

    private static final Logger log = Logger.getLogger(ApplicationSetup.class);

    private static final String APP_DIR_NAME = ".bad_stat";

    public Path createApplicationDirectories(String appRootDir) {
        Path path = Paths.get(appRootDir);
        try {
            if (!Files.isDirectory(path.resolve(APP_DIR_NAME))) {
                log.info("Initialize application directories...");
                Files.createDirectory(path.resolve(APP_DIR_NAME));
            }
            Path subDirPath = Paths.get(appRootDir).resolve(APP_DIR_NAME);

            Arrays.stream(DirTypes.values()).forEach(dirType -> {
                createDirectoryIfNotExists(subDirPath.toAbsolutePath().toString(), dirType.displayName);
            });

        } catch (IOException e) {
            log.errorf("Error creating application directories: {}", e.getMessage());
        }
        return path.resolve(APP_DIR_NAME);
    }

    private void createDirectoryIfNotExists(String baseDir, String subDirName) {
        Path subDirPath = Paths.get(baseDir, subDirName);
        if (!Files.isDirectory(subDirPath)) {
            try {
                Files.createDirectory(subDirPath);
                log.debugf("Successfully created subdirectory: %s", subDirPath);
            } catch (IOException e) {
                log.errorf("Failed to create subdirectory '%s': %s", subDirName, e.getMessage());
                throw new RuntimeException("Failed to create subdirectory: " + subDirName, e);
            }
        } else {
            log.debugf("Subdirectory already exists: %s", subDirPath);
        }
    }
}
