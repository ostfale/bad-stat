package de.ostfale.qk.data.player.model;

import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.domain.player.PlayerTournamentId;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import io.quarkus.logging.Log;

import java.util.HashSet;
import java.util.Set;

public class FavPlayerListData {

    private final Set<FavPlayerData> favoritePlayers = new HashSet<>();

    public Set<FavPlayerData> getFavoritePlayers() {
        return favoritePlayers;
    }

    public void addFavoritePlayer(PlayerInfoDTO playerInfoDTO) {
        Log.debugf("PlayerCustomDataListHandler :: addPlayerCustomData(%s)", playerInfoDTO.getPlayerInfoMasterDataDTO().getPlayerName());
        var favPlayerData = fromPlayerInfoDTO(playerInfoDTO);
        favoritePlayers.add(favPlayerData);
    }

    public void removeFavoritePlayer(PlayerInfoDTO playerInfoDTO) {
        Log.debugf("PlayerCustomDataListHandler :: removePlayerCustomData(%s)", playerInfoDTO.getPlayerInfoMasterDataDTO().getPlayerName());
        var favPlayerData = fromPlayerInfoDTO(playerInfoDTO);
        favoritePlayers.remove(favPlayerData);
    }

    public FavPlayerData getFavPlayerDataByPlayerName(String name) {
        return favoritePlayers.stream().filter(favPlayerData -> favPlayerData.playerName().equals(name)).findFirst().orElse(null);
    }

    private FavPlayerData fromPlayerInfoDTO(PlayerInfoDTO playerInfoDTO) {
        var masterData = playerInfoDTO.getPlayerInfoMasterDataDTO();
        Log.debugf("FavPlayerListData :: map fromPlayerInfoDTO(%s)", masterData.getPlayerName());

        return new FavPlayerData(
                new PlayerId(masterData.getPlayerId()),
                new PlayerTournamentId(masterData.getPlayerTournamentId()),
                masterData.getPlayerName()
        );
    }
}



