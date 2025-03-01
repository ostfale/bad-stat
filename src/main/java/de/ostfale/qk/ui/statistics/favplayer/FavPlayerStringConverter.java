package de.ostfale.qk.ui.statistics.favplayer;

import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.db.service.PlayerServiceProvider;
import jakarta.inject.Inject;
import javafx.util.StringConverter;

import java.util.List;

public class FavPlayerStringConverter extends StringConverter<Player> {

    @Inject
    PlayerServiceProvider serviceProvider;

    @Override
    public String toString(Player player) {
        return player.getName();
    }

    @Override
    public Player fromString(String playerName) {
        List<Player> players = serviceProvider.findPlayersByFullName(playerName);
        switch (players.size()) {
            case 0 -> throw new IllegalStateException("No players found");
            case 1 -> {
                return players.getFirst();
            }
            default -> throw new IllegalStateException("Multiple players found");
        }
    }
}
