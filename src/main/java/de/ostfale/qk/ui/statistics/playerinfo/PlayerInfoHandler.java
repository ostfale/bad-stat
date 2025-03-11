package de.ostfale.qk.ui.statistics.playerinfo;

import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.db.service.PlayerServiceProvider;
import de.ostfale.qk.web.api.WebService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class PlayerInfoHandler {

    private static final Logger log = Logger.getLogger(PlayerInfoHandler.class);

    @Inject
    PlayerServiceProvider playerServiceProvider;

    @Inject
    WebService webService;

    private List<Player> allPlayers;

    public PlayerInfoDTO prepareData(String playerName) {
        log.debug("Prepare data for player info");
        return new PlayerInfoDTO();
    }

    public Player findPlayerByName(String playerName) {
        log.debugf("PlayerInfoHandler :: Find player by name %s", playerName);
        return allPlayers.stream()
                .filter(player -> player.getName().equalsIgnoreCase(playerName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerName));
    }

    public List<Player> findAllFavoritePlayers() {
        var favPlayers = playerServiceProvider.findFavoritePlayers();
        log.debugf("PlayerInfoHandler :: Read all favorite players  %d players", favPlayers.size());
        return favPlayers;
    }

    public List<Player> findAllPlayers() {
        if (allPlayers == null) {
            allPlayers = playerServiceProvider.getAllPlayers();
        }
        log.debugf("PlayerInfoHandler :: Read all players  %d players", allPlayers.size());
        return allPlayers;
    }
}
