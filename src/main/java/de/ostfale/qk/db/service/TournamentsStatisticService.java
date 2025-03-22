package de.ostfale.qk.db.service;

import de.ostfale.qk.db.api.TournamentsStatisticRepository;
import de.ostfale.qk.db.internal.match.TournamentsStatistic;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
@Transactional
public class TournamentsStatisticService {

    private static final Logger log = Logger.getLogger(TournamentsStatisticService.class);

    @Inject
    TournamentsStatisticRepository tournamentsStatisticRepository;

    public TournamentsStatistic findByPlayerId(String playerId) {
        log.debugf("Find statistic for player with playerId %s", playerId);
        return tournamentsStatisticRepository.findByPlayerIdOrEmpty(playerId);
    }

    public void save(TournamentsStatistic tournamentsStatistic) {
        log.debugf("Save statistic for player with playerId %s", tournamentsStatistic.getPlayerId());
        tournamentsStatisticRepository.persist(tournamentsStatistic);
    }
}
