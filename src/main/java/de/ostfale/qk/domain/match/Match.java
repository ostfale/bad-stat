package de.ostfale.qk.domain.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.ostfale.qk.domain.set.MatchSet;

import java.util.List;

public interface Match {

    String getFirstPlayerOrWithPartnerName();

    String getSecondPlayerOrWithPartnerName();

    String getRoundName();

    String getMatchDate();

    List<MatchSet> getMatchSets();

    @JsonIgnore
    default MatchType getMatchType(){
        return MatchType.ELIMINATION_MATCH;
    }
}
