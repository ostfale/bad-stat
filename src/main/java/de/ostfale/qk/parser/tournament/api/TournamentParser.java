package de.ostfale.qk.parser.tournament.api;

import de.ostfale.qk.parser.tournament.internal.model.TournamentDisciplineDTO;
import de.ostfale.qk.parser.tournament.internal.model.TournamentInfoDTO;
import de.ostfale.qk.parser.tournament.internal.model.TournamentYearDTO;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;

import java.util.List;

public interface TournamentParser {

    TournamentYearDTO parseTournamentYear (String year, HtmlElement content);

    TournamentInfoDTO parseTournamentInfo (HtmlElement content);




    List<TournamentDisciplineDTO> parseDisciplines (HtmlDivision content);
}
