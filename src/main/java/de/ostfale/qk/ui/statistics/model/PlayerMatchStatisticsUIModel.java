package de.ostfale.qk.ui.statistics.model;

import de.ostfale.qk.db.api.tournament.Tournament;
import de.ostfale.qk.db.internal.match.Match;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class PlayerMatchStatisticsUIModel {

    private static final Logger log = Logger.getLogger(PlayerMatchStatisticsUIModel.class);

    private static final String SPACE = "";

    private String tournamentDate;
    private String tournamentName;
    private String tournamentLocation;
    private String disciplineName;
    private String roundName;
    private String ptOneName;
    private String ptTwoName;
    private String matchResult;
    private List<PlayerMatchStatisticsUIModel> matchDetails = new ArrayList<>();

    public static PlayerMatchStatisticsUIModel createChildData(Match match) {
        log.debug("PlToStatDTO :: Create new child row ");
        var dto = new PlayerMatchStatisticsUIModel();
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

    public static PlayerMatchStatisticsUIModel createRootData(Tournament tournament) {
        log.debug("PlToStatDTO :: Create new root row ");
        var dto = new PlayerMatchStatisticsUIModel();
        dto.setTournamentName(tournament.getTournamentName());
        dto.setTournamentDate(tournament.getTournamentDate());
        dto.setTournamentLocation(tournament.getTournamentLocation());
        return dto;
    }

    public PlayerMatchStatisticsUIModel() {
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

    public List<PlayerMatchStatisticsUIModel> getMatchDetails() {
        return matchDetails;
    }

    public void setMatchDetails(List<PlayerMatchStatisticsUIModel> matchDetails) {
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
