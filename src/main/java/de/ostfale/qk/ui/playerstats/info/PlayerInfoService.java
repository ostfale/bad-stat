package de.ostfale.qk.ui.playerstats.info;

import de.ostfale.qk.app.cache.RankingPlayerCache;
import de.ostfale.qk.data.player.model.FavPlayerData;
import de.ostfale.qk.domain.player.Player;
import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.domain.player.PlayerTournamentId;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import de.ostfale.qk.ui.playerstats.info.rankingdata.PlayerDiscStatDTO;
import de.ostfale.qk.ui.playerstats.info.tournamentdata.PlayerTourStatDTO;
import de.ostfale.qk.web.async.PlayerAsyncWebService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class PlayerInfoService {

    @Inject
    RankingPlayerCache rankingPlayerCache;

    @Inject
    PlayerAsyncWebService playerAsyncWebService;

    private final Map<PlayerId, PlayerInfoDTO> playerInfoDTOMap = new ConcurrentHashMap<>();

    public PlayerInfoDTO getPlayerInfoDTO(FavPlayerData favPlayerData) {
        Log.debugf("PlayerInfoService :: Read player infos for favourite player %s", favPlayerData.playerName());
        if (playerInfoDTOMap.containsKey(favPlayerData.playerId())) {
            Log.debugf("PlayerInfoService :: found player info in cache for player id %s", favPlayerData.playerId());
            return playerInfoDTOMap.get(favPlayerData.playerId());
        }
        Player foundPlayer = rankingPlayerCache.getPlayerById(favPlayerData.playerId().playerId());
        var playerInfo = new PlayerInfoDTO(foundPlayer);
        playerInfo.setSingleDiscStat(mapSingleDisciplineStatistics(foundPlayer));
        playerInfo.setDoubleDiscStat(mapDoubleDisciplineStatistics(foundPlayer));
        playerInfo.setMixedDiscStat(mapMixedDisciplineStatistics(foundPlayer));
        playerInfo.getPlayerInfoMasterDataDTO().setPlayerTournamentId(favPlayerData.playerTournamentId().tournamentId());
        playerInfo.setPlayerTourStatDTO(new PlayerTourStatDTO(favPlayerData));
        return playerInfo;
    }

    public PlayerInfoDTO getPlayerInfoDTO(PlayerId playerIdObject) throws HtmlParserException {
        String playerId = playerIdObject.playerId();
        Log.debugf("PlayerInfoService :: get player info for player id %s", playerIdObject.playerId());
        if (playerInfoDTOMap.containsKey(playerIdObject)) {
            Log.debugf("PlayerInfoService :: found player info in cache for player id %s", playerIdObject.playerId());
            return playerInfoDTOMap.get(playerIdObject);
        }
        Player foundPlayer = rankingPlayerCache.getPlayerById(playerId);
        var playerInfo = new PlayerInfoDTO(foundPlayer);
        playerInfo.setSingleDiscStat(mapSingleDisciplineStatistics(foundPlayer));
        playerInfo.setDoubleDiscStat(mapDoubleDisciplineStatistics(foundPlayer));
        playerInfo.setMixedDiscStat(mapMixedDisciplineStatistics(foundPlayer));

        playerAsyncWebService.fetchPlayerTournamentId(playerId)
                .onFailure().invoke(throwable -> Log.errorf("Failed to get tournament id for player %s", playerId, throwable))
                .onItem().transform(PlayerTournamentId::tournamentId)
                .subscribe().with(tournamentId -> {
                    playerInfo.getPlayerInfoMasterDataDTO().setPlayerTournamentId(tournamentId);
                });

        var playerIdObj = new PlayerId(playerId);
        var playerTournamentId = new PlayerTournamentId(playerInfo.getPlayerInfoMasterDataDTO().getPlayerTournamentId());
        playerAsyncWebService.fetchPlayerTourStatInfo(playerIdObj, playerTournamentId)
                .onFailure().invoke(throwable -> Log.errorf("Failed to get tournament statistics for player %s", playerId, throwable))
                .subscribe().with(playerInfo::setPlayerTourStatDTO);

        playerInfoDTOMap.put(playerIdObject, playerInfo);
        return playerInfo;
    }

    public List<PlayerInfoDTO> getPlayerInfoList() {
        Log.debug("PlayerInfoService :: map all players from cache into PlayerInfoDTOs ");
        return rankingPlayerCache.getPlayerList().stream().map(PlayerInfoDTO::new).toList();
    }

    public PlayerInfoDTO getPlayerInfosForPlayerName(String playerName) throws HtmlParserException {
        List<Player> foundPlayers = rankingPlayerCache.getPlayerByName(playerName);
        if (foundPlayers.size() == 1) {
            return getPlayerInfoDTO(foundPlayers.getFirst().getPlayerId());
        }
        Log.errorf("Multiple players found with name: %s -> %d", playerName, foundPlayers.size());
        return null;
    }

    private PlayerDiscStatDTO mapSingleDisciplineStatistics(Player player) {
        return Optional.ofNullable(player.getSingleRankingInformation())
                .map(info -> new PlayerDiscStatDTO(
                        info.tournaments(),
                        info.rankingPoints(),
                        info.rankingPosition(),
                        rankingPlayerCache.calculatePlayerRanking(player, Player::getSinglePoints, "single")))
                .orElse(new PlayerDiscStatDTO(0, 0, 0, 0));
    }

    private PlayerDiscStatDTO mapDoubleDisciplineStatistics(Player player) {
        return Optional.ofNullable(player.getDoubleRankingInformation())
                .map(info -> new PlayerDiscStatDTO(
                        info.tournaments(),
                        info.rankingPoints(),
                        info.rankingPosition(),
                        rankingPlayerCache.calculatePlayerRanking(player, Player::getDoublePoints, "double")))
                .orElse(new PlayerDiscStatDTO(0, 0, 0, 0));
    }

    private PlayerDiscStatDTO mapMixedDisciplineStatistics(Player player) {
        return Optional.ofNullable(player.getMixedRankingInformation())
                .map(info -> new PlayerDiscStatDTO(
                        info.tournaments(),
                        info.rankingPoints(),
                        info.rankingPosition(),
                        rankingPlayerCache.calculatePlayerRanking(player, Player::getMixedPoints, "mixed")))
                .orElse(new PlayerDiscStatDTO(0, 0, 0, 0));
    }
}
