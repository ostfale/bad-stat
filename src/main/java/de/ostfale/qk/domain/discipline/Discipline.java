package de.ostfale.qk.domain.discipline;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.ostfale.qk.domain.match.DisciplineMatch;

import java.util.List;

@JsonDeserialize(as = TournamentDiscipline.class)
public interface Discipline {

    DisciplineInfo getDisciplineInfo();

    DisciplineType getDisciplineType();

    String getGroupName();


//  check

    boolean hasEliminationMatches();

    boolean hasGroupMatches();

   List<DisciplineMatch> getEliminationMatches();

   List<DisciplineMatch> getGroupMatches();
}
