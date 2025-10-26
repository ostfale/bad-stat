package de.ostfale.qk.app.config;

import de.ostfale.qk.app.io.FileHandler;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DirectoryManagerService {

    @Inject
    DirectoryConfiguration directoryConfig;

    @Inject
    FileHandler fileHandler;

    /**
     * Validates and creates application directory structure based on configuration
     *
     * @return List of any validation errors
     */
    public List<String> validateAndCreateDirectoryStructure() {
        List<String> errors = new ArrayList<>();
        String basePath = directoryConfig.basePath();
        Log.infof("DirectoryManagerService :: Validating directory structure with base path: %s", basePath);

        Path baseDirectory = Paths.get(basePath);
        if (!validateAndCreateBaseDirectory(baseDirectory, errors)) {
            return errors; // Can't continue without base directory
        }

        processDirectoryStructure(baseDirectory, errors);
        return errors;
    }

    private boolean validateAndCreateBaseDirectory(Path baseDirectory, List<String> errors) {
        if (!Files.exists(baseDirectory)) {
            try {
                Files.createDirectories(baseDirectory);
                Log.infof("DirectoryManagerService :: Created base directory: %s", baseDirectory);
            } catch (Exception e) {
                String error = "DirectoryManagerService :: Failed to create base directory: " + baseDirectory;
                Log.errorf(error, e);
                errors.add(error);
                return false;
            }
        }
        return true;
    }

    private void processDirectoryStructure(Path baseDirectory, List<String> errors) {
        for (DirectoryConfiguration.DirectoryEntry entry : directoryConfig.structure()) {
            Path fullPath = baseDirectory.resolve(entry.path());
            processDirectoryEntry(entry, fullPath, errors);
        }
    }

    private void processDirectoryEntry(DirectoryConfiguration.DirectoryEntry entry, Path fullPath, List<String> errors) {
        if (!Files.exists(fullPath)) {
            handleMissingDirectory(entry, fullPath, errors);
        } else {
            validateExistingPath(fullPath, errors);
        }
    }

    private void handleMissingDirectory(DirectoryConfiguration.DirectoryEntry entry, Path fullPath, List<String> errors) {
        if (entry.createIfMissing()) {
            createDirectory(entry, fullPath, errors);
        } else if (entry.required()) {
            String error = "Required directory does not exist and creation is disabled: " + fullPath;
            Log.error(error);
            errors.add(error);
        }
    }

    private void createDirectory(DirectoryConfiguration.DirectoryEntry entry, Path fullPath, List<String> errors) {
        try {
            Files.createDirectories(fullPath);
            Log.infof("Created directory: %s", fullPath);
        } catch (Exception e) {
            String error = "Failed to create directory: " + fullPath;
            Log.error(error, e);
            errors.add(error);
            if (entry.required()) {
                Log.errorf("Required directory creation failed: %s", fullPath);
            }
        }
    }

    private void validateExistingPath(Path fullPath, List<String> errors) {
        Log.tracef("Directory exists: %s", fullPath);
        if (!Files.isDirectory(fullPath)) {
            String error = "Path exists but is not a directory: " + fullPath;
            Log.error(error);
            errors.add(error);
        }
    }
    /**
     * Gets the full path for a specific directory
     */
    public Path getDirectoryPath(String relativePath) {
        return Paths.get(directoryConfig.basePath()).resolve(relativePath);
    }

    /**
     * Checks if a specific directory exists
     */
    public boolean directoryExists(String relativePath) {
        Path fullPath = getDirectoryPath(relativePath);
        return Files.exists(fullPath) && Files.isDirectory(fullPath);
    }

    /**
     * Gets the base application directory path
     */
    public Path getBasePath() {
        return Paths.get(directoryConfig.basePath());
    }
}
