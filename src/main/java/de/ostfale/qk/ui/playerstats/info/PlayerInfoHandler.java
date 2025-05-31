package de.ostfale.qk.ui.playerstats.info;

import de.ostfale.qk.data.player.model.FavPlayerData;
import de.ostfale.qk.ui.app.BaseHandler;
import io.quarkiverse.fx.views.FxViewRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import javafx.scene.Node;

@ApplicationScoped
public class PlayerInfoHandler implements BaseHandler {

    private static final String PLAYER_INFO_FXML = "player-stat-info";

    private final FxViewRepository fxViewRepository;
    private final PlayerInfoService playerInfoService;

    @Inject
    public PlayerInfoHandler(FxViewRepository fxViewRepository, PlayerInfoService playerInfoService) {
        this.fxViewRepository = fxViewRepository;
        this.playerInfoService = playerInfoService;
    }

    @Override
    public Node getRootNode() {
        Log.debug("PlayerInfoHandler :: Get player info view");
        return fxViewRepository.getViewData(PLAYER_INFO_FXML).getRootNode();
    }

    public void onChangedFavorite(@Observes FavPlayerData favPlayerData) {
        Log.debugf("PlayerInfoHandler :: Favorite player changed to %s", favPlayerData);
        PlayerInfoController controller = getPlayerInfoController();
        var playerInfoDTO = playerInfoService.getPlayerInfoDTO(favPlayerData);
        controller.updatePlayerMasterData(playerInfoDTO.getPlayerInfoMasterDataDTO());
        controller.updateDynamicPlayerInfo(playerInfoDTO);
        controller.updatePlayerMatchesStatsForYear(favPlayerData);
        controller.updateTreeMatchStatistics(playerInfoDTO);
        controller.clearPlayerSearchField();
    }

    private PlayerInfoController getPlayerInfoController() {
        return fxViewRepository.getViewData(PLAYER_INFO_FXML).getController();
    }
}
