package de.ostfale.qk.db.api;

import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.parser.ranking.internal.GenderType;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class PlayerRepository implements PanacheRepository<Player> {

    private static final Logger log = Logger.getLogger(PlayerRepository.class);

    public List<Player> findByFirstnameAndLastname(String firstname, String lastname) {
        log.debugf("PlayerRepository :: findByFirstnameAndLastname(%s, %s)", firstname, lastname);
        return list("firstName = ?1 and lastName = ?2", firstname, lastname);
    }

    public List<Player> findPlayersByFullNameIgnoreCase(String fullName) {
        log.debugf("PlayerRepository :: findPlayersByFullNameIgnoreCase(%s)", fullName);
        return list("lower(fullName) = lower(?1)", fullName);
    }

    public List<Player> findFavoritePlayers() {
        log.debugf("PlayerRepository :: findFavoritePlayers()");
        return list("favorite", true);
    }

    public Long countPlayerByGender(GenderType genderType) {
        List<Player> playerList = list("gender", genderType);
        log.debugf("PlayerRepository :: Searched for all %s player and found: %d", genderType, playerList.size());
        return Long.valueOf(playerList.size());
    }

    public Player findByPlayerId(String playerId) {
        log.tracef("PlayerRepository :: findByPlayerId(%s)", playerId);
        return find("playerId = ?1", playerId).firstResult();
    }
}
