package de.ostfale.qk.db.api;

import de.ostfale.qk.db.internal.player.Player;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PlayerRepository implements PanacheRepository<Player> {

    public List<Player> findByFirstnameAndLastname(String firstname, String lastname) {
        return list("firstName = ?1 and lastName = ?2", firstname, lastname);
    }

    public Player findByPlayerId(String playerId) {
        return find("playerId = ?1", playerId).firstResult();
    }
}
