package de.ostfale.qk.ui.statistics.playerinfo;

import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.db.service.PlayerServiceProvider;
import de.ostfale.qk.web.api.WebService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
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

    public PlayerInfoDTO prepareData(String playerName) {
        log.debug("Prepare data for player info");
        return new PlayerInfoDTO();
    }

   /* public Player calculatePlayersAgeClassRanking(Player player, List<Player> allPlayers) {
        List<Player> filteredByAgeClass = allPlayers.stream()
                .filter(p -> p.getAgeClassGeneral().equalsIgnoreCase(player.getAgeClassGeneral()))
                .toList();

        var singlePlayerList = filteredByAgeClass.stream().sorted(Comparator.comparingInt(Player::getSinglePoints).reversed()).toList();
        var doublePlayerList = filteredByAgeClass.stream().sorted(Comparator.comparingInt(Player::getDoublePoints).reversed()).toList();
        var mixedPlayerList = filteredByAgeClass.stream().sorted(Comparator.comparingInt(Player::getMixedPoints).reversed()).toList();

        Integer singleRank = singlePlayerList.indexOf(player) + 1;
        Integer doubleRank = doublePlayerList.indexOf(player) + 1;
        Integer mixedRank = mixedPlayerList.indexOf(player) + 1;

        player.setSingleRanking(singleRank);
        player.setDoubleRanking(doubleRank);
        player.setMixedRanking(mixedRank);

        return player;
    }*/


    public PlayerInfoDTO calculatePlayersAgeClassRanking(PlayerInfoDTO player) {
        List<Player> filteredPlayers = filterByAgeClass(allPlayers, player.getAgeClass());
        Player currentPlayer = allPlayers.stream().filter(p -> p.getPlayerId().equalsIgnoreCase(player.getPlayerId())).findFirst().orElse(null);

        setRankingForDiscipline(currentPlayer, filteredPlayers, Player::getSinglePoints, Player::setSingleRanking);
        setRankingForDiscipline(currentPlayer, filteredPlayers, Player::getDoublePoints, Player::setDoubleRanking);
        setRankingForDiscipline(currentPlayer, filteredPlayers, Player::getMixedPoints, Player::setMixedRanking);

        return new PlayerInfoDTO(Objects.requireNonNull(currentPlayer, "Player not found"));
    }

    private List<Player> filterByAgeClass(List<Player> players, String ageClass) {
        return players.stream()
                .filter(p -> p.getAgeClassGeneral().equalsIgnoreCase(ageClass))
                .toList();
    }

    private void setRankingForDiscipline(Player player, List<Player> filteredPlayers, ToIntFunction<Player> pointExtractor, BiConsumer<Player, Integer> rankSetter) {
        List<Player> sortedPlayers = filteredPlayers.stream()
                .sorted(Comparator.comparingInt(pointExtractor).reversed())
                .toList();
        int rank = sortedPlayers.indexOf(player) + 1;
        rankSetter.accept(player, rank);
    }


    public List<PlayerInfoDTO> findAllFavoritePlayers() {
        var favPlayers = playerServiceProvider.findFavoritePlayers();
        log.debugf("PlayerInfoHandler :: Read all favorite players  %d players", favPlayers.size());
        return favPlayers.stream().map(PlayerInfoDTO::new).toList();
    }

    public List<PlayerInfoDTO> findPlayerByName(String playerName) {
        log.debugf("PlayerInfoHandler :: Find player by name %s", playerName);
        return allPlayers.stream().filter(player -> player.getName().equalsIgnoreCase(playerName)).map(PlayerInfoDTO::new).toList();
    }

    public List<PlayerInfoDTO> findAllPlayers() {
        if (allPlayers == null) {
            allPlayers = playerServiceProvider.getAllPlayers();
        }
        log.debugf("PlayerInfoHandler :: Read all players  %d players", allPlayers.size());
        return allPlayers.stream().map(PlayerInfoDTO::new).toList();
    }
}
