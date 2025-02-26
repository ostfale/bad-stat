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
        String[] nameParts = splitPlayerName(player);
        var firstName = nameParts[0];
        var lastName = nameParts[1];
        log.infof("Get player info statistics for player: firstName %s - lastName: %s", firstName, lastName);
        List<Player> foundPlayers = playerRepository.findByFirstnameAndLastname(firstName, lastName);

        var playerInfoStatisticsDTO = new PlayerInfoStatisticsDTO();
        if (foundPlayers.size() == 1) {
            var foundPlayer = foundPlayers.getFirst();
            playerInfoStatisticsDTO.setPlayerId(foundPlayer.getPlayerId());
            playerInfoStatisticsDTO.setPlayerName(player);
            playerInfoStatisticsDTO.setBirthYear(foundPlayer.getYearOfBirth().toString());
            playerInfoStatisticsDTO.setAgeClass(foundPlayer.getAgeClassDetail());
        }

        return playerInfoStatisticsDTO;
    }

    private String[] splitPlayerName(String playerName) {
        // Check if the input is null or blank
        if (playerName == null || playerName.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        String[] nameParts = playerName.trim().split("\\s+"); // Split into at most two parts

        if (nameParts.length < 2) {
            throw new IllegalArgumentException("Full name must include both first and last name");
        }

        String firstName = nameParts[0];
        String lastName = nameParts[1];

        if (nameParts.length == 3) {
            firstName = nameParts[0] + " " + nameParts[1];
            lastName = nameParts[2];
        }

        return new String[]{firstName, lastName};
    }
}
