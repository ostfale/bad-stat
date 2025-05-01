package de.ostfale.qk.db.internal.match;

import de.ostfale.qk.db.api.tournament.Tournament;
import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.match.internal.model.MatchRawModel;
import de.ostfale.qk.parser.set.SetRawModel;

import java.util.List;
import java.util.stream.Collectors;

public class Match implements Comparable<Match> {

    private static final String EMPTY_STRING = "";
    private static final String PLAYERS_SETS_DELIMITER = "    ";

    private static final String TOURNAMENT_ID_COLUMN = "tournament_id";
    private static final String TOURNAMENT_REFERENCED_ID_COLUMN = "id";

    private Long id;

    private Tournament associatedTournament;

    private Discipline discipline;

    private int matchOrder = 0;
    private String disciplineName = "";
    private String roundName = "";
    private String matchDuration = "";
    private String matchResult = "";

    private String teamOnePlayerOneName;
    private String teamOnePlayerTwoName = EMPTY_STRING;
    private String teamTwoPlayerOneName;
    private String teamTwoPlayerTwoName = EMPTY_STRING;

    public Match() {
    }

    public Match(Tournament tournament, MatchRawModel matchRawModel, String disciplineName) {
        this.discipline = matchRawModel.getDiscipline();
        this.disciplineName = disciplineName;
        this.roundName = matchRawModel.getRoundName();
        this.matchOrder = extractFromRoundName(roundName);
        this.matchDuration = matchRawModel.getRoundDuration();
        this.associatedTournament = tournament;
        this.matchResult = mapPlayerSetsToString(matchRawModel);
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

    public String getPlayerOrTeamOne() {
        if (getPlayerNames().size() == 2) {
            return teamOnePlayerOneName;
        } else if (getPlayerNames().size() == 4) {
            return teamOnePlayerOneName + " / " + teamOnePlayerTwoName;
        }
        throw new IllegalStateException("Match has no player or team one");
    }

    public String getPlayerOrTeamTwo() {
        if (getPlayerNames().size() == 2) {
            return teamTwoPlayerOneName;
        } else if (getPlayerNames().size() == 4) {
            return teamTwoPlayerOneName + " / " + teamTwoPlayerTwoName;
        }
        throw new IllegalStateException("Match has no player or team one");
    }

    public List<String> getPlayerNames() {
        var allPlayersList = List.of(teamOnePlayerOneName, teamOnePlayerTwoName, teamTwoPlayerOneName, teamTwoPlayerTwoName);
        return allPlayersList.stream().filter(playerName -> !playerName.isBlank()).toList();
    }

    public String mapPlayerSetsToString(MatchRawModel matchRawModel) {
        return matchRawModel.getPlayersSets()
                .stream()
                .map(SetRawModel::toString)
                .collect(Collectors.joining(PLAYERS_SETS_DELIMITER));
    }

    public int getMatchOrder() {
        return matchOrder;
    }

    public void setMatchOrder(int matchOrder) {
        this.matchOrder = matchOrder;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
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

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String playersSets) {
        this.matchResult = playersSets;
    }

    @Override
    public int compareTo(Match other) {
        if (this.discipline == other.discipline) {
            return Integer.compare(other.matchOrder, this.matchOrder);
        }

        // Define the custom order: SINGLE -> DOUBLE -> MIXED
        if (this.discipline == Discipline.SINGLE) {
            return -1;  // SINGLE comes before everything
        } else if (this.discipline == Discipline.MIXED) {
            return 1;   // MIXED comes after everything
        } else if (this.discipline == Discipline.DOUBLE) {
            // DOUBLE comes after SINGLE but before MIXED
            return other.discipline == Discipline.SINGLE ? 1 : -1;
        }

        return 0; // fallback case
    }

    private int extractFromRoundName(String roundName) {
        return MatchOrder.lookupOrder(roundName);
    }
}
