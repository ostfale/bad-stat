package de.ostfale.qk.db.service;

import de.ostfale.qk.db.api.PlayerRepository;
import de.ostfale.qk.db.internal.player.Player;
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

    public List<Player> findPlayersByFullName(String fullName) {
        log.infof("Find players by full name: %s", fullName);
        return playerRepository.findPlayersByFullNameIgnoreCase(fullName);
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
        log.infof("Found %d favorite players", foundFavoritePlayers.size());
        return foundFavoritePlayers;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.listAll();
    }
}
