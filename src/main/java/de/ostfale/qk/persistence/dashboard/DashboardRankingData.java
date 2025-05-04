package de.ostfale.qk.persistence.dashboard;

import org.jboss.logging.Logger;

import java.time.LocalDateTime;

public class DashboardRankingData {

    Logger log = Logger.getLogger(DashboardRankingData.class);

    private String rankingFileName;
    private String playerCacheLoadedForCW;
    private LocalDateTime lastRankingFileDownload;

    public String getRankingFileName() {
        return rankingFileName;
    }

    public void setRankingFileName(String rankingFileName) {
        this.rankingFileName = rankingFileName;
    }

    public String getPlayerCacheLoadedForCW() {
        return playerCacheLoadedForCW;
    }

    public void setPlayerCacheLoadedForCW(String playerCacheLoadedForCW) {
        this.playerCacheLoadedForCW = playerCacheLoadedForCW;
    }

    public LocalDateTime getLastRankingFileDownload() {
        return lastRankingFileDownload;
    }

    public void setLastRankingFileDownload(LocalDateTime lastRankingFileDownload) {
        this.lastRankingFileDownload = lastRankingFileDownload;
    }
}
