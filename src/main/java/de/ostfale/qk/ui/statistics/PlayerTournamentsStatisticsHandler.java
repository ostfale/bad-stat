package de.ostfale.qk.ui.statistics;

import de.ostfale.qk.db.api.tournament.Tournament;
import de.ostfale.qk.db.api.tournament.TournamentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class PlayerTournamentsStatisticsHandler {

    private static final Logger log = Logger.getLogger(PlayerTournamentsStatisticsHandler.class);

    @Inject
    PlayerTournamentsStatisticsTreeTableController playerTourStatsController;

    @Inject
    TournamentService tournamentService;

    public List<PlayerMatchStatistics> mapFromTournaments(List<Tournament> tournaments) {
        log.debugf("Found %d tournaments to be mapped into PlayerTournamentsStatisticsModel", tournaments.size());

        return tournaments.stream()
                .map(this::mapTournamentToPlToStatDTO) // Directly map each tournament
                .toList(); // Collect as an immutable list
    }

    private PlayerMatchStatistics mapTournamentToPlToStatDTO(Tournament tournament) {
        log.debugf("Mapping Tournament: %s", tournament.getTournamentName());

        PlayerMatchStatistics rootData = PlayerMatchStatistics.createRootData(tournament);
        rootData.setMatchDetails(mapMatchesToChildStatistics(tournament));
        return rootData;
    }

    private List<PlayerMatchStatistics> mapMatchesToChildStatistics(Tournament tournament) {
        return tournament.getMatches()
                .stream()
                .map(PlayerMatchStatistics::createChildData)
                .toList(); // Use stream API for concise and functional transformation
    }

    public void refreshUI() {
        var result = tournamentService.getAllTournamentsForYearAndPlayer(2024, "Louis Sauerbrei");
        var mapped = mapFromTournaments(result);
        playerTourStatsController.updateTreeTable(mapped);
    }

    public TreeTableView<PlayerMatchStatistics> getUI() {
        TreeTableView<PlayerMatchStatistics> ttv = playerTourStatsController.getPlStatTreeView();
        AnchorPane.setTopAnchor(ttv, 0.0);
        AnchorPane.setLeftAnchor(ttv, 0.0);
        AnchorPane.setRightAnchor(ttv, 0.0);
        AnchorPane.setBottomAnchor(ttv, 0.0);
        return ttv;
    }
}
