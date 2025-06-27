package de.ostfale.qk.domain.discipline;

public interface Discipline {

    DisciplineType getDisciplineType();

    boolean hasMatches();

    boolean hasEliminationMatches();

    boolean hasGroupMatches();

   DisciplineOrder getDisciplineOrder();
}
