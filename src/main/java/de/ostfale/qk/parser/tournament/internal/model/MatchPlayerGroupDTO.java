package de.ostfale.qk.parser.tournament.internal.model;

public class MatchPlayerGroupDTO {

    private MatchPlayerDTO firstPlayer;
    private MatchPlayerDTO secondPlayer;
    private boolean hasWon = false;

    public MatchPlayerGroupDTO(MatchPlayerDTO firstPlayer, MatchPlayerDTO secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public boolean isHasWon() {
        return hasWon;
    }

    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    public MatchPlayerGroupDTO(MatchPlayerDTO firstPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = null;
    }

    public MatchPlayerDTO getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(MatchPlayerDTO firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public MatchPlayerDTO getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(MatchPlayerDTO secondPlayer) {
        this.secondPlayer = secondPlayer;
    }
}
