package de.ostfale.qk.domain.discipline;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.ostfale.qk.domain.match.DisciplineMatch;

import java.util.List;

@JsonDeserialize(as = TournamentDiscipline.class)
public interface Discipline {

    DisciplineType getDisciplineType();

    boolean hasMatches();

    boolean hasEliminationMatches();

    boolean hasGroupMatches();

   DisciplineOrder getDisciplineOrder();

   List<DisciplineMatch> getEliminationMatches();

   List<DisciplineMatch> getGroupMatches();
}
