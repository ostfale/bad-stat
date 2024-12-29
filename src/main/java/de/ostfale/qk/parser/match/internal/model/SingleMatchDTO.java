package de.ostfale.qk.parser.match.internal.model;

import de.ostfale.qk.parser.player.PlayerDTO;
import de.ostfale.qk.parser.set.SetDTO;

import java.util.ArrayList;
import java.util.List;

public class SingleMatchDTO extends MatchDTO {

    private PlayerDTO firstPlayer;
    private PlayerDTO secondPlayer;

    private final List<SetDTO> playersSets = new ArrayList<>();

    public SingleMatchDTO(PlayerDTO firstPlayer, PlayerDTO secondPlayer, List<SetDTO> playersSets) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.playersSets.addAll(playersSets);
    }

    @Override
    Discipline getDiscipline() {
        return Discipline.SINGLE;
    }

    public boolean hasFirstPlayerWon() {
        long firstWins = playersSets.stream().filter(SetDTO::firstIsBetterThanSecond).count();
        long secondWins = playersSets.size() - firstWins;
        return firstWins > secondWins;
    }

    public List<SetDTO> getPlayersSets() {
        return playersSets;
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
