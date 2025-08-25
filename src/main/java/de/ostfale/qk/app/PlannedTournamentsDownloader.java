package de.ostfale.qk.app;

import de.ostfale.qk.web.api.WebFacade;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@ApplicationScoped
public class PlannedTournamentsDownloader implements WebFacade, FileSystemFacade, TimeHandlerFacade {

    private static final String FILE_NAME = "Tournament";
    private static final String FILE_SUFFIX = ".csv";
    private static final String DATE_SEPARATOR = "_";

    private final HttpClient httpClient;
    private String lastDownloadDate;

    @Override
    public String prepareDownloadUrl(String year) {
        return String.format("%s%s%s", TOURNAMENT_DOWNLOAD_URL_PREFIX, year, TOURNAMENT_DOWNLOAD_SEARCH_PARAM);
    }

    @Override
    public String prepareDownloadFileName(String year) {
        String tFileName = FILE_NAME + DATE_SEPARATOR + year + DATE_SEPARATOR + LocalDate.now() + FILE_SUFFIX;
        Log.debugf("PlannedTournamentsDownloader :: Prepare download file name %s", tFileName);
        return tFileName;
    }

    @Override
    public String prepareDownloadTargetPath(String appDirName) {
        var appDir = getApplicationHomeDir();
        var tourDir = appDir + SEP + appDirName + SEP;
        Log.debugf("PlannedTournamentsDownloader :: Prepare download target path %s", tourDir);
        return tourDir;
    }

    @Override
    public String getLastDownloadDate(String targetPath) {
        if (lastDownloadDate != null) {
            return lastDownloadDate;
        }

        Log.debug("PlannedTournamentsDownloader :: Get last download date from file system");
        return extractLastDownloadDateFromFileSystem(targetPath);
    }

    @Override
    public List<File> getDownloadFiles(String targetPath) {
        Log.debugf("PlannedTournamentsDownloader :: Get downloaded files from directory: %s", targetPath);
        var fileList = readAllFiles(targetPath);
        if (fileList.isEmpty()) {
            return List.of();
        }
        return fileList;
    }

    public PlannedTournamentsDownloader() {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    public PlannedTournamentsDownloader(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setLastDownloadDate(String lastDownloadDate) {
        this.lastDownloadDate = lastDownloadDate;
    }

    @Override
    public CompletableFuture<Path> download(String url, Path target) {
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        return httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofInputStream())
                .thenCompose(resp -> {
                    if (resp.statusCode() != 200) {
                        Log.errorf("Download failed with status code: %d for URL: %s", resp.statusCode(), url);
                        return CompletableFuture.failedFuture(new IllegalStateException("HTTP " + resp.statusCode()));
                    }
                    return CompletableFuture.supplyAsync(() -> {
                        try (var in = resp.body()) {
                            Files.copy(in, target, REPLACE_EXISTING);
                            Log.infof("Successfully downloaded file  to %s", target);
                            return target;
                        } catch (Exception e) {
                            Log.errorf("Could not download file from %s to %s", url, target, e);
                            throw new RuntimeException(e);
                        }
                    });
                });
    }

    public CompletableFuture<Void> downloadTwoFiles(String url1, String url2, Path targetPath1, Path targetPath2) {
        var download1 = download(url1, targetPath1);
        var download2 = download(url2, targetPath2);

        return CompletableFuture.allOf(download1, download2)
                .exceptionally(throwable -> {
                    Log.errorf("Error downloading files in parallel: %s", throwable.getMessage());
                    throw new RuntimeException("Failed to download files", throwable);
                });
    }

    private String extractLastDownloadDateFromFileSystem(String targetPath) {
        var fileList = readAllFiles(targetPath);
        if (fileList.isEmpty()) {
            return EMPTY_STRING;
        }

        String lastFileName = fileList.getFirst().getName();
        lastDownloadDate = extractDateFromFileName(lastFileName);
        Log.debugf("PlannedTournamentsDownloader :: Last download date from file system: %s", lastDownloadDate);
        return lastDownloadDate;
    }

    private String extractDateFromFileName(String fileName) {
        int startIndex = fileName.lastIndexOf(DATE_SEPARATOR) + 1;
        int endIndex = fileName.indexOf(FILE_SUFFIX, startIndex);
        var extractedDateFileFormat = fileName.substring(startIndex, endIndex);
        return formatDateToTournamentFormat(extractedDateFileFormat);
    }
}
