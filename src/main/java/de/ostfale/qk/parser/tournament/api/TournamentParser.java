package de.ostfale.qk.parser.tournament.api;

import de.ostfale.qk.parser.tournament.internal.model.TournamentDisciplineInfoDTO;
import de.ostfale.qk.parser.tournament.internal.model.TournamentHeaderInfoDTO;
import org.htmlunit.html.HtmlDivision;

import java.util.List;

public interface TournamentParser {

    TournamentHeaderInfoDTO parseHeader (HtmlDivision content);

    List<TournamentDisciplineInfoDTO> parseDisciplines (HtmlDivision content);
}
