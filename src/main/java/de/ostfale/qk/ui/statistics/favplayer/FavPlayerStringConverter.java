package de.ostfale.qk.ui.statistics.favplayer;

import de.ostfale.qk.ui.statistics.playerinfo.PlayerInfoDTO;
import de.ostfale.qk.ui.statistics.playerinfo.PlayerInfoService;
import jakarta.inject.Inject;
import javafx.util.StringConverter;

public class FavPlayerStringConverter extends StringConverter<PlayerInfoDTO> {

    @Inject
    PlayerInfoService playerInfoService;

    @Override
    public String toString(PlayerInfoDTO player) {
        return player.getPlayerName();
    }

    @Override
    public PlayerInfoDTO fromString(String playerName) {
        PlayerInfoDTO player = playerInfoService.getPlayerInfosForPlayer(playerName);
        if (player == null) {
            throw new IllegalStateException("No players found");
        }
        return player;
    }
}
