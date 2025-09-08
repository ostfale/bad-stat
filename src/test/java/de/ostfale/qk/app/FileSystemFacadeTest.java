package de.ostfale.qk.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.wildfly.common.Assert.assertFalse;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test FileSystemFacade default functions")
@Tag("unittest")
class FileSystemFacadeTest {

    private FileSystemFacade sut;

    @BeforeEach
    void setUp() {
        sut = new FileSystemFacade() {
        };
    }

    @TempDir
    File testTempDirectory;


    @Test
    @DisplayName("Should verify the existence of a valid directory path")
    void testExistenceOfDirectory() {
        // given
        String existingPath = testTempDirectory.getAbsolutePath();
        String nonExistingPath = existingPath + "nonExistingDir";

        // when
        var success = sut.directoryExists(existingPath);
        var failure = sut.directoryExists(nonExistingPath);

        // then
        assertTrue(success);
        assertFalse(failure);
    }

    @Test
    @DisplayName("Should return the correct user home directory")
    void testGetHomeDir() {
        // given
        String expectedHomeDir = System.getProperty("user.home");

        // when
        String homeDir = sut.getHomeDir();

        // then
        assertEquals(expectedHomeDir, homeDir);
    }


    @Test
    @DisplayName("Should return the application home directory")
    void testApplicationHomeDirectory() {
        // given
        String expectedHomeDir = System.getProperty("user.home");
        String expectedAppDir = expectedHomeDir + File.separator + ".bad_stat";

        // when
        String appDir = sut.getApplicationHomeDir();

        // then
        assertEquals(expectedAppDir, appDir);
    }


    @Test
    @DisplayName("Should list all files in a directory")
    void testReadAllFiles() throws IOException {
        // given
        File tempFile1 = new File(testTempDirectory, "file1.txt");
        File tempFile2 = new File(testTempDirectory, "file2.txt");
        var fileOneCreated = tempFile1.createNewFile();
        var fileTwoCreated = tempFile2.createNewFile();

        // when
        List<File> files = sut.readAllFiles(testTempDirectory.getAbsolutePath());

        // then
        assertAll("Grouped assertions for file reading test",
                () -> assertThat(fileOneCreated).isTrue(),
                () -> assertThat(fileTwoCreated).isTrue(),
                () -> assertThat(files.size()).isEqualTo(2),
                () -> assertThat(files.containsAll(List.of(tempFile1, tempFile2))).isTrue()
        );
    }

    @Test
    @DisplayName("Should return empty list for an empty directory")
    void testReadAllFilesEmptyDirectory() {
        // when
        List<File> files = sut.readAllFiles(testTempDirectory.getAbsolutePath());

        // then
        assertTrue(files.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list for a non-existent directory")
    void testReadAllFilesNonExistentDirectory() {
        // given
        String nonExistentDir = "nonExistentTestDir";

        // when
        List<File> files = sut.readAllFiles(nonExistentDir);

        // then
        assertTrue(files.isEmpty());
    }

    @ParameterizedTest(name = "Test extraction of a date {0} from file name {1}")
    @CsvSource({
            "03.09.2025,Tournament_2025_2025-09-03.csv",
            "03.09.2025,Tournament_2026_2025-09-03.csv",
    })
    @DisplayName("Read download date from file name")
    void testDateExtractionFromFileName(String expectedDate, String fileName) {
        // when
        String result = sut.extractDateFromFileName(fileName);

        // then
        assertThat(result).isEqualTo(expectedDate);
    }
}
