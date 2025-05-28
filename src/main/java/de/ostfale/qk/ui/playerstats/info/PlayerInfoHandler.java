package de.ostfale.qk.ui.playerstats.info;

import de.ostfale.qk.data.player.model.FavPlayerData;
import de.ostfale.qk.ui.app.BaseHandler;
import io.quarkiverse.fx.views.FxViewRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import javafx.scene.Node;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PlayerInfoHandler implements BaseHandler {

    private static final Logger log = Logger.getLogger(PlayerInfoHandler.class);

    private static final String PLAYER_INFO_FXML = "player-stat-info";

    @Inject
    FxViewRepository fxViewRepository;

    @Override
    public Node getRootNode() {
        log.debug("PlayerInfoHandler :: Get player info view");
        return fxViewRepository.getViewData(PLAYER_INFO_FXML).getRootNode();
    }

    public void onChangedFavorite(@Observes FavPlayerData favPlayerData) {
        log.debugf("PlayerInfoHandler :: Favorite player changed to %s", favPlayerData);
        updatePlayerInfo(favPlayerData);
    }

    public void updatePlayerInfo(FavPlayerData favPlayerData) {
        PlayerInfoController controller = fxViewRepository.getViewData(PLAYER_INFO_FXML).getController();
        controller.updatePlayerInfoUI(favPlayerData);
        controller.clearPlayerSearchField();
    }
}
