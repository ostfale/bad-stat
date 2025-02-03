package de.ostfale.qk.db.internal.match;

import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.match.internal.model.MatchDTO;
import de.ostfale.qk.parser.set.SetDTO;

import java.util.stream.Collectors;

public abstract class BaseMatch {

    private static final String PLAYERS_SETS_DELIMITER = ";";

    protected static final String TOURNAMENT_ID_COLUMN = "tournament_id";
    protected static final String TOURNAMENT_REFERENCED_ID_COLUMN = "id";

    public abstract Discipline getDiscipline();

    protected String mapPlayerSetsToString(MatchDTO matchDTO) {
        return matchDTO.getPlayersSets()
                .stream()
                .map(SetDTO::getSetAsString)
                .collect(Collectors.joining(PLAYERS_SETS_DELIMITER));
    }
}
