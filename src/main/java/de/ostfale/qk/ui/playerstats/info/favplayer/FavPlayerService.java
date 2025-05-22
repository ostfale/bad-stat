package de.ostfale.qk.ui.playerstats.info.favplayer;

import de.ostfale.qk.data.player.FavoritePlayerDataJsonHandler;
import de.ostfale.qk.data.player.model.FavPlayerListData;
import de.ostfale.qk.ui.playerstats.info.PlayerInfoService;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FavPlayerService {

    private static final Logger log = Logger.getLogger(FavPlayerService.class);

    @Inject
    FavoritePlayerDataJsonHandler favoritePlayerDataJsonHandler;

    @Inject
    PlayerInfoService playerInfoService;

    private FavPlayerListData favoritePlayerListData;

    private final List<PlayerInfoDTO> favPlayers;

    public FavPlayerService() {
        favPlayers = new ArrayList<>();
    }

    @PostConstruct
    public void readFavoritePlayersFromData() {
        log.info("FavPlayerService :: Read favorite players from data");
        favoritePlayerListData = favoritePlayerDataJsonHandler.readFavoritePlayersList();
    }

    @PreDestroy
    public void writeFavoritePlayersToData() {
        if (favoritePlayerListData != null) {
            log.info("FavPlayerService :: Persist favorite players");
            favoritePlayerDataJsonHandler.writeFavoritePlayersList(favoritePlayerListData);
        }
    }

    public FavPlayerListData getFavoritePlayerListData() {
        return favoritePlayerListData;
    }


    public void addFavPlayer(PlayerInfoDTO player) {
        log.debugf("FavPlayerService :: Adding player to favorite list: %s", player);
        favoritePlayerListData.addFavoritePlayer(player);
    }

    public void addFavPlayer(String playerName) {
        PlayerInfoDTO playerInfoDTO = playerInfoService.getPlayerInfosForPlayerName(playerName);
        if (playerInfoDTO != null) {
            addFavPlayer(playerInfoDTO);
        } else {
            log.warnf("No unique player found with name: %s", playerName);
        }
    }

    public void removeFavPlayer(String playerName) {
        PlayerInfoDTO playerToRemove = favPlayers
                .stream()
                .filter(player -> player.getPlayerInfoMasterDataDTO().getPlayerName().equals(playerName))
                .findFirst().orElse(null);
        favPlayers.remove(playerToRemove);
    }
}
