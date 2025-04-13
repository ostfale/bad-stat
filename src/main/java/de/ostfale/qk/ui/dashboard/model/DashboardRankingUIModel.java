package de.ostfale.qk.ui.dashboard.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.ostfale.qk.db.internal.player.PlayerOverview;

public class DashboardRankingUIModel {

    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm";

    private String downloadFileName;
    private Long nofPlayers;
    private Long nofMalePlayers;
    private Long nofFemalePlayers;

    public DashboardRankingUIModel(String rankingFileName, int nofPlayers, int nofMalePlayers, int nofFemalePlayers) {
        this.downloadFileName = rankingFileName;
        this.nofPlayers = Long.valueOf(nofPlayers);
        this.nofMalePlayers = Long.valueOf(nofMalePlayers);
        this.nofFemalePlayers = Long.valueOf(nofFemalePlayers);
    }

    public DashboardRankingUIModel(PlayerOverview playerOverview) {
        this.nofPlayers = playerOverview.numberOfPlayer();
        this.nofMalePlayers = playerOverview.numberOfMalePlayer();
        this.nofFemalePlayers = playerOverview.numberOfFemalePlayer();
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
        return LocalDateTime.now().format(formatter);
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }
}
