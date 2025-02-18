package de.ostfale.qk.parser.match.internal.model;

import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.player.PlayerRawModel;
import de.ostfale.qk.parser.set.SetRawModel;

import java.util.List;

public class DoubleMatchRawModel extends MatchRawModel {

    private PlayerRawModel firstDoublePlayerOne;
    private PlayerRawModel firstDoublePlayerTwo;
    private PlayerRawModel secondDoublePlayerOne;
    private PlayerRawModel secondDoublePlayerTwo;

    public DoubleMatchRawModel(PlayerRawModel firstDoublePlayerOne, PlayerRawModel firstDoublePlayerTwo, PlayerRawModel secondDoublePlayerOne, PlayerRawModel secondDoublePlayerTwo, List<SetRawModel> playersSets) {
        this.firstDoublePlayerOne = firstDoublePlayerOne;
        this.firstDoublePlayerTwo = firstDoublePlayerTwo;
        this.secondDoublePlayerOne = secondDoublePlayerOne;
        this.secondDoublePlayerTwo = secondDoublePlayerTwo;
        this.playersSets.addAll(playersSets);
        hasFirstPlayerWon();
    }

    public DoubleMatchRawModel(PlayerRawModel firstDoublePlayerOne, PlayerRawModel firstDoublePlayerTwo, PlayerRawModel secondDoublePlayerOne, PlayerRawModel secondDoublePlayerTwo) {
        this.firstDoublePlayerOne = firstDoublePlayerOne;
        this.firstDoublePlayerTwo = firstDoublePlayerTwo;
        this.secondDoublePlayerOne = secondDoublePlayerOne;
        this.secondDoublePlayerTwo = secondDoublePlayerTwo;
    }

    @Override
    public Discipline getDiscipline() {
        return Discipline.DOUBLE;
    }

    @Override
    public List<String> getPlayerNames() {
        return List.of(firstDoublePlayerOne.name, firstDoublePlayerTwo.name,
                secondDoublePlayerOne.name, secondDoublePlayerTwo.name);
    }

    public PlayerRawModel getFirstDoublePlayerOne() {
        return firstDoublePlayerOne;
    }

    public void setFirstDoublePlayerOne(PlayerRawModel firstDoublePlayerOne) {
        this.firstDoublePlayerOne = firstDoublePlayerOne;
    }

    public PlayerRawModel getFirstDoublePlayerTwo() {
        return firstDoublePlayerTwo;
    }

    public void setFirstDoublePlayerTwo(PlayerRawModel firstDoublePlayerTwo) {
        this.firstDoublePlayerTwo = firstDoublePlayerTwo;
    }

    public PlayerRawModel getSecondDoublePlayerOne() {
        return secondDoublePlayerOne;
    }

    public void setSecondDoublePlayerOne(PlayerRawModel secondDoublePlayerOne) {
        this.secondDoublePlayerOne = secondDoublePlayerOne;
    }

    public PlayerRawModel getSecondDoublePlayerTwo() {
        return secondDoublePlayerTwo;
    }

    public void setSecondDoublePlayerTwo(PlayerRawModel secondDoublePlayerTwo) {
        this.secondDoublePlayerTwo = secondDoublePlayerTwo;
    }
}
