package de.ostfale.qk.parser.tournament;

import de.ostfale.qk.parser.tournament.model.TournamentParserModel;
import de.ostfale.qk.parser.tournament.model.TournamentYearParserModel;
import org.htmlunit.html.HtmlElement;

public interface TournamentParser {

    TournamentYearParserModel parseTournamentYear (String year, HtmlElement content);

    TournamentParserModel parseTournamentInfo (HtmlElement content);

    Integer parseNofTournaments (HtmlElement content);
}
