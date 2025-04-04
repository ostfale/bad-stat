package de.ostfale.qk.app.downloader.ranking;

import org.jboss.logging.Logger;

import java.io.File;
import java.util.Objects;

public class RankingFileModel {

    private static final Logger log = Logger.getLogger(RankingFileModel.class);

    private static final String FILENAME_PREFIX_DELIMITER = "_";
    private static final String FILENAME_EXTENSION_DELIMITER = ".";
    private static final String FILE_NAME = "Ranking";
    private static final String FILE_SUFFIX = ".xlsx";


    private final File rankingFile;
    private final String rankingFileName;

    private final String calendarWeek;
    private final String calendarYear;
    private final String fileSuffix;

    public RankingFileModel(File rankingFile) {
        Objects.requireNonNull(rankingFile, "Ranking file must not be null");

        this.rankingFile = rankingFile;
        this.rankingFileName = rankingFile.getName();
        this.fileSuffix = getSuffix(rankingFileName);
        this.calendarWeek = getCalendarWeek(rankingFileName);
        this.calendarYear = getCalendarYear(rankingFileName);
        log.debugf("Ranking File :: file name: %s", rankingFileName);
    }

    public static String prepareFileNameForThisCalendarWeek(int calendarWeek, int calendarYear) {
        var createdFileName = FILE_NAME + FILENAME_PREFIX_DELIMITER + calendarYear + FILENAME_PREFIX_DELIMITER + calendarWeek + FILE_SUFFIX;
        log.debugf("Created file name: %s", createdFileName);
        return createdFileName;
    }

    public String getCalendarYear() {
        return calendarYear;
    }

    public String getCalendarWeek() {
        return calendarWeek;
    }

    public File getRankingFile() {
        return rankingFile;
    }

    public String getRankingFileName() {
        return rankingFileName;
    }

    public String getSuffix() {
        return fileSuffix;
    }

    public String getCalendarYear(String fileName) {
        var startOfCalendarYear = fileName.indexOf(FILENAME_PREFIX_DELIMITER) + 1;
        var endOfCalendarYear = fileName.lastIndexOf(FILENAME_PREFIX_DELIMITER);
        return fileName.substring(startOfCalendarYear, endOfCalendarYear);
    }

    public String getCalendarWeek(String fileName) {
        var startOfCalendarWeek = fileName.lastIndexOf(FILENAME_PREFIX_DELIMITER) + 1;
        var endOfCalendarWeek = fileName.lastIndexOf(FILENAME_EXTENSION_DELIMITER);
        return fileName.substring(startOfCalendarWeek, endOfCalendarWeek);
    }

    private String getSuffix(String fileName) {
        int lastDelimiterIndex = fileName.lastIndexOf(FILENAME_EXTENSION_DELIMITER) - 1;
        return fileName.substring(lastDelimiterIndex + 1);
    }
}
