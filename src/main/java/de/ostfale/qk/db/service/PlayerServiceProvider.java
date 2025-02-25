package de.ostfale.qk.db.service;

import de.ostfale.qk.db.api.PlayerRepository;
import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.ui.statistics.model.PlayerInfoStatisticsDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class PlayerServiceProvider {

    private static final Logger log = Logger.getLogger(PlayerInfoStatisticsDTO.class);

    @Inject
    PlayerRepository playerRepository;

    @Transactional
    public PlayerInfoStatisticsDTO getPlayerInfoStatisticsDTO(String player) {
        log.infof("Get player info statistics for player %s", player);
        List<Player> foundPlayers = playerRepository.findByFirstnameAndLastname("Louis", "Sauerbrei");

        var playerInfoStatisticsDTO = new PlayerInfoStatisticsDTO();
        if (foundPlayers.size() == 1) {
            playerInfoStatisticsDTO.setPlayerId(foundPlayers.getFirst().getPlayerId());
            playerInfoStatisticsDTO.setPlayerName(player);
        }

        return playerInfoStatisticsDTO;
    }

}
