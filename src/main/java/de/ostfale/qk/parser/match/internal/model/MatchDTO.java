package de.ostfale.qk.parser.match.internal.model;

import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.set.SetDTO;
import org.jboss.logging.Logger;


import java.util.ArrayList;
import java.util.List;

public abstract class MatchDTO implements Match {

    private static final Logger log = Logger.getLogger(MatchDTO.class);

    abstract Discipline getDiscipline();

    protected final List<SetDTO> playersSets = new ArrayList<>();

    private  MatchInfoDTO matchInfoDTO = new MatchInfoDTO();

    // no regular match
    protected Boolean isMatchWalkover = null;
    protected Boolean isMatchRetired = null;
    protected Boolean isTreeSystem = Boolean.TRUE;

    protected Boolean hasFirstPlayerWonProp = null;

    public boolean hasFirstPlayerWon() {
        if (hasFirstPlayerWonProp != null) {
            log.debugf("hasFirstPlayerWon property not null -> returned: {} ", hasFirstPlayerWonProp);
            return hasFirstPlayerWonProp;
        }

        long firstWins = playersSets.stream().filter(SetDTO::firstIsBetterThanSecond).count();
        long secondWins = playersSets.size() - firstWins;

        if (firstWins > secondWins) {
            hasFirstPlayerWonProp = Boolean.TRUE;
        }
        else  {
            hasFirstPlayerWonProp = Boolean.FALSE;
        }

        var result = firstWins > secondWins;
        log.debugf("hasFirstPlayerWon property not set -> calculated: {} ", result);
        return result;
    }

    public MatchInfoDTO getMatchInfoDTO() {
        return matchInfoDTO;
    }

    public void setMatchInfoDTO(MatchInfoDTO matchInfoDTO) {
        this.matchInfoDTO = matchInfoDTO;
    }

    public List<SetDTO> getPlayersSets() {
        return playersSets;
    }

    public void setHasFirstPlayerWonProp(Boolean hasFirstPlayerWonProp) {
        this.hasFirstPlayerWonProp = hasFirstPlayerWonProp;
    }

    public void setMatchRetired(Boolean matchRetired) {
        isMatchRetired = matchRetired;
    }

    public Boolean getMatchRetired() {
        return isMatchRetired;
    }
}
