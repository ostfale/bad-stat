package de.ostfale.qk.parser.tournament.internal.model;

import java.util.ArrayList;
import java.util.List;

public record TournamentYearDTO(
        String year,
        List<TournamentDTO> tournaments
) {
    public TournamentYearDTO(String year) {
        this(year, new ArrayList<>());
    }

    public void addTournament(TournamentDTO tournament) {
        tournaments.add(tournament);
    }

    @Override
    public String toString() {
        return "Tournaments for year " + year + ":\n\n" + tournaments + "\n\n";
    }
}
