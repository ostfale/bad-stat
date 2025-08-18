package de.ostfale.qk.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Test planned tournaments downloader")
@Tag("unittest")
class PlannedTournamentsDownloaderTest {

    private PlannedTournamentsDownloader sut;

    @TempDir
    Path tempDir; // JUnit will create and clean up this directory automatically

    @BeforeEach
    void setUp() {
        sut = new PlannedTournamentsDownloader();
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

    @Test
    @DisplayName("Should read the download date from the file name")
    void shouldReadDownloadDateFromFile() {
        // given
        String appDirName = "testAppDir";
        String lastDownloadDate = "2025-01-01";

        // when
        sut.setLastDownloadDate(lastDownloadDate);
        String result = sut.getLastDownloadDate(appDirName);

        // then
        assertThat(result).isEqualTo(lastDownloadDate);
    }

    @Test
    @DisplayName("Should handle file operations in temp directory")
    void shouldHandleFileOperationsInTempDirectory() throws IOException {
        // given
        String fileName = "Tournament_2026_2025-01-25.csv";
        String testContent = "Tournament,Date,Location test Tournament,2025-01-01,Test City";

        // Create a test file in temp directory
        Path testFile = tempDir.resolve(fileName);
        Files.write(testFile, testContent.getBytes());

        // when
        var result = sut.getLastDownloadDate(tempDir.toString());
        boolean fileExists = Files.exists(testFile);
        String content = Files.readString(testFile);

        // then
        assertAll("Read file data",
                () -> assertThat(fileExists).isTrue(),
                () -> assertThat(content).isEqualTo(testContent),
                () -> assertThat(result).isEqualTo("25.01.2025")
        );
    }

}
