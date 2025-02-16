package de.ostfale.qk.db.internal.match;

import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.match.internal.model.MatchRawModel;
import de.ostfale.qk.parser.set.SetRawModel;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseMatch {

    private static final String PLAYERS_SETS_DELIMITER = ";";

    protected static final String TOURNAMENT_ID_COLUMN = "tournament_id";
    protected static final String TOURNAMENT_REFERENCED_ID_COLUMN = "id";

    public abstract Discipline getDiscipline();

    public abstract List<String> getPlayerNames();

    public abstract String getDisciplineName();

    public boolean containsPlayer(String playerName) {
        return getPlayerNames().contains(playerName);
    }

    protected String mapPlayerSetsToString(MatchRawModel matchRawModel) {
        return matchRawModel.getPlayersSets()
                .stream()
                .map(SetRawModel::getSetAsString)
                .collect(Collectors.joining(PLAYERS_SETS_DELIMITER));
    }
}
