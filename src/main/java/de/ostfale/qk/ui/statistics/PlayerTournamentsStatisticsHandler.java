package de.ostfale.qk.ui.statistics;

import de.ostfale.qk.db.api.tournament.Tournament;
import de.ostfale.qk.db.api.tournament.TournamentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PlayerTournamentsStatisticsHandler {

    private static final Logger log = Logger.getLogger(PlayerTournamentsStatisticsHandler.class);

    @Inject
    PlayerTournamentsStatisticsTreeTableController playerTourStatsController;

    @Inject
    TournamentService tournamentService;

    public List<PlToStatDTO> mapFromTournaments(List<Tournament> tournaments) {
        log.debugf("Found %d tournaments to be mapped into PlayerTournamentsStatisticsModel", tournaments.size());

        // Map all tournaments and their matches using a helper method
        return tournaments.stream()
                .flatMap(tournament -> mapTournamentToPlToStatDTO(tournament).stream())
                .toList();
    }

    private List<PlToStatDTO> mapTournamentToPlToStatDTO(Tournament tournament) {
        // Log tournament details first
        log.debugf("Mapping Tournament: %s", tournament.getTournamentName());

        // Create the root data from the Tournament
        List<PlToStatDTO> tournamentData = new ArrayList<>();
        tournamentData.add(PlToStatDTO.createRootData(tournament));

        // Add match-specific data
        tournament.getMatches()
                .stream()
                .map(PlToStatDTO::createChildData)
                .forEach(tournamentData::add);

        return tournamentData;
    }


    public void refreshUI() {
        var result = tournamentService.getAllTournamentsForYearAndPlayer(2024, "Louis Sauerbrei");
        var mapped = mapFromTournaments(result);
        playerTourStatsController.updateTreeTable(mapped);
    }

    public TreeTableView<PlToStatDTO> getUI() {
        TreeTableView<PlToStatDTO> ttv = playerTourStatsController.getPlStatTreeView();
        AnchorPane.setTopAnchor(ttv, 0.0);
        AnchorPane.setLeftAnchor(ttv, 0.0);
        AnchorPane.setRightAnchor(ttv, 0.0);
        AnchorPane.setBottomAnchor(ttv, 0.0);
        return ttv;
    }
}
