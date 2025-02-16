package de.ostfale.qk.parser.tournament.api;

import de.ostfale.qk.parser.tournament.internal.model.TournamentRawModel;
import de.ostfale.qk.parser.tournament.internal.model.TournamentYearRawModel;
import org.htmlunit.html.HtmlElement;

public interface TournamentParser {

    TournamentYearRawModel parseTournamentYear (String year, HtmlElement content);

    TournamentRawModel parseTournamentInfo (HtmlElement content);

}
