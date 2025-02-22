package de.ostfale.qk.ui.statistics;

import de.ostfale.qk.db.api.tournament.Tournament;
import de.ostfale.qk.db.internal.match.Match;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class PlayerMatchStatistics {

    private static final Logger log = Logger.getLogger(PlayerMatchStatistics.class);

    private static final String SPACE = "";

    private String tournamentDate;
    private String tournamentName;
    private String tournamentLocation;
    private String disciplineName;
    private String roundName;
    private String ptOneName;
    private String ptTwoName;
    private String matchResult;
    private List<PlayerMatchStatistics> matchDetails = new ArrayList<>();

    public static PlayerMatchStatistics createChildData(Match match) {
        log.debug("PlToStatDTO :: Create new child row ");
        var dto = new PlayerMatchStatistics();
        dto.setTournamentName(SPACE);
        dto.setTournamentDate(SPACE);
        dto.setTournamentLocation(SPACE);
        dto.setDisciplineName(match.getDisciplineName());
        dto.setRoundName(match.getRoundName());
        dto.setPtOneName(match.getPlayerOrTeamOne());
        dto.setPtTwoName(match.getPlayerOrTeamTwo());
        dto.setMatchResult(match.getMatchResult());
        return dto;
    }

    public static PlayerMatchStatistics createRootData(Tournament tournament) {
        log.debug("PlToStatDTO :: Create new root row ");
        var dto = new PlayerMatchStatistics();
        dto.setTournamentName(tournament.getTournamentName());
        dto.setTournamentDate(tournament.getTournamentDate());
        dto.setTournamentLocation(tournament.getTournamentLocation());
        return dto;
    }

    public PlayerMatchStatistics() {
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    public String getPtOneName() {
        return ptOneName;
    }

    public void setPtOneName(String ptOneName) {
        this.ptOneName = ptOneName;
    }

    public String getPtTwoName() {
        return ptTwoName;
    }

    public void setPtTwoName(String ptTwoName) {
        this.ptTwoName = ptTwoName;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public List<PlayerMatchStatistics> getMatchDetails() {
        return matchDetails;
    }

    public void setMatchDetails(List<PlayerMatchStatistics> matchDetails) {
        this.matchDetails = matchDetails;
    }

    public String getTournamentDate() {
        return tournamentDate;
    }

    public void setTournamentDate(String tournamentDate) {
        this.tournamentDate = tournamentDate;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getTournamentLocation() {
        return tournamentLocation;
    }

    public void setTournamentLocation(String tournamentLocation) {
        this.tournamentLocation = tournamentLocation;
    }
}
