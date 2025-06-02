package de.ostfale.qk.app.cache;

import de.ostfale.qk.data.dashboard.RankingPlayerCacheLoader;
import de.ostfale.qk.domain.player.GenderType;
import de.ostfale.qk.domain.player.Player;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@Singleton
public class RankingPlayerCache {

    private final List<Player> playerList = new ArrayList<>();

    @Inject
    RankingPlayerCacheLoader rankingPlayerCacheLoader;

    @Startup
    public void loadPlayerIntoCache() {
        playerList.clear();
        playerList.addAll(rankingPlayerCacheLoader.loadRankingPlayerCache());
        Log.infof("RankingPlayerCache :: Loaded %d players into cache", playerList.size());
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public long getNumberOfPlayers() {
        return playerList.size();
    }

    public long getNumberOfFemalePlayers() {
        return playerList.stream().filter(player -> player.getGender().equals(GenderType.FEMALE)).count();
    }

    public long getNumberOfMalePlayers() {
        return playerList.stream().filter(player -> player.getGender().equals(GenderType.MALE)).count();
    }

    public Player getPlayerById(String playerId) {
        return playerList.parallelStream().filter(player -> player.getPlayerId().playerId().equals(playerId)).findFirst().orElse(null);
    }

    public List<Player> getPlayerByName(String playerName) {
        return playerList.parallelStream().filter(player -> player.getFullName().equals(playerName)).toList();
    }

    public Set<Player> filterByGenderAndAgeClass(String ageClass, String gender) {
        var result = playerList.parallelStream()
                .filter(player -> matchesGenderAndAgeClass(player, ageClass, gender))
                .collect(Collectors.toSet());
        ;

        Log.debugf("RankingPlayerCache :: Found %d players for gender %s and age class %s", result.size(), gender, ageClass);
        return result;
    }

    public Integer calculatePlayerRanking(Player player, ToIntFunction<Player> pointsExtractor, String rankingType) {
        Log.debugf("PlayerInfoService :: calculate ranking for player %s", player.getFullName());
        var ageClassGeneral = player.getPlayerInfo().getAgeClassGeneral();
        var gender = player.getGender().getDisplayName();
        var sortedPlayers = filterByGenderAndAgeClass(ageClassGeneral, gender)
                .stream()
                .sorted(Comparator.comparingInt(pointsExtractor).reversed())
                .toList();
        int rank = sortedPlayers.indexOf(player) + 1;
        Log.debugf("Calculated %s ranking for player %s is %d", rankingType, player.getFullName(), rank);
        return rank;
    }

    private boolean matchesGenderAndAgeClass(Player player, String ageClass, String gender) {
        return player.getPlayerInfo().getAgeClassGeneral().equalsIgnoreCase(ageClass)
                && player.getGender().getDisplayName().equalsIgnoreCase(gender);
    }
}
