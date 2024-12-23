package de.ostfale.qk.parser.tournament.internal;

public class TournamentMatchInfo {

    private String roundName;
    private String roundDate;
    private String roundLocation;
    private String roundDuration;

    public TournamentMatchInfo(String roundName, String roundDate, String roundLocation, String roundDuration) {
        this.roundName = roundName;
        this.roundDate = roundDate;
        this.roundLocation = roundLocation;
        this.roundDuration = roundDuration;
    }

    @Override
    public String toString() {
        return "TournamentMatchInfo{" +
                "roundName='" + roundName + '\'' +
                ", roundDate='" + roundDate + '\'' +
                ", roundLocation='" + roundLocation + '\'' +
                ", roundDuration='" + roundDuration + '\'' +
                '}';
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getRoundDate() {
        return roundDate;
    }

    public void setRoundDate(String roundDate) {
        this.roundDate = roundDate;
    }

    public String getRoundLocation() {
        return roundLocation;
    }

    public void setRoundLocation(String roundLocation) {
        this.roundLocation = roundLocation;
    }

    public String getRoundDuration() {
        return roundDuration;
    }

    public void setRoundDuration(String roundDuration) {
        this.roundDuration = roundDuration;
    }
}
