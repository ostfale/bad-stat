package de.ostfale.qk.ui.dashboard.model;

public class DashBoardTourCalDTO {

    private String lastDownloadDate;
    private String thisYearsAllTournaments;
    private String thisYearsRemainingTournaments;
    private String nextYearsTournaments;

    public DashBoardTourCalDTO(String lastDownloadDate, String thisYearsAllTournaments,String thisYearsRemainingTournaments, String nextYearsTournaments) {
        this.lastDownloadDate = lastDownloadDate;
        this.thisYearsAllTournaments = thisYearsAllTournaments;
        this.thisYearsRemainingTournaments = thisYearsRemainingTournaments;
        this.nextYearsTournaments = nextYearsTournaments;
    }

    public String thisYearsFormattedTournaments() {
        return thisYearsAllTournaments + " (" + thisYearsRemainingTournaments + ")";
    }

    public DashBoardTourCalDTO() {
    }

    public String getLastDownloadDate() {
        return lastDownloadDate;
    }

    public void setLastDownloadDate(String lastDownloadDate) {
        this.lastDownloadDate = lastDownloadDate;
    }

    public String getThisYearsAllTournaments() {
        return thisYearsAllTournaments;
    }

    public void setThisYearsAllTournaments(String thisYearsAllTournaments) {
        this.thisYearsAllTournaments = thisYearsAllTournaments;
    }

    public String getNextYearsTournaments() {
        return nextYearsTournaments;
    }

    public void setNextYearsTournaments(String nextYearsTournaments) {
        this.nextYearsTournaments = nextYearsTournaments;
    }

    public String getThisYearsRemainingTournaments() {
        return thisYearsRemainingTournaments;
    }

    public void setThisYearsRemainingTournaments(String thisYearsRemainingTournaments) {
        this.thisYearsRemainingTournaments = thisYearsRemainingTournaments;
    }
}
