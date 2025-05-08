package de.ostfale.qk.ui.playerstats.info.tournamentdata;

import de.ostfale.qk.parser.discipline.internal.model.DisciplineRawModel;
import de.ostfale.qk.parser.match.internal.model.Match;
import de.ostfale.qk.parser.tournament.internal.model.TournamentRawModel;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import de.ostfale.qk.ui.playerstats.matches.PlayerMatchStatisticsUIModel;
import de.ostfale.qk.ui.playerstats.matches.PlayerStatisticsController;
import de.ostfale.qk.web.internal.TournamentWebService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class PlayerTournamentsService {

    private static final Logger log = Logger.getLogger(PlayerTournamentsService.class);

    @Inject
    TournamentWebService tournamentWebService;

    @Inject
    PlayerStatisticsController playerTourStatsController;

    public void loadPlayerTournamentsForYear(int year, PlayerInfoDTO player) {
        log.debugf("Loading tournaments for player %s for year %d", player.toString(), year);
        List<TournamentRawModel> allTournaments = tournamentWebService.getTournamentsForYearAndPlayer(year, player.getPlayerInfoMasterDataDTO().getPlayerTournamentId());


        List<PlayerMatchStatisticsUIModel> mappedTournaments = allTournaments.stream().map(this::mapToUIModel).toList();
        playerTourStatsController.updateTreeTable(mappedTournaments);
        log.debugf("PlayerTournamentsService :: mapped tournaments for year %d and found: %d",year,mappedTournaments.size());
    }

    private PlayerMatchStatisticsUIModel mapToUIModel(TournamentRawModel rawModel) {
        log.debugf("Mapping Tournament: %s", rawModel.getTournamentName());
        var root = PlayerMatchStatisticsUIModel.createRootData(rawModel);
     //   rawModel.getTournamentDisciplines().stream().map()

        return root;
    }

    private PlayerMatchStatisticsUIModel mapMatch(DisciplineRawModel rawModel, Match match) {
        var dto = new PlayerMatchStatisticsUIModel();
      //  var result =rawModel.getMatches().stream().map(match ->PlayerMatchStatisticsUIModel.createChildData(match)).toList();
        return dto;
    }
}
