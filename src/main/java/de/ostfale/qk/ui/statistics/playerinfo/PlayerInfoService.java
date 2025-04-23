package de.ostfale.qk.ui.statistics.playerinfo;

import de.ostfale.qk.domain.player.Player;
import de.ostfale.qk.persistence.ranking.RankingPlayerCacheHandler;
import de.ostfale.qk.ui.dashboard.DashboardService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

@ApplicationScoped
public class PlayerInfoService {

    private static final Logger log = Logger.getLogger(DashboardService.class);

    private static final String PATH_SEPARATOR = "/";

    @Inject
    RankingPlayerCacheHandler rankingPlayerCacheHandler;

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

    public PlayerInfoDTO getPlayerByName(String playerName) {
        List<Player> foundPlayers = rankingPlayerCacheHandler.getRankingPlayerCache().getPlayerByName(playerName);
        if (foundPlayers.size() == 1) {
            var player = foundPlayers.getFirst();
            return new PlayerInfoDTO(player);
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

    public Integer getSingleRankingForAgeClass(Player player) {
        return calculatePlayersRanking(player, Player::getSinglePoints, "single");
    }

    public Integer getDoubleRankingForAgeClass(Player player) {
        return calculatePlayersRanking(player, Player::getDoublePoints, "double");
    }

    public Integer getMixedRankingForAgeClass(Player player) {
        return calculatePlayersRanking(player, Player::getMixedPoints, "mixed");
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
