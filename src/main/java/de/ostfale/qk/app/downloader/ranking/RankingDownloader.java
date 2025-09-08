package de.ostfale.qk.app.downloader.ranking;

import de.ostfale.qk.app.DirTypes;
import de.ostfale.qk.data.dashboard.DashboardRankingDataJsonHandler;
import de.ostfale.qk.data.dashboard.model.DashboardRankingData;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Singleton
public class RankingDownloader implements RankingFacade {

    @Inject
    DashboardRankingDataJsonHandler dashboardRankingDataJsonHandler;

    public boolean downloadLastCWRankingFile() {
        Log.info("RankingDownloader :: download last CW ranking file");
        int lastCW = getLastCalendarWeek();
        int lastYear = getCalendarYearFromLastWeek();
        String targetFilePath = getApplicationSubDir(DirTypes.RANKING.displayName) + SEP + prepareRankingFileName(lastCW, lastYear);
        String targetUrl = createURLForCalendarWeek(lastCW, lastYear);
        return downloadRankingFile(targetFilePath, targetUrl);
    }

    public boolean downloadCurrentCWRankingFile() {
        Log.info("RankingDownloader :: download current CW ranking file");
        int currentCW = getActualCalendarWeek();
        int currentYear = getActualCalendarYear();
        String targetFilePath = getApplicationSubDir(DirTypes.RANKING.displayName) + SEP + prepareRankingFileName(currentCW, currentYear);
        return downloadRankingFile(targetFilePath, CURRENT_RANKING_FILE_URL);
    }

    private boolean downloadRankingFile(String targetFilePath, String targetUrl) {
        Log.info("RankingDownloader :: download ranking file");
        if (!deleteAllFiles(getApplicationSubDir(DirTypes.RANKING.displayName))) {
            Log.error("RankingDownloader :: could not delete existing ranking files");
            return false;
        }
        try {
            URL url = getURL(targetUrl);
            downloadIntoFile(url, targetFilePath);
            updateConfigurationInfo();
        } catch (IOException e) {
            Log.errorf("RankingDownloader :: Malformed URL: %s", targetUrl);
            resetConfigurationInfo();
            return false;
        }
        return true;
    }

    private void resetConfigurationInfo() {
        Log.info("RankingDownloader :: reset configuration info");
        DashboardRankingData dashboardRankingData = dashboardRankingDataJsonHandler.readDashboardRankingData();
        dashboardRankingData.setLastRankingFileDownload(null);
        dashboardRankingData.setRankingFileName(null);
        dashboardRankingDataJsonHandler.saveDashboardRankingData(dashboardRankingData);
    }

    private void updateConfigurationInfo() {
        Log.info("RankingDownloader :: update configuration info");
        DashboardRankingData dashboardRankingData = dashboardRankingDataJsonHandler.readDashboardRankingData();
        dashboardRankingData.setLastRankingFileDownload(LocalDateTime.now());
        dashboardRankingData.setRankingFileName(getRankingFiles().getFirst().getName());
        dashboardRankingDataJsonHandler.saveDashboardRankingData(dashboardRankingData);
    }

    public String getCalendarWeekFromRankingFile(File aFile) {
        Objects.requireNonNull(aFile, "Ranking file must not be null");
        var fileName = aFile.getName();
        Log.debugf("RankingFile Download :: get calendar week for file name: %s", fileName);
        return getCalenderWeekFromRankingFileName(fileName);
    }

    public List<File> getRankingFiles() {
        var rankingDir = getApplicationSubDir(DirTypes.RANKING.displayName);
        Log.debugf("RankingFile Download :: ranking dir: %s", rankingDir);
        return readAllFiles(rankingDir);
    }
}
