package de.ostfale.qk.ui.statistics.playerinfo;

import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.db.service.PlayerServiceProvider;
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

    public void updatePlayerTournamentId(PlayerInfoDTO playerDTO, String playerTournamentId) {
        Objects.requireNonNull(playerDTO, "Player name must not be null");
        log.debugf("PlayerInfoHandler :: Updating player %s with tournament id %s", playerDTO.getPlayerName(), playerDTO.getPlayerTournamentId());
        playerDTO.setPlayerTournamentId(playerTournamentId);
        Player player = playerServiceProvider.findPlayerById(playerDTO.getPlayerId());
        playerServiceProvider.updatePlayersTournamentId(player, playerDTO.getPlayerTournamentId());
    }

    public void toggleAndSavePlayerAsFavorite(PlayerInfoDTO playerDTO) {
        Objects.requireNonNull(playerDTO, "Player name must not be null");
        if (playerDTO.getFavorite()) {
            playerDTO.setFavorite(false);
            log.infof("PlayerInfoHandler :: Player %s is not a favorite anymore", playerDTO.getPlayerName());
        } else {
            playerDTO.setFavorite(true);
            log.infof("PlayerInfoHandler :: Player %s is now a favorite", playerDTO.getPlayerName());
        }
        Player player = playerServiceProvider.findPlayerById(playerDTO.getPlayerId());
        playerServiceProvider.updatePlayerAsFavorite(player);
    }

    public List<PlayerInfoDTO> findPlayerByName(String playerName) {
        log.debugf("PlayerInfoHandler :: Find player by name %s", playerName);
        return allPlayer.stream().filter(player -> player.getPlayerName().equalsIgnoreCase(playerName)).toList();
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
}
