package de.ostfale.qk.domain.discipline;

public interface Discipline {

    DisciplineType getDisciplineType();

    boolean hasMatches();

    boolean hasTreeMatches();

    boolean hasGroupMatches();

   DisciplineOrder getDisciplineOrder();
}
