package de.ostfale.qk.app.downloader.ranking;

import de.ostfale.qk.db.app.BadStatConfigService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Singleton
public class RankingDownloader implements RankingFacade {

    private static final Logger log = Logger.getLogger(RankingDownloader.class);

    @Inject
    BadStatConfigService badStatConfigService;

    public boolean downloadLastCWRankingFile() {
        log.info("RankingDownloader :: download last CW ranking file");
        int lastCW = getLastCalendarWeek();
        int lastYear = getCalendarYearFromLastWeek();
        String targetFilePath = getApplicationRankingDir() + SEP + prepareRankingFileName(lastCW, lastYear);
        String targetUrl = createURLForCalendarWeek(lastCW, lastYear);
        return downloadRankingFile(targetFilePath, targetUrl);
    }

    public boolean downloadCurrentCWRankingFile() {
        log.info("RankingDownloader :: download current CW ranking file");
        int currentCW = getActualCalendarWeek();
        int currentYear = getActualCalendarYear();
        String targetFilePath = getApplicationRankingDir() + SEP + prepareRankingFileName(currentCW, currentYear);
        return downloadRankingFile(targetFilePath, CURRENT_RANKING_FILE_URL);
    }

    private boolean downloadRankingFile(String targetFilePath, String targetUrl) {
        log.info("RankingDownloader :: download ranking file");
        if (!deleteAllFiles(getApplicationRankingDir())) {
            log.error("RankingDownloader :: could not delete existing ranking files");
            return false;
        }
        try {
            URL url = getURL(targetUrl);
            downloadIntoFile(url, targetFilePath);
            updateConfigurationInfo();
        } catch (IOException e) {
            log.errorf("RankingDownloader :: Malformed URL: %s", targetUrl);
            resetConfigurationInfo();
            return false;
        }
        return true;
    }

    private void resetConfigurationInfo() {
        log.info("RankingDownloader :: reset configuration info");
        var badStatConfig = badStatConfigService.readConfiguration();
        badStatConfig.setLastRankingFileDownload(null);
        badStatConfig.setRankingFileName(null);
        badStatConfigService.saveConfig(badStatConfig);
    }

    private void updateConfigurationInfo() {
        log.info("RankingDownloader :: update configuration info");
        var badStatConfig = badStatConfigService.readConfiguration();
        badStatConfig.setLastRankingFileDownload(LocalDateTime.now());
        badStatConfig.setRankingFileName(getRankingFiles().getFirst().getName());
        badStatConfigService.saveConfig(badStatConfig);
    }

    public String getCalendarWeekFromRankingFile(File aFile) {
        Objects.requireNonNull(aFile, "Ranking file must not be null");
        var fileName = aFile.getName();
        log.debugf("RankingFile Download :: get calendar week for file name: %s", fileName);
        return getCalenderWeekFromRankingFileName(fileName);
    }

    public List<File> getRankingFiles() {
        var rankingDir = getApplicationRankingDir();
        log.debugf("RankingFile Download :: ranking dir: %s", rankingDir);
        return readAllFiles(rankingDir);
    }
}
