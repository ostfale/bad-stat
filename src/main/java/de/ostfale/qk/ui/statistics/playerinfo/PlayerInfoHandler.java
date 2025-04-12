package de.ostfale.qk.ui.statistics.playerinfo;

import de.ostfale.qk.db.api.tournament.Tournament;
import de.ostfale.qk.db.internal.match.TournamentsStatistic;
import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.db.service.PlayerServiceProvider;
import de.ostfale.qk.db.service.TournamentsStatisticService;
import de.ostfale.qk.parser.tournament.internal.model.TournamentRawModel;
import de.ostfale.qk.parser.tournament.internal.model.TournamentYearRawModel;
import de.ostfale.qk.ui.app.RecentYears;
import de.ostfale.qk.web.api.WebService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

@Singleton
public class PlayerInfoHandler {

    private static final Logger log = Logger.getLogger(PlayerInfoHandler.class);

    @Inject
    PlayerServiceProvider playerServiceProvider;

    @Inject
    TournamentsStatisticService tournamentsStatisticService;

    @Inject
    WebService webService;

    private List<PlayerInfoDTO> allPlayer;

    public List<PlayerInfoDTO> findAllFavoritePlayers() {
        initPlayerList();
        var favPlayers = allPlayer.stream().filter(PlayerInfoDTO::getFavorite).toList();
        log.debugf("PlayerInfoHandler :: Read all favorite players  %d players", favPlayers.size());
        return favPlayers;
    }

    public List<PlayerInfoDTO> getAllPlayer() {
        initPlayerList();
        return allPlayer;
    }

    public Integer getSingleRankingForAgeClass(PlayerInfoDTO player) {
        return calculateRanking(player, PlayerInfoDTO::getSinglePoints, "single");
    }

    public Integer getDoubleRankingForAgeClass(PlayerInfoDTO player) {
        return calculateRanking(player, PlayerInfoDTO::getDoublePoints, "double");
    }

    public Integer getMixedRankingForAgeClass(PlayerInfoDTO player) {
        return calculateRanking(player, PlayerInfoDTO::getMixedPoints, "mixed");
    }

    public TournamentsStatistic updateOrCreatePlayerTournamentsStatistics(PlayerInfoDTO playerDTO) {
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
    }

    public void toggleAndSavePlayerAsFavorite(PlayerInfoDTO playerDTO) {
        Objects.requireNonNull(playerDTO, "Player name must not be null");
        playerServiceProvider.updatePlayerAsFavorite(playerDTO.getPlayerId(),true);
    }

    public List<PlayerInfoDTO> findPlayerByName(String playerName) {
        log.debugf("PlayerInfoHandler :: Find player by name %s", playerName);
        return allPlayer.stream().filter(player -> player.getPlayerName().equalsIgnoreCase(playerName)).toList();
    }

    public void updateAndSavePlayerTournamentsStatistics(PlayerInfoDTO playerDTO, Integer year) {
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
    }



    private Integer calculateRanking(PlayerInfoDTO player, ToIntFunction<PlayerInfoDTO> pointsExtractor, String rankingType) {
        List<PlayerInfoDTO> filteredPlayers = filterByAgeClassAndGender(allPlayer, player.getAgeClass(), player.getGender());
        var sortedPlayers = filteredPlayers.stream()
                .sorted(Comparator.comparingInt(pointsExtractor).reversed())
                .toList();
        int rank = sortedPlayers.indexOf(player) + 1;
        log.debugf("Calculated %s ranking for player %s is %d", rankingType, player.getPlayerName(), rank);
        return rank;
    }

    private List<PlayerInfoDTO> filterByAgeClassAndGender(List<PlayerInfoDTO> players, String ageClass, String gender) {
        return players.stream()
                .filter(p -> p.getAgeClass().equalsIgnoreCase(ageClass) && p.getGender().equalsIgnoreCase(gender))
                .toList();
    }

    private void initPlayerList() {
        if (allPlayer == null) {
            allPlayer = playerServiceProvider.getAllPlayers().stream().map(PlayerInfoDTO::new).toList();
        }
    }

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
