package de.ostfale.qk.ui.playerstats.info;

import de.ostfale.qk.ui.app.BaseHandler;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import io.quarkiverse.fx.RunOnFxThread;
import io.quarkiverse.fx.views.FxViewRepository;
import io.quarkus.vertx.ConsumeEvent;
import jakarta.enterprise.context.ApplicationScoped;
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

    @ConsumeEvent("player-selected")
    public void consumeFavChange(PlayerInfoDTO player) {
        updatePlayerInfo(player);
    }

    @RunOnFxThread
    public void updatePlayerInfo(PlayerInfoDTO player) {
        PlayerInfoController controller = fxViewRepository.getViewData(PLAYER_INFO_FXML).getController();
        controller.updatePlayerInfo(player);
        controller.updatePlayerMatchesStatics(player);
    }
}
