package de.ostfale.qk.domain.match;

public interface Match {

    String getFirstPlayerTeamName();

    String getSecondPlayerTeamName();

    String getRoundName();

    String getMatchDate();

    default MatchType getMatchType(){
        return MatchType.ELIMINATION_MATCH;
    }
}
