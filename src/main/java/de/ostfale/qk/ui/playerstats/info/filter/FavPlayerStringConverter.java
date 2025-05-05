package de.ostfale.qk.ui.playerstats.info.filter;

import de.ostfale.qk.ui.playerstats.info.PlayerInfoService;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import jakarta.inject.Inject;
import javafx.util.StringConverter;

public class FavPlayerStringConverter extends StringConverter<PlayerInfoDTO> {

    @Inject
    PlayerInfoService playerInfoService;

    @Override
    public String toString(PlayerInfoDTO player) {
        return player.getPlayerInfoMasterDataDTO().getPlayerName() == null ? "" : player.getPlayerInfoMasterDataDTO().getPlayerName();
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
