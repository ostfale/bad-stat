package de.ostfale.qk.app.downloader.ranking;

import de.ostfale.qk.app.DirTypes;
import de.ostfale.qk.app.FileSystemFacade;
import de.ostfale.qk.app.TimeHandlerFacade;
import de.ostfale.qk.web.internal.RankingWebService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.io.File;
import java.util.List;
import java.util.Objects;

@Singleton
public class RankingDownloader implements FileSystemFacade, TimeHandlerFacade {

    private static final Logger log = Logger.getLogger(RankingDownloader.class);

    @Inject
    RankingWebService rankingWebService;

    private static final String FILENAME_PREFIX_DELIMITER = "_";
    private static final String FILENAME_EXTENSION_DELIMITER = ".";
    private static final String FILE_NAME = "Ranking_";
    private static final String FILE_SUFFIX = ".xlsx";
    private static final String FILE_NAME_CALENDAR_WEEK = "KW";

    private static final String RANKING_URL = "https://turniere.badminton.de/uploads/ranking/";

    public void downloadRankingFile() {
        int currentCalendarWeek = getActualCalendarWeek();
        String onlineCalendarWeek = rankingWebService.getCalendarWeekForLastUpdate();
        String localCalendarWeek = handleRankingFiles(getRankingFiles());

        log.debugf("""
                RankingDownloader ::
                    current calendar week: %s,
                    online calendar week: %s,
                    local calendar week %s""", currentCalendarWeek, onlineCalendarWeek, localCalendarWeek);

        // if online calendar week is up to date -> check if download already happened, otherwise download ranking file for this week
        if (currentCalendarWeek == Integer.parseInt(onlineCalendarWeek)) {
            log.debug("RankingDownloader :: Current calendar week is equal to online calendar week ");
            ensureRankingFileForThisCalendarWeekIsDownloaded(currentCalendarWeek, localCalendarWeek);
            return;
        }

        if (currentCalendarWeek > Integer.parseInt(onlineCalendarWeek)) {
            log.debug("RankingDownloader :: Current calender week is greater than online calendar week");

            if (localCalendarWeek.isEmpty()) {
                ensureRankingFileForLastCalendarWeekIsDownloaded();
                return;
            }
            
            log.debugf("RankingDownloader :: Local calendar week is not empty: %s", localCalendarWeek);
            if (Integer.parseInt(localCalendarWeek) < Integer.parseInt(onlineCalendarWeek)) {
                deleteAllFiles(getApplicationRankingDir());
                ensureRankingFileForGivenCalendarWeekAndYearIsDownloaded(Integer.parseInt(onlineCalendarWeek),getActualCalendarYear());
            }
        }
    }

    private void ensureRankingFileForGivenCalendarWeekAndYearIsDownloaded(int calendarWeek, int calendarYear) {
        String targetURL = createURLForCalendarWeek(calendarWeek, calendarYear);
        String targetFilePath = getApplicationRankingDir() + SEP + RankingFileModel.prepareFileNameForThisCalendarWeek(calendarWeek, calendarYear);
        if (downloadFile(targetURL, targetFilePath)) {
            log.infof("RankingDownloader :: Download of ranking file for KW %d was successful!", calendarWeek);
        } else {
            log.errorf("RankingDownloader :: Download of ranking file for KW %d failed!", calendarWeek);
        }
    }
    
    private void ensureRankingFileForLastCalendarWeekIsDownloaded() {
        var lastCalendarWeek = getLastCalendarWeek();
        var yearFromLastCalendarWeek = getCalendarYearFromLastWeek();
        String targetURL = createURLForCalendarWeek(lastCalendarWeek, yearFromLastCalendarWeek);
        String targetFilePath = getApplicationRankingDir() + SEP + RankingFileModel.prepareFileNameForThisCalendarWeek(lastCalendarWeek, yearFromLastCalendarWeek);
        if (downloadFile(targetURL, targetFilePath)) {
            log.infof("RankingDownloader :: Download of ranking file for KW %d was successful!", lastCalendarWeek);
        } else {
            log.errorf("RankingDownloader :: Download of ranking file for KW %d failed!", lastCalendarWeek);
        }
    }

    private void ensureRankingFileForThisCalendarWeekIsDownloaded(Integer currentCalendarWeek, String localCalendarWeek) {
        if (!localCalendarWeek.isEmpty() && currentCalendarWeek == Integer.parseInt(localCalendarWeek)) {
            log.debug("RankingDownloader :: Ranking file for this calendar has already been downloaded");
            return;
        }

        var deletionSuccess = deleteAllFiles(getApplicationHomeDir() + SEP + DirTypes.RANKING.displayName);
        if (!deletionSuccess) {
            log.error("RankingDownloader :: Failed to delete ranking files");
        }
    }

    public String getCalendarWeekFromRankingFile(File aFile) {
        Objects.requireNonNull(aFile, "Ranking file must not be null");
        var fileName = aFile.getName();
        log.debugf("Ranking File :: file name: %s", fileName);
        var startOfCalendarWeek = fileName.lastIndexOf(FILENAME_PREFIX_DELIMITER) + 1;
        var endOfCalendarWeek = fileName.lastIndexOf(FILENAME_EXTENSION_DELIMITER);

        return fileName.substring(startOfCalendarWeek, endOfCalendarWeek);
    }

    private String handleRankingFiles(List<File> existingRankingFiles) {
        if (existingRankingFiles.isEmpty()) {
            return "";
        } else if (existingRankingFiles.size() == 1) {
            return getCalendarWeekFromRankingFile(existingRankingFiles.getFirst());
        } else {
            log.errorf("More than one ranking file found -> %d", existingRankingFiles.size());
            return "";
        }
    }

    private List<File> getRankingFiles() {
        var rankingDir = getApplicationRankingDir();
        log.debugf("RankingFile Download :: ranking dir: %s", rankingDir);
        return readAllFiles(rankingDir);
    }

    private String createURLForCalendarWeek(int calendarWeek, int calendarYear) {
        String targetURL = RANKING_URL + FILE_NAME + calendarYear + FILENAME_PREFIX_DELIMITER + FILE_NAME_CALENDAR_WEEK + calendarWeek + FILE_SUFFIX;
        log.debugf("RankingFile Download :: Created URL: %s", targetURL);
        return targetURL;
    }

    private String createTargetFilePath(int calendarWeek, int calendarYear) {
        String targetFilePath = getApplicationHomeDir() + SEP + DirTypes.RANKING.displayName + SEP + RankingFileModel.prepareFileNameForThisCalendarWeek(calendarWeek, calendarYear);
        log.debugf("RankingFile Download :: Created target file path: %s", targetFilePath);
        return targetFilePath;
    }
}


