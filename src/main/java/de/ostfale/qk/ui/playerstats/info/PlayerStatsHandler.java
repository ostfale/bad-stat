package de.ostfale.qk.ui.playerstats.info;

import de.ostfale.qk.ui.app.BaseHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javafx.scene.Node;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PlayerStatsHandler implements BaseHandler {

    private static final Logger log = Logger.getLogger(PlayerStatsHandler.class);

    @Inject
    PlayerStatsController playerStatsController;

    @Override
    public Node getRootNode() {
        log.debug("PlayerStatsHandler :: Get player stats view");
        return playerStatsController.getUI();
    }
}
