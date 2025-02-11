package de.ostfale.qk.db.api;

import de.ostfale.qk.db.internal.Tournament;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TournamentRepository implements PanacheRepository<Tournament> {

    public Tournament findByTournamentId(String tournamentId) {
        return find("tournamentID", tournamentId).firstResult();
    }

    public List<Tournament> findByTournamentYear(int tournamentYear) {
        return find("tournamentYear", tournamentYear).list();
    }
}
