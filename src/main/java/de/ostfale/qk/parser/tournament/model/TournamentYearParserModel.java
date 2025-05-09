package de.ostfale.qk.parser.tournament.model;

import java.util.ArrayList;
import java.util.List;

public record TournamentYearParserModel(
        String year,
        List<TournamentParserModel> tournaments
) {
    public TournamentYearParserModel(String year) {
        this(year, new ArrayList<>());
    }

    public void addTournament(TournamentParserModel tournament) {
        tournaments.add(tournament);
    }
}
