package de.ostfale.qk.app.io;

import io.quarkus.logging.Log;
import jakarta.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Default implementation of FileHandler for basic file I/O operations.
 */
@Singleton
public class DefaultFileHandler implements FileHandler {

    @Override
    public Optional<String> read(Path path) {
        try {
            if (!Files.exists(path)) {
                Log.warnf("DefaultFileHandler :: File not found: %s", path);
                return Optional.empty();
            }
            String content = Files.readString(path);
            return Optional.of(content);
        } catch (IOException e) {
            Log.errorf( "DefaultFileHandler :: Failed to read file: %s -> error: %s", path,e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> read(String path) {
        return read(Paths.get(path));
    }

    @Override
    public Optional<File> readFile(Path path) {
        if (!Files.exists(path)) {
            Log.warnf("DefaultFileHandler :: File not found: %s", path);
            return Optional.empty();
        }
        return Optional.of(path.toFile());
    }

    @Override
    public List<File> readAllFiles(Path directory) {
        if (!Files.exists(directory)) {
            Log.warnf("DefaultFileHandler :: Directory not found: %s", directory);
            return List.of();
        }

        try (Stream<Path> fileStream = Files.list(directory)) {
            return fileStream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .toList();
        } catch (IOException e) {
            Log.errorf("DefaultFileHandler :: Failed to read directory: %s -> error: %s", directory, e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean write(Path path, String content) {
        try {
            // Create parent directories if they don't exist
            if (path.getParent() != null) {
               Files.createDirectories(path.getParent());
            }
            Files.writeString(path, content);
            return true;
        } catch (IOException e) {
            Log.errorf( "DefaultFileHandler :: Failed to write file: %s -> error: %s", path,e.getMessage());
            return false;
        }
    }

    @Override
    public boolean write(String path, String content) {
        return write(Paths.get(path), content);
    }

    @Override
    public boolean exists(Path path) {
        return Files.exists(path);
    }

    @Override
    public boolean exists(String path) {
        return exists(Paths.get(path));
    }

    @Override
    public boolean deleteFile(Path path) {
        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            Log.errorf("DefaultFileHandler :: Failed to delete file: %s -> error: %s", path,e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteAllFiles(Path directory) {
        if (!Files.exists(directory)) {
            Log.warnf("DefaultFileHandler :: Directory not found: %s", directory);
            return false;
        }

        try (Stream<Path> paths = Files.walk(directory)) {
            paths.filter(Files::isRegularFile)
                    .forEach(this::deleteFileSafely);
            return true;
        } catch (IOException e) {
            Log.errorf("DefaultFileHandler :: Failed to delete files in directory: %s -> error: %s", directory, e.getMessage());
            return false;
        }
    }

    private void deleteFileSafely(Path filePath) {
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            Log.errorf("DefaultFileHandler :: Failed to delete file: %s -> error: %s", filePath, e.getMessage());
        }
    }
}
