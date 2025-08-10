package de.ostfale.qk.app;

import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class PlannedTournamentsDownloader {

    // Returns a CompletableFuture; in Quarkus you can use @RunOnVirtualThread if exposing as REST
    public CompletableFuture<Path> download(String url, Path target) {
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        return client.sendAsync(req, HttpResponse.BodyHandlers.ofInputStream())
                .thenCompose(resp -> {
                    if (resp.statusCode() != 200) {
                        return CompletableFuture.failedFuture(new IllegalStateException("HTTP " + resp.statusCode()));
                    }
                    return CompletableFuture.supplyAsync(() -> {
                        try (var in = resp.body()) {
                            Files.createDirectories(target.getParent());
                            Files.copy(in, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                            return target;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
    }

    private final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(30))
            .build();

}
