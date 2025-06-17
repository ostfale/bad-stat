package de.ostfale.qk.domain.match;

public class DisciplineMatch implements Match {

    private String matchDate;
    private String playerOneName;
    private String playerTwoName;
    private String teamPlayerOneName;
    private String teamPlayerTwoName;

    @Override
    public String getFirstPlayerTeamName() {
        return "";
    }

    @Override
    public String getSecondPlayerTeamName() {
        return "";
    }

    @Override
    public String getRoundName() {
        return "";
    }

    @Override
    public String getMatchDate() {
        return matchDate != null ? matchDate : "";
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }

    public String getTeamPlayerOneName() {
        return teamPlayerOneName;
    }

    public void setTeamPlayerOneName(String teamPlayerOneName) {
        this.teamPlayerOneName = teamPlayerOneName;
    }

    public String getTeamPlayerTwoName() {
        return teamPlayerTwoName;
    }

    public void setTeamPlayerTwoName(String teamPlayerTwoName) {
        this.teamPlayerTwoName = teamPlayerTwoName;
    }
}
