package de.ostfale.qk.domain.discipline;

import de.ostfale.qk.domain.match.Match;

import java.util.List;

public interface Discipline {

    DisciplineType getDisciplineType();

    boolean hasMatches();

    boolean hasEliminationMatches();

    boolean hasGroupMatches();

   DisciplineOrder getDisciplineOrder();

   List<Match> getEliminationMatches();

   List<Match> getGroupMatches();
}
