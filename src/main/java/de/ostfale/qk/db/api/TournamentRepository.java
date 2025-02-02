package de.ostfale.qk.db.api;

import de.ostfale.qk.db.internal.Tournament;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TournamentRepository implements PanacheRepository<Tournament> {

    public Tournament findByTournamentId(String tournamentId) {
        return find("tournamentID", tournamentId).firstResult();
    }
}
