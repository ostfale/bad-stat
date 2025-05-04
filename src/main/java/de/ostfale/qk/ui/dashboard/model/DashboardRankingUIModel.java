package de.ostfale.qk.ui.dashboard.model;

import de.ostfale.qk.domain.player.PlayerOverview;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardRankingUIModel {

    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm";

    private LocalDateTime lastRankingFileDownload;
    private String downloadFileName;
    private Long nofPlayers = 0L;
    private Long nofMalePlayers = 0L;
    private Long nofFemalePlayers = 0L;

    private String dbUpdateInCW;

    public DashboardRankingUIModel(String rankingFileName, int nofPlayers, int nofMalePlayers, int nofFemalePlayers) {
        this.downloadFileName = rankingFileName;
        this.nofPlayers = (long) nofPlayers;
        this.nofMalePlayers = (long) nofMalePlayers;
        this.nofFemalePlayers = (long) nofFemalePlayers;
    }

    public DashboardRankingUIModel(PlayerOverview playerOverview) {
        this.nofPlayers = playerOverview.numberOfPlayer();
        this.nofMalePlayers = playerOverview.numberOfMalePlayer();
        this.nofFemalePlayers = playerOverview.numberOfFemalePlayer();
    }

    public String getDbUpdateInCW() {
        return dbUpdateInCW;
    }

    public void setDbUpdateInCW(String dbUpdateInCW) {
        this.dbUpdateInCW = dbUpdateInCW;
    }

    public LocalDateTime getLastRankingFileDownload() {
        return lastRankingFileDownload;
    }

    public void setLastRankingFileDownload(LocalDateTime lastRankingFileDownload) {
        this.lastRankingFileDownload = lastRankingFileDownload;
    }

    public DashboardRankingUIModel() {
    }

    public Long getNofPlayers() {
        return nofPlayers;
    }

    public void setNofPlayers(Long nofPlayers) {
        this.nofPlayers = nofPlayers;
    }

    public Long getNofMalePlayers() {
        return nofMalePlayers;
    }

    public void setNofMalePlayers(Long nofMalePlayers) {
        this.nofMalePlayers = nofMalePlayers;
    }

    public Long getNofFemalePlayers() {
        return nofFemalePlayers;
    }

    public void setNofFemalePlayers(Long nofFemalePlayers) {
        this.nofFemalePlayers = nofFemalePlayers;
    }

    public String getFileDownloadTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        if (lastRankingFileDownload != null) {
            return lastRankingFileDownload.format(formatter);
        }
        return "";
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }
}
