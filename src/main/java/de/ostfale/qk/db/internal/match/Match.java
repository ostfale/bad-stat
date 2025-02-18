package de.ostfale.qk.db.internal.match;

import de.ostfale.qk.db.api.tournament.Tournament;
import de.ostfale.qk.parser.match.internal.model.MatchRawModel;
import de.ostfale.qk.parser.set.SetRawModel;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Match {

    private static final String EMPTY_STRING = "";
    private static final String PLAYERS_SETS_DELIMITER = ";";

    protected static final String TOURNAMENT_ID_COLUMN = "tournament_id";
    protected static final String TOURNAMENT_REFERENCED_ID_COLUMN = "id";

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = TOURNAMENT_ID_COLUMN, referencedColumnName = TOURNAMENT_REFERENCED_ID_COLUMN, nullable = false)
    private Tournament associatedTournament;

    private String disciplineName = "";
    private String roundName = "";
    private String matchDuration = "";
    private String playersSets = "";

    private String teamOnePlayerOneName;
    private String teamOnePlayerTwoName = EMPTY_STRING;
    private String teamTwoPlayerOneName;
    private String teamTwoPlayerTwoName = EMPTY_STRING;

    public Match() {
    }

    public Match(Tournament tournament, MatchRawModel matchRawModel, String disciplineName) {
        this.disciplineName = disciplineName;
        this.roundName = matchRawModel.getRoundName();
        this.matchDuration = matchRawModel.getRoundDuration();
        this.associatedTournament = tournament;
        var playerNames = matchRawModel.getPlayerNames();
        if (playerNames.size() == 2) {
            this.teamOnePlayerOneName = playerNames.getFirst();
            this.teamTwoPlayerOneName = playerNames.getLast();
        } else {
            this.teamOnePlayerOneName = playerNames.getFirst();
            this.teamOnePlayerTwoName = playerNames.get(1);
            this.teamTwoPlayerOneName = playerNames.get(2);
            this.teamTwoPlayerTwoName = playerNames.getLast();
        }
    }

    public boolean containsPlayer(String playerName) {
        return getPlayerNames().contains(playerName);
    }

    public List<String> getPlayerNames() {
        var allPlayersList = List.of(teamOnePlayerOneName, teamOnePlayerTwoName, teamTwoPlayerOneName, teamTwoPlayerTwoName);
        return allPlayersList.stream().filter(playerName -> !playerName.isBlank()).toList();
    }

    public String mapPlayerSetsToString(MatchRawModel matchRawModel) {
        return matchRawModel.getPlayersSets()
                .stream()
                .map(SetRawModel::getSetAsString)
                .collect(Collectors.joining(PLAYERS_SETS_DELIMITER));
    }

    public Long getId() {
        return id;
    }

    public String getTeamOnePlayerOneName() {
        return teamOnePlayerOneName;
    }

    public void setTeamOnePlayerOneName(String teamOnePlayerOneName) {
        this.teamOnePlayerOneName = teamOnePlayerOneName;
    }

    public String getTeamOnePlayerTwoName() {
        return teamOnePlayerTwoName;
    }

    public void setTeamOnePlayerTwoName(String teamOnePlayerTwoName) {
        this.teamOnePlayerTwoName = teamOnePlayerTwoName;
    }

    public String getTeamTwoPlayerOneName() {
        return teamTwoPlayerOneName;
    }

    public void setTeamTwoPlayerOneName(String teamTwoPlayerOneName) {
        this.teamTwoPlayerOneName = teamTwoPlayerOneName;
    }

    public String getTeamTwoPlayerTwoName() {
        return teamTwoPlayerTwoName;
    }

    public void setTeamTwoPlayerTwoName(String teamTwoPlayerTwoName) {
        this.teamTwoPlayerTwoName = teamTwoPlayerTwoName;
    }

    public Tournament getAssociatedTournament() {
        return associatedTournament;
    }

    public void setAssociatedTournament(Tournament associatedTournament) {
        this.associatedTournament = associatedTournament;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getMatchDuration() {
        return matchDuration;
    }

    public void setMatchDuration(String matchDuration) {
        this.matchDuration = matchDuration;
    }

    public String getPlayersSets() {
        return playersSets;
    }

    public void setPlayersSets(String playersSets) {
        this.playersSets = playersSets;
    }
}
