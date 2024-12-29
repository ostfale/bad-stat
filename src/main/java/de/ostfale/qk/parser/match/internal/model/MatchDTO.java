package de.ostfale.qk.parser.match.internal.model;

import de.ostfale.qk.parser.set.SetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

abstract class MatchDTO implements Match {

    private static final Logger log = LoggerFactory.getLogger(MatchDTO.class);

    abstract Discipline getDiscipline();

    protected final List<SetDTO> playersSets = new ArrayList<>();

    protected Boolean hasFirstPlayerWonProp = null;

    public boolean hasFirstPlayerWon() {
        if (hasFirstPlayerWonProp != null) {
            log.debug("hasFirstPlayerWon property not null -> returned: {} ", hasFirstPlayerWonProp);
            return hasFirstPlayerWonProp;
        }

        long firstWins = playersSets.stream().filter(SetDTO::firstIsBetterThanSecond).count();
        long secondWins = playersSets.size() - firstWins;

        var result = firstWins > secondWins;
        log.debug("hasFirstPlayerWon property not set -> calculated: {} ", result);
        return result;
    }

    public List<SetDTO> getPlayersSets() {
        return playersSets;
    }

    public void setHasFirstPlayerWonProp(Boolean hasFirstPlayerWonProp) {
        this.hasFirstPlayerWonProp = hasFirstPlayerWonProp;
    }
}
