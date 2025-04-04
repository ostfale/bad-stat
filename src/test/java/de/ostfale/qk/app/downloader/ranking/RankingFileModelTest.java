package de.ostfale.qk.app.downloader.ranking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test the ranking file model")
@Tag("unittest")
class RankingFileModelTest {

    private static final String FILE_TEST_NAME = "Ranking_2025_06.xlsx";

    private RankingFileModel rankingFileModel;

    @BeforeEach
    void setUp() {
        File testFile = new File(FILE_TEST_NAME);
        rankingFileModel = new RankingFileModel(testFile);
    }

    @Test
    void testGetCalendarWeek_ValidCalendarWeek() {
        assertEquals("06", rankingFileModel.getCalendarWeek(), "Expected calendar week is 06");
    }

    @Test
    void testGetCalendarWeek_ValidCalendarYear() {
        assertEquals("2025", rankingFileModel.getCalendarYear(), "Expected calendar year is 2025");
    }

    @Test
    void testGetCalendarWeek_ValidFileName() {
        assertEquals("Ranking_2025_06.xlsx", rankingFileModel.getRankingFileName());
    }

    @Test
    void testGetCalendarWeek_ValidFileSuffix() {

        assertEquals(".xlsx", rankingFileModel.getSuffix());
    }

    @Test
    void testPrepareFileNameForThisCalendarWeek() {
        // Test file name generation for a specific calendar week and year
        String generatedFileName = RankingFileModel.prepareFileNameForThisCalendarWeek(15, 2023);
        assertEquals("Ranking_2023_15.xlsx", generatedFileName, "Expected file name is Ranking_2023_15.xlsx");

        generatedFileName = RankingFileModel.prepareFileNameForThisCalendarWeek(1, 2024);
        assertEquals("Ranking_2024_1.xlsx", generatedFileName, "Expected file name is Ranking_2024_1.xlsx");
    }
}
