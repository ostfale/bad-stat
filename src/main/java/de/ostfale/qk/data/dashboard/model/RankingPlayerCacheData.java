package de.ostfale.qk.data.dashboard.model;

import de.ostfale.qk.domain.player.GenderType;
import de.ostfale.qk.domain.player.Player;
import org.jboss.logging.Logger;

import java.util.List;

public record RankingPlayerCacheData(List<Player> players) {

    private static final Logger log = Logger.getLogger(RankingPlayerCacheData.class);


    public List<Player> getPlayers() {
        return players;
    }

    public long getNumberOfPlayers() {
        return players.size();
    }

    public long getNumberOfFemalePlayers() {
        return players.parallelStream().filter(player -> player.getGender().equals(GenderType.FEMALE)).toList().size();
    }

    public long getNumberOfMalePlayers() {
        return players.parallelStream().filter(player -> player.getGender().equals(GenderType.MALE)).toList().size();
    }

    public List<Player> getPlayerByName(String playerName) {
        return players.parallelStream().filter(player -> player.getFullName().equals(playerName)).toList();
    }

    public Player getPlayerByPlayerId(String playerId) {
        return players.parallelStream().filter(player -> player.getPlayerId().playerId().equals(playerId)).findFirst().orElse(null);
    }

    public List<Player> filterByGenderAndAgeClass(String ageClass, String gender) {
        var result = players.parallelStream()
                .filter(player -> matchesGenderAndAgeClass(player, ageClass, gender))
                .toList();

        log.debugf("Found %d players for gender %s and age class %s", result.size(), gender, ageClass);
        return result;
    }

    private boolean matchesGenderAndAgeClass(Player player, String ageClass, String gender) {
        return player.getPlayerInfo().getAgeClassGeneral().equalsIgnoreCase(ageClass)
                && player.getGender().getDisplayName().equalsIgnoreCase(gender);
    }

}
