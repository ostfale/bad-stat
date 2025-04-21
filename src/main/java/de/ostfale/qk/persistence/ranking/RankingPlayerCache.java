package de.ostfale.qk.persistence.ranking;

import de.ostfale.qk.domain.player.GenderType;
import de.ostfale.qk.domain.player.Player;

import java.util.List;

public record RankingPlayerCache(
        List<Player> players
) {

    public long getNumberOfPlayers() {
        return players.size();
    }

    public long getNumberOfFemalePlayers() {
        return players
                .parallelStream()
                .filter(player -> player.getGender().equals(GenderType.FEMALE))
                .toList()
                .size();
    }

    public long getNumberOfMalePlayers() {
        return players
                .parallelStream()
                .filter(player -> player.getGender().equals(GenderType.MALE))
                .toList()
                .size();
    }
}
