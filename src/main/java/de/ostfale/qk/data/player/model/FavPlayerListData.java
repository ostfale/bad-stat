package de.ostfale.qk.data.player.model;

import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import org.jboss.logging.Logger;

import java.util.HashSet;
import java.util.Set;

public class FavPlayerListData {

    Logger log = Logger.getLogger(FavPlayerListData.class);

    private final Set<FavPlayerData> favoritePlayers = new HashSet<>();

    public Set<FavPlayerData> getFavoritePlayers() {
        return favoritePlayers;
    }

    public void addFavoritePlayer(PlayerInfoDTO playerInfoDTO) {
        log.debugf("PlayerCustomDataListHandler :: addPlayerCustomData(%s)", playerInfoDTO.getPlayerInfoMasterDataDTO().getPlayerName());
        var favPlayerData = fromPlayerInfoDTO(playerInfoDTO);
        favoritePlayers.add(favPlayerData);
    }

    public void removeFavoritePlayer(PlayerInfoDTO playerInfoDTO) {
        log.debugf("PlayerCustomDataListHandler :: removePlayerCustomData(%s)", playerInfoDTO.getPlayerInfoMasterDataDTO().getPlayerName());
        var favPlayerData = fromPlayerInfoDTO(playerInfoDTO);
        favoritePlayers.remove(favPlayerData);
    }

    private FavPlayerData fromPlayerInfoDTO(PlayerInfoDTO playerInfoDTO) {
        log.debugf("FavPlayerListData :: map fromPlayerInfoDTO(%s)", playerInfoDTO.getPlayerInfoMasterDataDTO().getPlayerId());
        String playerId = playerInfoDTO.getPlayerInfoMasterDataDTO().getPlayerId();
        String playerName = playerInfoDTO.getPlayerInfoMasterDataDTO().getPlayerName();
        return new FavPlayerData(new PlayerId(playerId), playerName);
    }

    public FavPlayerData getFavPlayerDataByPlayerName(String name) {
        return favoritePlayers.stream().filter(favPlayerData -> favPlayerData.playerName().equals(name)).findFirst().orElse(null);
    }
}
