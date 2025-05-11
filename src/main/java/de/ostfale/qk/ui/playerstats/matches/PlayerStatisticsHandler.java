package de.ostfale.qk.ui.playerstats.matches;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javafx.scene.control.TreeTableView;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class PlayerStatisticsHandler {

    private static final Logger log = Logger.getLogger(PlayerStatisticsHandler.class);

    @Inject
    PlayerStatisticsController playerTourStatsController;

    public TreeTableView<PlayerMatchStatisticsUIModel> getUI() {
        return playerTourStatsController.getPlStatTreeView();
    }

    public void updatePlayerMatchStatistics(List<PlayerMatchStatisticsUIModel> tournamentMatches) {
        log.debugf("PlayerStatisticsHandler :: Updating UI with %d tournaments", tournamentMatches.size());
        playerTourStatsController.updateTreeTable(tournamentMatches);
    }
}
