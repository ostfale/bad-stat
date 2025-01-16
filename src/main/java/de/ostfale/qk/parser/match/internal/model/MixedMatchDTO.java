package de.ostfale.qk.parser.match.internal.model;

import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.player.PlayerDTO;
import de.ostfale.qk.parser.set.SetDTO;

import java.util.List;

public class MixedMatchDTO extends MatchDTO {

    private PlayerDTO firstMixedPlayerOne;
    private PlayerDTO firstMixedPlayerTwo;
    private PlayerDTO secondMixedPlayerOne;
    private PlayerDTO secondMixedPlayerTwo;

    public MixedMatchDTO(PlayerDTO firstMixedPlayerOne, PlayerDTO firstMixedPlayerTwo, PlayerDTO secondMixedPlayerOne, PlayerDTO secondMixedPlayerTwo, List<SetDTO> playersSets) {
        this.firstMixedPlayerOne = firstMixedPlayerOne;
        this.firstMixedPlayerTwo = firstMixedPlayerTwo;
        this.secondMixedPlayerOne = secondMixedPlayerOne;
        this.secondMixedPlayerTwo = secondMixedPlayerTwo;
        this.playersSets.addAll(playersSets);
    }

    public MixedMatchDTO(PlayerDTO firstMixedPlayerOne, PlayerDTO firstMixedPlayerTwo, PlayerDTO secondMixedPlayerOne, PlayerDTO secondMixedPlayerTwo) {
        this.firstMixedPlayerOne = firstMixedPlayerOne;
        this.firstMixedPlayerTwo = firstMixedPlayerTwo;
        this.secondMixedPlayerOne = secondMixedPlayerOne;
        this.secondMixedPlayerTwo = secondMixedPlayerTwo;
    }

    @Override
    Discipline getDiscipline() {
        return Discipline.MIXED;
    }

    public PlayerDTO getFirstMixedPlayerOne() {
        return firstMixedPlayerOne;
    }

    public void setFirstMixedPlayerOne(PlayerDTO firstMixedPlayerOne) {
        this.firstMixedPlayerOne = firstMixedPlayerOne;
    }

    public PlayerDTO getFirstMixedPlayerTwo() {
        return firstMixedPlayerTwo;
    }

    public void setFirstMixedPlayerTwo(PlayerDTO firstMixedPlayerTwo) {
        this.firstMixedPlayerTwo = firstMixedPlayerTwo;
    }

    public PlayerDTO getSecondMixedPlayerOne() {
        return secondMixedPlayerOne;
    }

    public void setSecondMixedPlayerOne(PlayerDTO secondMixedPlayerOne) {
        this.secondMixedPlayerOne = secondMixedPlayerOne;
    }

    public PlayerDTO getSecondMixedPlayerTwo() {
        return secondMixedPlayerTwo;
    }

    public void setSecondMixedPlayerTwo(PlayerDTO secondMixedPlayerTwo) {
        this.secondMixedPlayerTwo = secondMixedPlayerTwo;
    }
}
