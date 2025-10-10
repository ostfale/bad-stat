package de.ostfale.qk.app.async;

import de.ostfale.qk.parser.FileParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@ApplicationScoped
public class AsyncDataLoaderService {

    /**
     * Read file from filesystem (external to JAR)
     */
    public CompletableFuture<String> readFileAsync(Path path) {
        Log.debugf("Starting async file read from filesystem: %s", path);

        return CompletableFuture.supplyAsync(() -> {
            try {
                Log.debugf("Reading file on virtual thread: %s", Thread.currentThread());
                return Files.readString(path);
            } catch (Exception e) {
                Log.errorf(e, "Failed to read file: %s", path);
                throw new RuntimeException("Failed to read file: " + path, e);
            }
        }, Executors.newVirtualThreadPerTaskExecutor());
    }

    /**
     * Read resource from classpath (inside JAR)
     */
    public CompletableFuture<String> readResourceAsync(String resourcePath) {
        Log.debugf("Starting async resource read from classpath: %s", resourcePath);

        return CompletableFuture.supplyAsync(() -> {
            try {
                Log.debugf("Reading resource on virtual thread: %s", Thread.currentThread());
                return readResourceAsString(resourcePath);
            } catch (Exception e) {
                Log.errorf(e, "Failed to read resource: %s", resourcePath);
                throw new RuntimeException("Failed to read resource: " + resourcePath, e);
            }
        }, Executors.newVirtualThreadPerTaskExecutor());
    }

    /**
     * Reads file and updates JavaFX UI on FX Application Thread
     */
    public void readFileAndUpdateUI(Path path,
                                    Consumer<String> onSuccess,
                                    Consumer<Throwable> onError) {
        readFileAsync(path)
                .thenAccept(content -> Platform.runLater(() -> onSuccess.accept(content)))
                .exceptionally(error -> {
                    Platform.runLater(() -> onError.accept(error));
                    return null;
                });
    }

    /**
     * Reads resource and updates JavaFX UI on FX Application Thread
     */
    public void readResourceAndUpdateUI(String resourcePath,
                                        Consumer<String> onSuccess,
                                        Consumer<Throwable> onError) {
        readResourceAsync(resourcePath)
                .thenAccept(content -> Platform.runLater(() -> onSuccess.accept(content)))
                .exceptionally(error -> {
                    Platform.runLater(() -> onError.accept(error));
                    return null;
                });
    }

    /**
     * Reads and parses file asynchronously
     */
    public <T> CompletableFuture<T> readAndParseAsync(Path path, FileParser<T> parser) {
        return readFileAsync(path).thenApply(parser::parse);
    }

    /**
     * Reads and parses resource asynchronously
     */
    public <T> CompletableFuture<T> readResourceAndParseAsync(String resourcePath, FileParser<T> parser) {
        return readResourceAsync(resourcePath).thenApply(parser::parse);
    }

    /**
     * Helper method to read resource as string
     */
    private String readResourceAsString(String resourcePath) throws IOException {
        // Remove leading slash if present
        String path = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;

        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path)) {

            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }

            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
