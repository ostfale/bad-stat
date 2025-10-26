package de.ostfale.qk.app.io;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Base interface for file I/O operations.
 * Provides core functionality for reading and writing files as raw strings.
 */
public interface FileHandler {

    Optional<String> read(Path path);

    Optional<String> read(String path);

    Optional<File> readFile(Path path);

    List<File> readAllFiles(Path directory);

    boolean write(Path path, String content);

    boolean write(String path, String content);

    boolean exists(Path path);

    boolean exists(String path);

    boolean deleteFile(Path path);

    boolean deleteAllFiles(Path directory);

}
