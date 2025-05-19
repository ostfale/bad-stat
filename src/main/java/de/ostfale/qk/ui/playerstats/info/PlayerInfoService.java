package de.ostfale.qk.ui.playerstats.info;

import de.ostfale.qk.data.dashboard.RankingPlayerCacheHandler;
import de.ostfale.qk.domain.player.Player;
import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.ui.dashboard.DashboardService;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import de.ostfale.qk.ui.playerstats.info.rankingdata.PlayerDiscStatDTO;
import de.ostfale.qk.ui.playerstats.info.tournamentdata.PlayerTourStatDTO;
import de.ostfale.qk.ui.playerstats.matches.PlayerInfoMatchStatService;
import de.ostfale.qk.web.player.PlayerTournamentId;
import de.ostfale.qk.web.player.PlayerWebParserService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.ToIntFunction;

@ApplicationScoped
public class PlayerInfoService {

    private static final Logger log = Logger.getLogger(DashboardService.class);

    @Inject
    RankingPlayerCacheHandler rankingPlayerCacheHandler;

    @Inject
    PlayerInfoMatchStatService playerInfoMatchStatService;

    @Inject
    PlayerWebParserService playerWebParserService;

    private final Map<PlayerId, PlayerInfoDTO> playerInfoDTOMap = new ConcurrentHashMap<>();


    public PlayerInfoDTO getPlayerInfoDTO(PlayerId playerIdObject) {
        String playerId = playerIdObject.playerId();
        log.debugf("PlayerInfoService :: get player info for player id %s", playerId);
        if (playerInfoDTOMap.containsKey(playerIdObject)) {
            return playerInfoDTOMap.get(playerIdObject);
        }
        Player foundPlayer = rankingPlayerCacheHandler.getRankingPlayerCache().getPlayerByPlayerId(playerId);
        var playerInfo = new PlayerInfoDTO(foundPlayer);
        playerInfo.setSingleDiscStat(mapSingleDisciplineStatistics(foundPlayer));
        playerInfo.setDoubleDiscStat(mapDoubleDisciplineStatistics(foundPlayer));
        playerInfo.setMixedDiscStat(mapMixedDisciplineStatistics(foundPlayer));
        playerInfoDTOMap.put(playerIdObject, playerInfo);
        return playerInfo;
    }

    // read tournamentId and use it to get the tournament statistics -> async request
    public Uni<Map<PlayerTournamentId, PlayerTourStatDTO>> getPlayerTourStatsAsync(String playerId) {
        return Uni.createFrom()
                .item(() -> playerWebParserService.getPlayerTournamentId(playerId))
                .onItem().transform(ptId -> playerInfoMatchStatService.readYearlyTournamentStatistics(playerId, ptId));
    }

    public List<PlayerInfoDTO> getPlayerInfoList() {
        log.debug("PlayerInfoService :: map all players from cache into PlayerInfoDTOs ");
        var rankingPlayerCache = rankingPlayerCacheHandler.getRankingPlayerCache();
        if (rankingPlayerCache != null) {
            return rankingPlayerCache.players().stream().map(PlayerInfoDTO::new).toList();
        }
        return List.of();
    }

    public PlayerInfoDTO getPlayerInfosForPlayerName(String playerName) {
        List<Player> foundPlayers = rankingPlayerCacheHandler.getRankingPlayerCache().getPlayerByName(playerName);
        if (foundPlayers.size() == 1) {
            return getPlayerInfoDTO(foundPlayers.getFirst().getPlayerId());
        }
        log.errorf("Multiple players found with name: %s -> %d", playerName, foundPlayers.size());
        return null;
    }

    private PlayerDiscStatDTO mapSingleDisciplineStatistics(Player player) {
        return Optional.ofNullable(player.getSingleRankingInformation())
                .map(info -> new PlayerDiscStatDTO(
                        info.tournaments(),
                        info.rankingPoints(),
                        info.rankingPosition(),
                        calculatePlayersRanking(player, Player::getSinglePoints, "single")))
                .orElse(new PlayerDiscStatDTO(0, 0, 0, 0));
    }

    private PlayerDiscStatDTO mapDoubleDisciplineStatistics(Player player) {
        return Optional.ofNullable(player.getDoubleRankingInformation())
                .map(info -> new PlayerDiscStatDTO(
                        info.tournaments(),
                        info.rankingPoints(),
                        info.rankingPosition(),
                        calculatePlayersRanking(player, Player::getDoublePoints, "double")))
                .orElse(new PlayerDiscStatDTO(0, 0, 0, 0));
    }

    private PlayerDiscStatDTO mapMixedDisciplineStatistics(Player player) {
        return Optional.ofNullable(player.getMixedRankingInformation())
                .map(info -> new PlayerDiscStatDTO(
                        info.tournaments(),
                        info.rankingPoints(),
                        info.rankingPosition(),
                        calculatePlayersRanking(player, Player::getMixedPoints, "mixed")))
                .orElse(new PlayerDiscStatDTO(0, 0, 0, 0));
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
