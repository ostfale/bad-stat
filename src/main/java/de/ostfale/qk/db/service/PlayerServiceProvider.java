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

    public boolean savePlayerIfNotExistsOrHasChanged(Player player) {
        log.tracef("Save Player :: Check player %s to be saved", player.getName());
        Player searchedPlayer = playerRepository.findByPlayerId(player.getPlayerId());
        if (searchedPlayer != null) {
            log.trace("Save Player :: Player has been found!");
            if (player.equals(searchedPlayer)) {
                log.tracef("Save Player :: Found player and given player are identical -> not saved");
                return false;
            }
            log.tracef("Save Player :: Searched player and given player are different -> player updated");
            searchedPlayer.updatePlayer(player);
            playerRepository.persist(searchedPlayer);
            return true;
        }
        log.tracef("Save Player :: Player not found -> will be saved!");
        playerRepository.persist(player);
        return true;
    }

    public List<Player> findPlayersByFullName(String fullName) {
        log.debugf("Find players by full name: %s", fullName);
        return playerRepository.findPlayersByFullNameIgnoreCase(fullName);
    }

    public Player findPlayerById(String playerId) {
        log.debugf("Find player by id: %s", playerId);
        return playerRepository.findByPlayerId(playerId);
    }

    public void updatePlayerAsFavorite(String playerId, boolean isFavorite){
        Player existingPlayer = playerRepository.findByPlayerId(playerId);
        if (existingPlayer != null){
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
}
