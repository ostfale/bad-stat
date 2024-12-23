package de.ostfale.qk.parser.tournament.api;

import de.ostfale.qk.parser.tournament.internal.TournamentDisciplineInfoDTO;
import de.ostfale.qk.parser.tournament.internal.TournamentHeaderInfoDTO;
import org.htmlunit.html.HtmlDivision;

import java.util.List;

public interface TournamentParser {

    TournamentHeaderInfoDTO parseHeader (HtmlDivision content);

    List<TournamentDisciplineInfoDTO> parseDisciplines (HtmlDivision content);
}
