package de.ostfale.qk.parser.match.internal.model;

import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.player.PlayerRawModel;
import de.ostfale.qk.parser.set.SetRawModel;

import java.util.List;

public class SingleMatchRawModel extends MatchRawModel {

    private PlayerRawModel firstPlayer;
    private PlayerRawModel secondPlayer;

    public SingleMatchRawModel(PlayerRawModel firstPlayer, PlayerRawModel secondPlayer, List<SetRawModel> playersSets) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.playersSets.addAll(playersSets);
        hasFirstPlayerWon();
    }

    public SingleMatchRawModel(PlayerRawModel firstPlayer, PlayerRawModel secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    @Override
    Discipline getDiscipline() {
        return Discipline.SINGLE;
    }

    public PlayerRawModel getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(PlayerRawModel firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public PlayerRawModel getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(PlayerRawModel secondPlayer) {
        this.secondPlayer = secondPlayer;
    }
}
