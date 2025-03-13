package de.ostfale.qk.ui.statistics.favplayer;

import de.ostfale.qk.ui.statistics.playerinfo.PlayerInfoDTO;
import de.ostfale.qk.ui.statistics.playerinfo.PlayerInfoHandler;
import jakarta.inject.Inject;
import javafx.util.StringConverter;

import java.util.List;

public class FavPlayerStringConverter extends StringConverter<PlayerInfoDTO> {

    @Inject
    PlayerInfoHandler playerInfoHandler;

    @Override
    public String toString(PlayerInfoDTO player) {
        return player.getPlayerName();
    }

    @Override
    public PlayerInfoDTO fromString(String playerName) {
        List<PlayerInfoDTO> players = playerInfoHandler.findPlayerByName(playerName);
        switch (players.size()) {
            case 0 -> throw new IllegalStateException("No players found");
            case 1 -> {
                return players.getFirst();
            }
            default -> throw new IllegalStateException("Multiple players found");
        }
    }
}
