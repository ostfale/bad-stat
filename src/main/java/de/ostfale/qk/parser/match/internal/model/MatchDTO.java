package de.ostfale.qk.parser.match.internal.model;

import de.ostfale.qk.parser.set.SetDTO;

import java.util.ArrayList;
import java.util.List;

abstract class MatchDTO implements Match {

    abstract Discipline getDiscipline();

    protected final List<SetDTO> playersSets = new ArrayList<>();

    public boolean hasFirstPlayerWon() {
        long firstWins = playersSets.stream().filter(SetDTO::firstIsBetterThanSecond).count();
        long secondWins = playersSets.size() - firstWins;
        return firstWins > secondWins;
    }

    public List<SetDTO> getPlayersSets() {
        return playersSets;
    }
}
