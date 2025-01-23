package de.ostfale.qk.app;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class ApplicationSetup {

    private static final Logger log = LoggerFactory.getLogger(ApplicationSetup.class);

    private static final String APP_DIR_NAME = ".bad_stat";

    public Path createApplicationDirectories(String appRootDir) {
        Path path = Paths.get(appRootDir);
        try {
            if (!Files.isDirectory(path.resolve(APP_DIR_NAME))) {
                log.info("Initialize application directories...");
                Files.createDirectory(path.resolve(APP_DIR_NAME));

                // Create directories for all DirTypes dynamically and log results
                Map<DirTypes, Path> paths = Arrays.stream(DirTypes.values())
                        .collect(Collectors.toMap(
                                dirType -> dirType,
                                dirType -> createSubDirectory(dirType, appRootDir)
                        ));
            }
        } catch (IOException e) {
            log.error("Error creating application directories: {}", e.getMessage());
        }
        return path.resolve(APP_DIR_NAME);
    }

    private Path createSubDirectory(DirTypes dirType, String rootAppDir) {
        try {
            Path subDirPath = Paths.get(rootAppDir).resolve(APP_DIR_NAME).resolve(dirType.displayName);
            var createdDir =  Files.createDirectory(subDirPath);
            log.info("Created: {}", createdDir.toFile().getPath());
            return createdDir;
        } catch (IOException e) {
            log.error("Failed to create directory for {}: {}", dirType.displayName, e.getMessage());
            throw new RuntimeException(e); // Propagate error as runtime exception if required
        }
    }
}
