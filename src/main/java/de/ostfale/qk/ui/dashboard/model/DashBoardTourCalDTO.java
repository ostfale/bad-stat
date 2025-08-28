package de.ostfale.qk.ui.dashboard.model;

public class DashBoardTourCalDTO {

    private String lastDownloadDate;
    private String thisYearsTournaments;
    private String nextYearsTournaments;

    public DashBoardTourCalDTO(String lastDownloadDate, String thisYearsTournaments, String nextYearsTournaments) {
        this.lastDownloadDate = lastDownloadDate;
        this.thisYearsTournaments = thisYearsTournaments;
        this.nextYearsTournaments = nextYearsTournaments;
    }

    public DashBoardTourCalDTO() {
    }

    public String getLastDownloadDate() {
        return lastDownloadDate;
    }

    public void setLastDownloadDate(String lastDownloadDate) {
        this.lastDownloadDate = lastDownloadDate;
    }

    public String getThisYearsTournaments() {
        return thisYearsTournaments;
    }

    public void setThisYearsTournaments(String thisYearsTournaments) {
        this.thisYearsTournaments = thisYearsTournaments;
    }

    public String getNextYearsTournaments() {
        return nextYearsTournaments;
    }

    public void setNextYearsTournaments(String nextYearsTournaments) {
        this.nextYearsTournaments = nextYearsTournaments;
    }
}
