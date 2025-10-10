package de.ostfale.qk.app.async;

import de.ostfale.qk.parser.FileParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class SyncDataLoaderService {

    public <T> T readResourceAndParse(String resourcePath, FileParser<T> parser) {
        Log.debugf("SyncDataLoaderService :: Starting sync resource read from classpath: %s", resourcePath);
        try {
            String content = readResourceAsString(resourcePath);
            return parser.parse(content);
        } catch (Exception e) {
            Log.errorf(e, "SyncDataLoaderService :: Failed to read or parse resource: %s", resourcePath);
            throw new RuntimeException("SyncDataLoaderService :: Failed to read or parse resource: " + resourcePath, e);
        }
    }

    private String readResourceAsString(String resourcePath) throws IOException {

        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }

            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
