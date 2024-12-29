package de.ostfale.qk.parser.match.internal.model;

import de.ostfale.qk.parser.player.PlayerDTO;
import de.ostfale.qk.parser.set.SetDTO;

import java.util.List;

public class MixedMatchDTO extends MatchDTO {

    private PlayerDTO firstDoublePlayerOne;
    private PlayerDTO firstDoublePlayerTwo;
    private PlayerDTO secondDoublePlayerOne;
    private PlayerDTO secondDoublePlayerTwo;

    public MixedMatchDTO(PlayerDTO firstDoublePlayerOne, PlayerDTO firstDoublePlayerTwo, PlayerDTO secondDoublePlayerOne, PlayerDTO secondDoublePlayerTwo, List<SetDTO> playersSets) {
        this.firstDoublePlayerOne = firstDoublePlayerOne;
        this.firstDoublePlayerTwo = firstDoublePlayerTwo;
        this.secondDoublePlayerOne = secondDoublePlayerOne;
        this.secondDoublePlayerTwo = secondDoublePlayerTwo;
        this.playersSets.addAll(playersSets);
    }

    public MixedMatchDTO(PlayerDTO firstDoublePlayerOne, PlayerDTO firstDoublePlayerTwo, PlayerDTO secondDoublePlayerOne, PlayerDTO secondDoublePlayerTwo) {
        this.firstDoublePlayerOne = firstDoublePlayerOne;
        this.firstDoublePlayerTwo = firstDoublePlayerTwo;
        this.secondDoublePlayerOne = secondDoublePlayerOne;
        this.secondDoublePlayerTwo = secondDoublePlayerTwo;
    }

    @Override
    Discipline getDiscipline() {
        return Discipline.MIXED;
    }

    public PlayerDTO getFirstDoublePlayerOne() {
        return firstDoublePlayerOne;
    }

    public void setFirstDoublePlayerOne(PlayerDTO firstDoublePlayerOne) {
        this.firstDoublePlayerOne = firstDoublePlayerOne;
    }

    public PlayerDTO getFirstDoublePlayerTwo() {
        return firstDoublePlayerTwo;
    }

    public void setFirstDoublePlayerTwo(PlayerDTO firstDoublePlayerTwo) {
        this.firstDoublePlayerTwo = firstDoublePlayerTwo;
    }

    public PlayerDTO getSecondDoublePlayerOne() {
        return secondDoublePlayerOne;
    }

    public void setSecondDoublePlayerOne(PlayerDTO secondDoublePlayerOne) {
        this.secondDoublePlayerOne = secondDoublePlayerOne;
    }

    public PlayerDTO getSecondDoublePlayerTwo() {
        return secondDoublePlayerTwo;
    }

    public void setSecondDoublePlayerTwo(PlayerDTO secondDoublePlayerTwo) {
        this.secondDoublePlayerTwo = secondDoublePlayerTwo;
    }
}
