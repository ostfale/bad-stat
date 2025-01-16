package de.ostfale.qk.parser.discipline.api;

import de.ostfale.qk.parser.discipline.internal.model.DisciplineDTO;
import de.ostfale.qk.parser.tournament.internal.model.TournamentDisciplineDTO;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;

import java.util.List;

public interface DisciplineParser {

    List<DisciplineDTO> parseDisciplines (HtmlElement content);

    DisciplineDTO parseDiscipline(HtmlElement content);

    List<TournamentDisciplineDTO> parseTournamentDisciplines (HtmlElement content);

}
