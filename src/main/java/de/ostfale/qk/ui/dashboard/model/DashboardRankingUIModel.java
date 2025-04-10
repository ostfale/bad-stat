package de.ostfale.qk.ui.dashboard.model;

public class DashboardRankingUIModel {

    private String fileDownloadTimestamp;
    private String downloadFileName;
    private int nofPlayers;
    private int nofMalePlayers;
    private int nofFemalePlayers;

    public DashboardRankingUIModel(String fileDownloadTimestamp, int nofPlayers, int nofMalePlayers,int nofFemalePlayers) {
        this.fileDownloadTimestamp = fileDownloadTimestamp;
        this.nofPlayers = nofPlayers;
        this.nofMalePlayers = nofMalePlayers;
        this.nofFemalePlayers = nofFemalePlayers;
    }

    public int getNofPlayers() {
        return nofPlayers;
    }

    public void setNofPlayers(int nofPlayers) {
        this.nofPlayers = nofPlayers;
    }

    public int getNofMalePlayers() {
        return nofMalePlayers;
    }

    public void setNofMalePlayers(int nofMalePlayers) {
        this.nofMalePlayers = nofMalePlayers;
    }

    public int getNofFemalePlayers() {
        return nofFemalePlayers;
    }

    public void setNofFemalePlayers(int nofFemalePlayers) {
        this.nofFemalePlayers = nofFemalePlayers;
    }

    public String getFileDownloadTimestamp() {
        return fileDownloadTimestamp;
    }

    public void setFileDownloadTimestamp(String fileDownloadTimestamp) {
        this.fileDownloadTimestamp = fileDownloadTimestamp;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }
}
