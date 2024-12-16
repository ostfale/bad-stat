package de.ostfale.qk.parser.tournament.api;

import de.ostfale.qk.parser.tournament.internal.TournamentHeaderInfo;
import org.htmlunit.html.HtmlDivision;

public interface TournamentParser {

    TournamentHeaderInfo parseHeader (HtmlDivision content);


}
