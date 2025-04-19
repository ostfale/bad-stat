package de.ostfale.qk.db.service;

import de.ostfale.qk.db.api.PlayerRepository;
import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.db.internal.player.PlayerOverview;
import de.ostfale.qk.parser.ranking.internal.GenderType;
import de.ostfale.qk.ui.statistics.playerinfo.PlayerInfoDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
@Transactional
public class PlayerServiceProvider {

    private static final Logger log = Logger.getLogger(PlayerInfoDTO.class);

    @Inject
    PlayerRepository playerRepository;

    public PlayerOverview getPlayerOverview() {
        Long nofPlayer = playerRepository.count();
        Long nofMalePlayer = playerRepository.countPlayerByGender(GenderType.MALE);
        Long nofFemalePlayer = playerRepository.countPlayerByGender(GenderType.FEMALE);
        var playerOverview = new PlayerOverview(nofPlayer, nofMalePlayer, nofFemalePlayer);
        log.infof("PlayerServiceProvider :: %s", playerOverview.toString());
        return playerOverview;
    }

    public Long getNofPlayers() {
        return playerRepository.count();
    }

    public List<Player> findPlayersByFullName(String fullName) {
        log.debugf("Find players by full name: %s", fullName);
        return playerRepository.findPlayersByFullNameIgnoreCase(fullName);
    }

    public Player findPlayerById(String playerId) {
        log.tracef("Find player by id: %s", playerId);
        return playerRepository.findByPlayerId(playerId);
    }

    public void updatePlayerAsFavorite(String playerId, boolean isFavorite) {
        Player existingPlayer = playerRepository.findByPlayerId(playerId);
        if (existingPlayer != null) {
            log.infof("Updating player %s as favorite : %s", existingPlayer.getName(), isFavorite);
            existingPlayer.setFavorite(isFavorite);
            playerRepository.persist(existingPlayer);
        }
    }

    public void updatePlayerAsFavorite(Player player) {
        Player existingPlayer = playerRepository.findByPlayerId(player.getPlayerId());
        if (existingPlayer != null) {
            log.infof("Updating player %s as favorite : %S", player.getName(), player.getFavorite());
            existingPlayer.setFavorite(player.getFavorite());
            playerRepository.persist(existingPlayer);
        }
    }

    public Player updatePlayersTournamentId(Player player, String tournamentId) {
        Player existingPlayer = playerRepository.findByPlayerId(player.getPlayerId());
        if (existingPlayer != null) {
            log.debugf("Updating player %s with tournament id %s", player.getName(), tournamentId);
            existingPlayer.setPlayerTournamentId(tournamentId);
            playerRepository.persist(existingPlayer);
            return existingPlayer;
        }
        log.errorf("Player %s not found", player.getName());
        throw new IllegalArgumentException("Player not found");
    }

    public List<Player> findFavoritePlayers() {
        var foundFavoritePlayers = playerRepository.findFavoritePlayers();
        log.debugf("Found %d favorite players", foundFavoritePlayers.size());
        return foundFavoritePlayers;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.listAll();
    }

    public void save(List<Player> playerList) {
        playerRepository.persist(playerList);
        log.infof("PlayerServiceProvider :: Saved %d players", playerList.size());
    }

    public void updatePlayer(Player player) {
        Player existingPlayer = playerRepository.findByPlayerId(player.getPlayerId());
        if (existingPlayer == null) {
            log.debugf("PlayerServiceProvider :: save new player: %s",player.getName());
            playerRepository.persist(player);
        }
        else {
            log.tracef("PlayerServiceProvider :: update existing player %s ", player.getName());
            existingPlayer.updatePlayer(player);
            playerRepository.persist(existingPlayer);
        }
    }
}
