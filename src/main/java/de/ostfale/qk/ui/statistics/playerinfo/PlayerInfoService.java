package de.ostfale.qk.ui.statistics.playerinfo;

import de.ostfale.qk.db.player.FavoritePlayerData;
import de.ostfale.qk.db.player.FavoritePlayerDataJsonHandler;
import de.ostfale.qk.db.player.FavoritePlayerListHandler;
import de.ostfale.qk.domain.player.Player;
import de.ostfale.qk.persistence.ranking.RankingPlayerCacheHandler;
import de.ostfale.qk.ui.dashboard.DashboardService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@ApplicationScoped
public class PlayerInfoService {

    private static final Logger log = Logger.getLogger(DashboardService.class);

    private static final String PATH_SEPARATOR = "/";

    @Inject
    RankingPlayerCacheHandler rankingPlayerCacheHandler;

    @Inject
    FavoritePlayerDataJsonHandler favoritePlayerDataJsonHandler;

    public List<PlayerInfoDTO> getPlayerInfoList() {
        log.debug("PlayerInfoService :: map all players from cache into PlayerInfoDTOs ");
        var rankingPlayerCache = rankingPlayerCacheHandler.getRankingPlayerCache();
        if (rankingPlayerCache != null) {
            return rankingPlayerCache.players().stream().map(PlayerInfoDTO::new).toList();
        }
        return List.of();
    }

    public String extractPlayerTournamentIdFromUrl(String url) {
        log.debugf("PlayerInfoService :: extract tournament id from url: %s", url);
        if (url == null || !url.contains(PATH_SEPARATOR)) {
            return "";
        }
        int lastSeparatorIndex = url.lastIndexOf(PATH_SEPARATOR);
        return url.substring(lastSeparatorIndex + 1);
    }


    public PlayerInfoDTO getPlayerInfosForPlayer(String playerName) {
        List<Player> foundPlayers = rankingPlayerCacheHandler.getRankingPlayerCache().getPlayerByName(playerName);
        if (foundPlayers.size() == 1) {
            var player = foundPlayers.getFirst();

            var playerInfo = new PlayerInfoDTO(player);
            playerInfo.setSingleDisciplineStatistics(mapSingleDisciplineStatistics(player));
            playerInfo.setDoubleDisciplineStatistics(mapDoubleDisciplineStatistics(player));
            playerInfo.setMixedDisciplineStatistics(mapMixedDisciplineStatistics(player));

            // set tournament ID for favorite player if available
            List<FavoritePlayerData> favoritePlayers = getFavoritePlayerData(playerName);
            if (!favoritePlayers.isEmpty()) {
                playerInfo.setPlayerTournamentId(favoritePlayers.getFirst().getPlayerTournamentId());
            }

            return playerInfo;
        }

        // TODO offer selection of player
        if (foundPlayers.size() > 1) {
            Log.warnf("Multiple players found with name: %s -> %d", playerName, foundPlayers.size());
        }
        if (foundPlayers.isEmpty()) {
            Log.debugf("No player found with name: %s", playerName);
        }
        return null;
    }

    public List<PlayerInfoDTO> getAllFavoritePlayers() {
        FavoritePlayerListHandler favoritePlayersList = favoritePlayerDataJsonHandler.readFavoritePlayersList();
        List<FavoritePlayerData> favoritePlayers = favoritePlayersList.favoritePlayersList();

        log.debugf("PlayerInfoService :: Processing favorite players list with %d entries", favoritePlayers.size());

        return favoritePlayers.stream()
                .map(FavoritePlayerData::getName)
                .map(this::getPlayerInfosForPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void addPlayerToFavoriteList(String playerName) {
        log.debugf("Adding player to favorite list: %s", playerName);
        FavoritePlayerListHandler favoritePlayerList = favoritePlayerDataJsonHandler.readFavoritePlayersList();

        if (favoritePlayerList.doesPlayerExist(playerName)) {
            log.infof("Player already in favorite list: %s -> no action", playerName);
            return;
        }

        FavoritePlayerData newPlayer = createFavoritePlayerData(playerName);
        favoritePlayerList.addPlayerCustomData(newPlayer);
        favoritePlayerDataJsonHandler.savePlayerCustomDataList(favoritePlayerList);
    }

    private FavoritePlayerData createFavoritePlayerData(String playerName) {
        var playerInfos = getPlayerInfosForPlayer(playerName);
        FavoritePlayerData newPlayer = new FavoritePlayerData();
        newPlayer.setName(playerName);
        newPlayer.setPlayerId(playerInfos.getPlayerId());
        return newPlayer;
    }


    public void removePlayerFromFavoriteList(String playerName) {
        log.debugf("PlayerInfoService :: Removing player from favorite list: %s", playerName);
        FavoritePlayerListHandler listHandler = favoritePlayerDataJsonHandler.readFavoritePlayersList();
        if (!listHandler.doesPlayerExist(playerName)) {
            log.infof("Player not in favorite list: %s -> no action", playerName);
            return;
        }
        listHandler.favoritePlayersList().removeIf(player -> player.getName().equalsIgnoreCase(playerName));
        favoritePlayerDataJsonHandler.savePlayerCustomDataList(listHandler);
        log.infof("Removed player from favorite list: %s", playerName);
    }

    public void updatePlayerTournamentId(PlayerInfoDTO selectedFavPlayerInfo) {
        String playerName = selectedFavPlayerInfo.getPlayerName();
        String playerTournamentId = selectedFavPlayerInfo.getPlayerTournamentId();

        log.infof("Updating tournament ID '%s' for player '%s'", playerTournamentId, playerName);

        FavoritePlayerListHandler listHandler = favoritePlayerDataJsonHandler.readFavoritePlayersList();

        if (!listHandler.doesPlayerExist(playerName)) {
            log.warnf("Player not in favorite list: %s -> no action", playerName);
            return;
        }

        updatePlayerInList(listHandler, playerName, playerTournamentId);
        favoritePlayerDataJsonHandler.savePlayerCustomDataList(listHandler);
    }

    private List<FavoritePlayerData> getFavoritePlayerData(String playerName) {
        log.debugf("PlayerInfoService :: get favorite player data for player %s", playerName);
        FavoritePlayerListHandler listHandler = favoritePlayerDataJsonHandler.readFavoritePlayersList();
        return listHandler.favoritePlayersList().stream().filter(player -> player.getName().equalsIgnoreCase(playerName)).toList();
    }

    private void updatePlayerInList(FavoritePlayerListHandler listHandler, String playerName, String playerTournamentId) {
        listHandler.favoritePlayersList().stream()
                .filter(player -> player.getName().equalsIgnoreCase(playerName))
                .findFirst()
                .ifPresent(player -> player.setPlayerTournamentId(playerTournamentId));
    }


    private DisciplineStatisticsDTO mapSingleDisciplineStatistics(Player player) {
        return Optional.ofNullable(player.getSingleRankingInformation())
                .map(info -> new DisciplineStatisticsDTO(
                        info.tournaments(),
                        info.rankingPoints(),
                        info.rankingPosition(),
                        calculatePlayersRanking(player, Player::getSinglePoints, "single")))
                .orElse(new DisciplineStatisticsDTO(0, 0, 0, 0));
    }

    private DisciplineStatisticsDTO mapDoubleDisciplineStatistics(Player player) {
        return Optional.ofNullable(player.getDoubleRankingInformation())
                .map(info -> new DisciplineStatisticsDTO(
                        info.tournaments(),
                        info.rankingPoints(),
                        info.rankingPosition(),
                        calculatePlayersRanking(player, Player::getDoublePoints, "double")))
                .orElse(new DisciplineStatisticsDTO(0, 0, 0, 0));
    }

    private DisciplineStatisticsDTO mapMixedDisciplineStatistics(Player player) {
        return Optional.ofNullable(player.getMixedRankingInformation())
                .map(info -> new DisciplineStatisticsDTO(
                        info.tournaments(),
                        info.rankingPoints(),
                        info.rankingPosition(),
                        calculatePlayersRanking(player, Player::getMixedPoints, "mixed")))
                .orElse(new DisciplineStatisticsDTO(0, 0, 0, 0));
    }

    private Integer calculatePlayersRanking(Player player, ToIntFunction<Player> pointsExtractor, String rankingType) {
        log.debugf("PlayerInfoService :: calculate ranking for player %s", player.getFullName());
        List<Player> filteredPlayers = rankingPlayerCacheHandler.getRankingPlayerCache()
                .filterByGenderAndAgeClass(player.getPlayerInfo().getAgeClassGeneral(), player.getGender().getDisplayName());
        var sortedPlayers = filteredPlayers.stream()
                .sorted(Comparator.comparingInt(pointsExtractor).reversed())
                .toList();
        int rank = sortedPlayers.indexOf(player) + 1;
        log.debugf("Calculated %s ranking for player %s is %d", rankingType, player.getFullName(), rank);
        return rank;
    }
}
