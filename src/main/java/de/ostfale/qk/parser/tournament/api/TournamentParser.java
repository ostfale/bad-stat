package de.ostfale.qk.parser.tournament.api;

import de.ostfale.qk.parser.tournament.internal.model.TournamentDisciplineDTO;
import de.ostfale.qk.parser.tournament.internal.model.TournamentInfoDTO;
import org.htmlunit.html.HtmlDivision;

import java.util.List;

public interface TournamentParser {

    TournamentInfoDTO parseHeader (HtmlDivision content);

    List<TournamentDisciplineDTO> parseDisciplines (HtmlDivision content);
}
