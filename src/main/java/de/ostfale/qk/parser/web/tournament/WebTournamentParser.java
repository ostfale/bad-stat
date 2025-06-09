package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.domain.tournament.TournamentInfo;
import org.htmlunit.html.HtmlElement;

public interface WebTournamentParser {

    TournamentInfo parseTournamentInfo(HtmlElement content);
}
