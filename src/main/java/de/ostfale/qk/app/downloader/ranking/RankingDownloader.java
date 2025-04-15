package de.ostfale.qk.app.downloader.ranking;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jboss.logging.Logger;

import de.ostfale.qk.app.FileSystemFacade;
import de.ostfale.qk.app.TimeHandlerFacade;
import de.ostfale.qk.db.app.BadStatConfigService;
import de.ostfale.qk.web.internal.RankingWebService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

@Singleton
public class RankingDownloader implements FileSystemFacade, TimeHandlerFacade {

    private static final Logger log = Logger.getLogger(RankingDownloader.class);

    @Inject
    RankingWebService rankingWebService;

    @Inject
    BadStatConfigService badStatConfigService;

    private static final String FILENAME_PREFIX_DELIMITER = "_";
    private static final String FILENAME_EXTENSION_DELIMITER = ".";
    private static final String FILE_NAME = "Ranking_";
    private static final String FILE_SUFFIX = ".xlsx";
    private static final String FILE_NAME_CALENDAR_WEEK = "KW";

    private static final String RANKING_URL = "https://turniere.badminton.de/uploads/ranking/";

    public boolean downloadRankingFileDialog() {
        String currentCalendarWeek = String.valueOf(getActualCalendarWeek());
        String onlineCalendarWeek = rankingWebService.getCalendarWeekForLastUpdate();
        String localCalendarWeek = handleRankingFiles(getRankingFiles());

        String cwOverview = "Aktuelle KW : \t" + currentCalendarWeek + "\nGeladene KW: \t" + localCalendarWeek + "\nOnline KW: \t" + onlineCalendarWeek;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Aktualisierung der Rangliste");
        alert.setHeaderText("Soll die Rangliste heruntergeladen werden?");
        alert.setContentText(cwOverview);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
            updateRankingFileFromWeb();
            return true;
        }
        log.debug("RankingDownloader :: no update selected!");
        return false;
    }

    private void updateRankingFileFromWeb() {
        if (deleteAllFiles(getApplicationRankingDir())) {
            log.debug("RankingDownloader :: existing files deleted!");
            downloadMostCurrentRankingFile();
        }
    }

    private void downloadMostCurrentRankingFile() {
        int currCW = getActualCalendarWeek();
        int currCWYear = getActualCalendarYear();
        int lastCW = getLastCalendarWeek();
        int lastCWYear = getCalendarYearFromLastWeek();

        int onlineCW = Integer.parseInt(rankingWebService.getCalendarWeekForLastUpdate());

        if (currCWYear == lastCWYear) {
            if (currCW == onlineCW || currCW < onlineCW) {
                log.infof("RankingDownloader :: Load ranking file for last available online CW: %d", onlineCW);
                downloadRankingFile(onlineCW, currCWYear);

            } else if (currCW > onlineCW) {
                log.infof("RankingDownloader :: Load ranking file for last CW: %d", lastCW);
                downloadRankingFile(lastCW, currCWYear);
            }
        }
    }

    private void downloadRankingFile(int calendarWeek, int calendarYear) {
        String targetURL = createURLForCalendarWeek(calendarWeek, calendarYear);
        String targetFilePath = getApplicationRankingDir() + SEP + RankingFileModel.prepareFileNameForThisCalendarWeek(calendarWeek, calendarYear);
        if (downloadFile(targetURL, targetFilePath)) {
            log.infof("RankingDownloader :: Download of ranking file for KW %d was successful!", calendarWeek);
            var badStatConfig=badStatConfigService.readConfiguration();
            badStatConfig.setLastRankingFileDownload(LocalDateTime.now());
            badStatConfig.setRankingFileName(getRankingFiles().getFirst().getName());
            badStatConfigService.saveConfiguration(badStatConfig);
        } else {
            log.errorf("RankingDownloader :: Download of ranking file for KW %d failed!", calendarWeek);
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

    public List<File> getRankingFiles() {
        var rankingDir = getApplicationRankingDir();
        log.debugf("RankingFile Download :: ranking dir: %s", rankingDir);
        return readAllFiles(rankingDir);
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

    private String createURLForCalendarWeek(int calendarWeek, int calendarYear) {
        String targetURL = RANKING_URL + FILE_NAME + calendarYear + FILENAME_PREFIX_DELIMITER + FILE_NAME_CALENDAR_WEEK + calendarWeek + FILE_SUFFIX;
        log.debugf("RankingFile Download :: Created URL: %s", targetURL);
        return targetURL;
    }
}
