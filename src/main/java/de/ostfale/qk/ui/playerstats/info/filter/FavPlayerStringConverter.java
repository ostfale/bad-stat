package de.ostfale.qk.ui.playerstats.info.filter;

import de.ostfale.qk.data.player.model.FavPlayerData;
import de.ostfale.qk.ui.playerstats.info.favplayer.FavPlayerService;
import jakarta.inject.Inject;
import javafx.util.StringConverter;

public class FavPlayerStringConverter extends StringConverter<FavPlayerData> {

    @Inject
    FavPlayerService favPlayerService;

    @Override
    public String toString(FavPlayerData player) {
        return player.playerName();
    }

    @Override
    public FavPlayerData fromString(String playerName) {
        FavPlayerData player = favPlayerService.getFavoritePlayerListData().getFavPlayerDataByPlayerName(playerName);
        if (player == null) {
            throw new IllegalStateException("No players found");
        }
        return player;
    }
}
