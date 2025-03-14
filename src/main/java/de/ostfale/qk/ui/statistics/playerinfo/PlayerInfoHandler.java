package de.ostfale.qk.ui.statistics.playerinfo;

import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.db.service.PlayerServiceProvider;
import de.ostfale.qk.web.api.WebService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

@ApplicationScoped
public class PlayerInfoHandler {


    private static final Logger log = Logger.getLogger(PlayerInfoHandler.class);

    private static final String LOG_MESSAGE_FORMAT = "PlayerInfoHandler :: Get player ranking for AgeClass %s and Discipline %s";

    @Inject
    PlayerServiceProvider playerServiceProvider;

    @Inject
    WebService webService;

    private List<Player> allPlayers;
    private List<PlayerInfoDTO> allPlayer;

    public List<PlayerInfoDTO> findAllFavoritePlayers() {
        if (allPlayer == null) {
            allPlayer = playerServiceProvider.getAllPlayers().stream().map(PlayerInfoDTO::new).toList();
        }
        var favPlayers = allPlayer.stream().filter(PlayerInfoDTO::getFavorite).toList();
        log.debugf("PlayerInfoHandler :: Read all favorite players  %d players", favPlayers.size());
        return favPlayers;
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


    public List<PlayerInfoDTO> findPlayerByName(String playerName) {
        log.debugf("PlayerInfoHandler :: Find player by name %s", playerName);
        return allPlayers.stream().filter(player -> player.getName().equalsIgnoreCase(playerName)).map(PlayerInfoDTO::new).toList();
    }

}
