package de.ostfale.qk.db.internal.player;

public record PlayerOverview(Long numberOfPlayer, Long numberOfMalePlayer, Long numberOfFemalePlayer) {

    @Override
    public String toString() {
        return "PlayerOverview [ #player = " + numberOfPlayer + " #mPlayer = " + numberOfMalePlayer + " #fPlayer = " + numberOfFemalePlayer + " ]";
    }
}
