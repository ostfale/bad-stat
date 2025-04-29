package de.ostfale.qk.ui.statistics.playerinfo;

import de.ostfale.qk.db.api.tournament.Tournament;
import de.ostfale.qk.parser.tournament.internal.model.TournamentRawModel;
import de.ostfale.qk.parser.tournament.internal.model.TournamentYearRawModel;
import de.ostfale.qk.ui.app.BaseHandler;
import io.quarkiverse.fx.views.FxViewRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.scene.Node;
import org.jboss.logging.Logger;

@Singleton
public class PlayerInfoHandler implements BaseHandler {

    private static final Logger log = Logger.getLogger(PlayerInfoHandler.class);

    private static final String PLAYER_INFO_FXML = "player-stat-info";

    @Inject
    FxViewRepository fxViewRepository;

    @Inject
    PlayerInfoController playerInfoController;

    @Override
    public Node getRootNode() {
        return fxViewRepository.getViewData(PLAYER_INFO_FXML).getRootNode();
    }

   /* public TournamentsStatistic updateOrCreatePlayerTournamentsStatistics(PlayerInfoDTO playerDTO) {
        Objects.requireNonNull(playerDTO, "Player name must not be null");

        // check if there is already a statistics entry in the database for this player
        TournamentsStatistic statistic = tournamentsStatisticService.findByPlayerId(playerDTO.getPlayerId());
        if (statistic.hasStatisticForPlayer()) {
            log.debugf("PlayerInfoHandler :: Updating player %s with tournament statistics", playerDTO.getPlayerName());
            return statistic;
        } else {
            log.debugf("PlayerInfoHandler :: Creating tournaments statistics overview for player %s ", playerDTO.getPlayerName());
            List<TournamentsStatisticsDTO> tournamentsStatisticsDTOs = readPlayersTournamentsForLastFourYears(playerDTO);
            TournamentsStatistic tournamentsStatistic = new TournamentsStatistic();
            tournamentsStatistic.setPlayerId(playerDTO.getPlayerId());
            tournamentsStatisticsDTOs.forEach(statisticsDTO -> {
                RecentYears year = RecentYears.lookup(statisticsDTO.year());
                switch (year) {
                    case RecentYears.CURRENT_YEAR ->
                            tournamentsStatistic.setYearPlayedTournaments(statisticsDTO.allTournaments());
                    case YEAR_MINUS_1 ->
                            tournamentsStatistic.setYearMinusOnePlayedTournaments(statisticsDTO.allTournaments());
                    case YEAR_MINUS_2 ->
                            tournamentsStatistic.setYearMinusTwoPlayedTournaments(statisticsDTO.allTournaments());
                    case YEAR_MINUS_3 ->
                            tournamentsStatistic.setYearMinusThreePlayedTournaments(statisticsDTO.allTournaments());
                    default -> log.errorf("TournamentsStatistic Mapper :: Unknown year: %s", year);
                }
            });

            tournamentsStatisticService.save(tournamentsStatistic);
            Player player = playerServiceProvider.findPlayerById(playerDTO.getPlayerId());
            playerServiceProvider.updatePlayersTournamentId(player, playerDTO.getPlayerTournamentId());
            return tournamentsStatistic;
        }
    }*/

   /* public void updateAndSavePlayerTournamentsStatistics(PlayerInfoDTO playerDTO, Integer year) {
        log.debugf("PlayerInfoHandler :: Update player %s statistics for year %d", playerDTO.getPlayerName(), year);
        List<TournamentRawModel> tourPlayerList= webService.getTournamentsForYearAndPlayer(year, playerDTO.getPlayerTournamentId());

    }

    public List<TournamentsStatisticsDTO> readPlayersTournamentsForLastFourYears(PlayerInfoDTO player) {
        List<TournamentsStatisticsDTO> tournamentsStatisticsDTOs = new ArrayList<>();
        Stream.of(RecentYears.values()).forEach(recentYears -> {
            Integer nofTournaments = webService.getNumberOfTournamentsForYearAndPlayer(recentYears.getValue(), player.getPlayerTournamentId());
            tournamentsStatisticsDTOs.add(new TournamentsStatisticsDTO(recentYears.getValue(), nofTournaments, 0));
            log.debugf("Read tournaments for player %s for year %d: %d", player.getPlayerName(), recentYears.getValue(), nofTournaments);
        });
        return tournamentsStatisticsDTOs;
    }*/

    // TODO - find better solution
    private static Tournament getTournamentInfos(TournamentYearRawModel tournamentYearRawModel, TournamentRawModel tInfo) {
        String tournamentId = tInfo.getTournamentId();
        String tournamentName = tInfo.getTournamentName();
        String tournamentOrganisation = tInfo.getTournamentOrganisation();
        String tournamentLocation = tInfo.getTournamentLocation();
        String tournamentDate = tInfo.getTournamentDate();
        Integer year = Integer.parseInt(tournamentYearRawModel.year());
        return new Tournament(tournamentId, tournamentName, tournamentOrganisation, tournamentLocation, tournamentDate, year);
    }
}
