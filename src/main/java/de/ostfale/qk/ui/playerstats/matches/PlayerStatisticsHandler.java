package de.ostfale.qk.ui.playerstats.matches;

import de.ostfale.qk.domain.tournament.Tournament;
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

    public List<PlayerMatchStatisticsUIModel> mapFromTournaments(List<Tournament> tournaments) {
        log.debugf("Found %d tournaments to be mapped into PlayerTournamentsStatisticsModel", tournaments.size());

        return tournaments.stream()
                .map(this::mapTournamentToPlToStatDTO) // Directly map each tournament
                .toList(); // Collect as an immutable list
    }

    private PlayerMatchStatisticsUIModel mapTournamentToPlToStatDTO(Tournament tournament) {
        log.debugf("Mapping Tournament: %s", tournament.getTournamentName());




      //  PlayerMatchStatisticsUIModel rootData = PlayerMatchStatisticsUIModel.createRootData(tournament);
//        rootData.setMatchDetails(mapMatchesToChildStatistics(tournament));
//        return rootData;
        return null;
    }

/*    private List<PlayerMatchStatisticsUIModel> mapMatchesToChildStatistics(Tournament tournament) {
        return tournament.getMatches()
                .stream()
                .map(PlayerMatchStatisticsUIModel::createChildData)
                .toList(); // Use stream API for concise and functional transformation
    }*/

   /* public void refreshUI() {
        var result = tournamentService.getAllTournamentsForYearAndPlayer(2024, "Louis Sauerbrei");
        var mapped = mapFromTournaments(result);
        playerTourStatsController.updateTreeTable(mapped);
    }*/

    public TreeTableView<PlayerMatchStatisticsUIModel> getUI() {
        return playerTourStatsController.getPlStatTreeView();
    }
}
