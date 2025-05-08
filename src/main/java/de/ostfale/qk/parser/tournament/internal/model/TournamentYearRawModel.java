package de.ostfale.qk.parser.tournament.internal.model;

import java.util.ArrayList;
import java.util.List;

public record TournamentYearRawModel(
        String year,
        List<TournamentRawModel> tournaments
) {
    public TournamentYearRawModel(String year) {
        this(year, new ArrayList<>());
    }

    public void addTournament(TournamentRawModel tournament) {
        tournaments.add(tournament);
    }
}
