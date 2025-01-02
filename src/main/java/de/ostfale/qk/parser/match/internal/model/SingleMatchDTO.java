package de.ostfale.qk.parser.match.internal.model;

import de.ostfale.qk.parser.discipline.Discipline;
import de.ostfale.qk.parser.player.PlayerDTO;
import de.ostfale.qk.parser.set.SetDTO;

import java.util.List;

public class SingleMatchDTO extends MatchDTO {

    private PlayerDTO firstPlayer;
    private PlayerDTO secondPlayer;

    public SingleMatchDTO(PlayerDTO firstPlayer, PlayerDTO secondPlayer, List<SetDTO> playersSets) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.playersSets.addAll(playersSets);
    }

    public SingleMatchDTO(PlayerDTO firstPlayer, PlayerDTO secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    @Override
    Discipline getDiscipline() {
        return Discipline.SINGLE;
    }

    public PlayerDTO getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(PlayerDTO firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public PlayerDTO getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(PlayerDTO secondPlayer) {
        this.secondPlayer = secondPlayer;
    }
}
