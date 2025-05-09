package de.ostfale.qk.ui.playerstats.matches;

import de.ostfale.qk.parser.match.internal.model.MatchRawModel;
import de.ostfale.qk.parser.tournament.model.TournamentParserModel;
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

    public static PlayerMatchStatisticsUIModel createChildData(MatchRawModel match) {
        log.debug("PlToStatDTO :: Create new child row ");
        var dto = new PlayerMatchStatisticsUIModel();
        dto.setTournamentName(SPACE);
        dto.setTournamentDate(SPACE);
        dto.setTournamentLocation(SPACE);
        dto.setDisciplineName(match.getDiscipline().name());
        dto.setRoundName(match.getRoundName());
        dto.setPtOneName(match.getFirstPlayerOrTeamName());
        dto.setPtTwoName(match.getSecondPlayerOrTeamName());
    //    dto.setMatchResult(match.getMatchResult());
        return dto;
    }

    public static PlayerMatchStatisticsUIModel createRootData(TournamentParserModel tournamentParserModel) {
        log.debug("PlayerMatchStatisticsUIModel :: Create new root row ");
        var dto = new PlayerMatchStatisticsUIModel();
        dto.setTournamentName(tournamentParserModel.getTournamentName());
        dto.setTournamentDate(tournamentParserModel.getTournamentDate());
        dto.setTournamentLocation(tournamentParserModel.getTournamentLocation());
        return dto;
    }

    @Override
    public String toString() {
        return "PlayerMatchStatisticsUIModel{" +
                "tournamentName='" + tournamentName + '\'' +
                ", disciplineName='" + disciplineName + '\'' +
                ", roundName='" + roundName + '\'' +
                '}';
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
