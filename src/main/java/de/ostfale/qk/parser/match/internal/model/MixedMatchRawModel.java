package de.ostfale.qk.parser.match.internal.model;

import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.player.PlayerRawModel;
import de.ostfale.qk.parser.set.SetRawModel;

import java.util.List;

public class MixedMatchRawModel extends MatchRawModel {

    private PlayerRawModel firstMixedPlayerOne;
    private PlayerRawModel firstMixedPlayerTwo;
    private PlayerRawModel secondMixedPlayerOne;
    private PlayerRawModel secondMixedPlayerTwo;

    public MixedMatchRawModel(PlayerRawModel firstMixedPlayerOne, PlayerRawModel firstMixedPlayerTwo, PlayerRawModel secondMixedPlayerOne, PlayerRawModel secondMixedPlayerTwo, List<SetRawModel> playersSets) {
        this.firstMixedPlayerOne = firstMixedPlayerOne;
        this.firstMixedPlayerTwo = firstMixedPlayerTwo;
        this.secondMixedPlayerOne = secondMixedPlayerOne;
        this.secondMixedPlayerTwo = secondMixedPlayerTwo;
        this.playersSets.addAll(playersSets);
        hasFirstPlayerWon();
    }

    public MixedMatchRawModel(PlayerRawModel firstMixedPlayerOne, PlayerRawModel firstMixedPlayerTwo, PlayerRawModel secondMixedPlayerOne, PlayerRawModel secondMixedPlayerTwo) {
        this.firstMixedPlayerOne = firstMixedPlayerOne;
        this.firstMixedPlayerTwo = firstMixedPlayerTwo;
        this.secondMixedPlayerOne = secondMixedPlayerOne;
        this.secondMixedPlayerTwo = secondMixedPlayerTwo;
    }

    @Override
    public Discipline getDiscipline() {
        return Discipline.MIXED;
    }

    @Override
    public List<String> getPlayerNames() {
        return List.of(firstMixedPlayerOne.getName(), firstMixedPlayerTwo.getName(),
                secondMixedPlayerOne.getName(), secondMixedPlayerTwo.getName());
    }

    public PlayerRawModel getFirstMixedPlayerOne() {
        return firstMixedPlayerOne;
    }

    public void setFirstMixedPlayerOne(PlayerRawModel firstMixedPlayerOne) {
        this.firstMixedPlayerOne = firstMixedPlayerOne;
    }

    public PlayerRawModel getFirstMixedPlayerTwo() {
        return firstMixedPlayerTwo;
    }

    public void setFirstMixedPlayerTwo(PlayerRawModel firstMixedPlayerTwo) {
        this.firstMixedPlayerTwo = firstMixedPlayerTwo;
    }

    public PlayerRawModel getSecondMixedPlayerOne() {
        return secondMixedPlayerOne;
    }

    public void setSecondMixedPlayerOne(PlayerRawModel secondMixedPlayerOne) {
        this.secondMixedPlayerOne = secondMixedPlayerOne;
    }

    public PlayerRawModel getSecondMixedPlayerTwo() {
        return secondMixedPlayerTwo;
    }

    public void setSecondMixedPlayerTwo(PlayerRawModel secondMixedPlayerTwo) {
        this.secondMixedPlayerTwo = secondMixedPlayerTwo;
    }
}
