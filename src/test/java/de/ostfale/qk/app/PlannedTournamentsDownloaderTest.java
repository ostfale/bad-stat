package de.ostfale.qk.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DisplayName("Test planned tournaments downloader")
@Tag("unittest")
class PlannedTournamentsDownloaderTest {

    private PlannedTournamentsDownloader sut;

    @BeforeEach
    void setUp() {
        sut = new PlannedTournamentsDownloader();
    }

    @Test
    void shouldFailDownloadForInvalidUrl() {
        // given
        HttpClient mockClient = mock(HttpClient.class);
        var mockResponse = mock(HttpResponse.class);

        when(mockResponse.statusCode()).thenReturn(404);
        when(mockClient.sendAsync(any(), any())).thenReturn(completedFuture(mockResponse));

        sut = new PlannedTournamentsDownloader(mockClient);

        // when and then
        CompletableFuture<Path> result = sut.download("http://invalid-url.com/mock-file.txt", Path.of("mockTarget"));

        assertThrows(Exception.class, result::get);
    }

    @Test
    @DisplayName("Should correctly prepare download URL")
    void shouldCorrectlyPrepareDownloadUrl() {
        // given
        var year = "2025";
        String expectedUrl = "https://turniere.badminton.de/download?name=&year=2025&remaining=all&colortype=&"; // Adjust according to method logic

        // when
        String result = sut.prepareDownloadUrl(year);

        // Assert
        assertThat(result).startsWith(expectedUrl);
    }

    @Test
    @DisplayName("Should prepare correct download file name")
    void shouldPrepareCorrectDownloadFileName() {
        // given
        var year = "2025";
        var expectedFileNamePattern = "Tournament_2025_.*\\.csv";

        // when
        String result = sut.prepareDownloadFileName(year);

        // then
        assertTrue(result.matches(expectedFileNamePattern), "The file name should match the expected pattern: " + expectedFileNamePattern);
    }

    @Test
    @DisplayName("Should include current date in download file name")
    void shouldIncludeCurrentDateInFileName() {
        // given
        var year = "2025";
        var currentDate = LocalDate.now().toString();

        // when
        String result = sut.prepareDownloadFileName(year);

        // then
        assertTrue(result.contains(currentDate), "The file name should include the current date: " + currentDate);
    }

    @Test
    @DisplayName("Should prepare a valid target path")
    void shouldPrepareValidTargetPath() {
        // given
        String appDirName = "testAppDir";
        var expectedFilPath = "\\.bad_stat\\testAppDir\\";

        // when
        String result = sut.prepareDownloadTargetPath(appDirName);

        // then
        assertTrue(result.endsWith(expectedFilPath), "The target path should end with the app directory name: " + appDirName);
    }

    @Test
    @DisplayName("Should include application directory in target path")
    void shouldIncludeApplicationDirectoryInTargetPath() {
        // given
        String appDirName = "testAppDir";

        // when
        String result = sut.prepareDownloadTargetPath(appDirName);

        // then
        assertTrue(result.contains(sut.getApplicationHomeDir()), "The target path should include the application home directory.");
    }
}
