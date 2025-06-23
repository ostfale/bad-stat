package de.ostfale.qk.domain.match;

import de.ostfale.qk.domain.set.MatchSet;

import java.util.List;

public interface Match {

    String getFirstPlayerTeamName();

    String getSecondPlayerTeamName();

    String getRoundName();

    String getMatchDate();

    List<MatchSet> getMatchSets();

    default MatchType getMatchType(){
        return MatchType.ELIMINATION_MATCH;
    }
}
