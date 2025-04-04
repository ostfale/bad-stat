package de.ostfale.qk.app.downloader.ranking;

import de.ostfale.qk.app.FileSystemFacade;
import de.ostfale.qk.app.TimeHandlerFacade;
import org.jboss.logging.Logger;

import java.io.File;
import java.util.Objects;

public class RankingDownloader implements FileSystemFacade, TimeHandlerFacade {

    private static final Logger log = Logger.getLogger(RankingDownloader.class);

    private static final String FILENAME_PREFIX_DELIMITER = "_";
    private static final String FILENAME_EXTENSION_DELIMITER = ".";


    public String getCalendarWeekFromRankingFile(File aFile) {
        Objects.requireNonNull(aFile, "Ranking file must not be null");
        var fileName = aFile.getName();
        log.debugf("Ranking File :: file name: %s", fileName);
        var startOfCalendarWeek = fileName.lastIndexOf(FILENAME_PREFIX_DELIMITER) + 1;
        var endOfCalendarWeek = fileName.lastIndexOf(FILENAME_EXTENSION_DELIMITER);

        return fileName.substring(startOfCalendarWeek, endOfCalendarWeek);
    }
}
