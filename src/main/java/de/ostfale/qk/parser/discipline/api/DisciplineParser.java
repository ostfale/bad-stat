package de.ostfale.qk.parser.discipline.api;

import de.ostfale.qk.parser.discipline.internal.model.DisciplineDTO;
import de.ostfale.qk.parser.tournament.internal.model.TournamentDisciplineDTO;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;

import java.util.List;

public interface DisciplineParser {

    DisciplineDTO parseDiscipline(HtmlDivision content);

    List<TournamentDisciplineDTO> parseTournamentDisciplines (HtmlElement content);

}
