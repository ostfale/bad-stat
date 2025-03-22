package de.ostfale.qk.db.api;

import de.ostfale.qk.db.internal.match.TournamentsStatistic;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TournamentsStatisticRepository implements PanacheRepository<TournamentsStatistic> {


    public TournamentsStatistic findByPlayerIdOrEmpty(String playerId) {
        return find("playerId", playerId).firstResultOptional().orElse(new TournamentsStatistic());
    }
}
