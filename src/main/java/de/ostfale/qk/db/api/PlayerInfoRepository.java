package de.ostfale.qk.db.api;

import de.ostfale.qk.db.internal.player.PlayerInfo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlayerInfoRepository implements PanacheRepository<PlayerInfo> {
}
