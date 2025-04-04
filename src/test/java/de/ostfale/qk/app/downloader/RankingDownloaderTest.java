package de.ostfale.qk.app.downloader;

import de.ostfale.qk.app.downloader.ranking.RankingDownloader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test the ranking downloader")
class RankingDownloaderTest {

    private static final String FILE_TEST_NAME ="Ranking_2025_06.xlsx";

    private File testFile;
    private RankingDownloader rankingDownloader;

    @TempDir
    static File testDir;

    @Test
    @DisplayName("Canary test")
    void testTempDirIsValid() {
        assertTrue(testDir.isDirectory());
    }

    @BeforeEach
    void setUp() {
        testFile = new File(FILE_TEST_NAME);
        rankingDownloader = new RankingDownloader();
    }

    @Test
    void testGetCalendarWeekFromRankingFileValidFile() {
        // when
        String result = rankingDownloader.getCalendarWeekFromRankingFile(testFile);

        // Assert
        assertEquals("06", result);
    }

    @Test
    void testGetCalendarWeekFromRankingFileWithMultipleDelimiters() {
        // Arrange
        File file = new File("ranking_data_2023_12.txt");
        RankingDownloader rankingDownloader = new RankingDownloader();

        // Act
        String result = rankingDownloader.getCalendarWeekFromRankingFile(file);

        // Assert
        assertEquals("12", result);
    }

    @Test
    void testGetCalendarWeekFromRankingFileNoPrefixDelimiter() {
        // Arrange
        File file = new File("data42.txt");
        RankingDownloader rankingDownloader = new RankingDownloader();

        // Act
        String result = rankingDownloader.getCalendarWeekFromRankingFile(file);

        // Assert
        assertEquals("42", result);
    }

    @Test
    void testGetCalendarWeekFromRankingFileNoExtensionDelimiter() {
        // Arrange
        File file = new File("ranking_42");
        RankingDownloader rankingDownloader = new RankingDownloader();

        // Act
        String result = rankingDownloader.getCalendarWeekFromRankingFile(file);

        // Assert
        assertEquals("42", result);
    }

    @Test
    void testGetCalendarWeekFromRankingFileInvalidFileName() {
        // Arrange
        File file = new File("ranking42txt"); // Missing delimiters
        RankingDownloader rankingDownloader = new RankingDownloader();

        // Act and Assert
        assertThrows(StringIndexOutOfBoundsException.class, () ->
                rankingDownloader.getCalendarWeekFromRankingFile(file)
        );
    }

    @Test
    void testGetCalendarWeekFromRankingFileNullFile() {
        // Arrange
        RankingDownloader rankingDownloader = new RankingDownloader();

        // Act and Assert
        assertThrows(NullPointerException.class, () ->
                rankingDownloader.getCalendarWeekFromRankingFile(null)
        );
    }
}
