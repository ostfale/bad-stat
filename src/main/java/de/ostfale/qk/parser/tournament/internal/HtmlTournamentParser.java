package de.ostfale.qk.parser.tournament.internal;

import de.ostfale.qk.parser.tournament.api.TournamentParser;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HtmlTournamentParser implements TournamentParser {

    private static final Logger log = LoggerFactory.getLogger(HtmlTournamentParser.class);

    final String TOURNAMENT_NAME = ".//h4[contains(@class, 'media__title media__title--medium')]";
    final String TOURNAMENT_ORGANISATION = ".//small[contains(@class, 'media__subheading')]";
    final String TOURNAMENT_DATE = ".//small[contains(@class, 'media__subheading media__subheading--muted')]";

    @Override
    public TournamentHeaderInfo parseHeader(HtmlDivision content) {
        log.info("Parsing tournament header ");

        HtmlElement tournamentNameElement = content.getFirstByXPath(TOURNAMENT_NAME);
        HtmlElement tournamentOrgElement = content.getFirstByXPath(TOURNAMENT_ORGANISATION);
        HtmlElement tournamentDateElement = content.getFirstByXPath(TOURNAMENT_DATE);

        var orgaAndLocation = tournamentOrgElement.asNormalizedText().split("\\|");

        var tournamentName = tournamentNameElement.asNormalizedText();
        var tournamentOrganisation = orgaAndLocation[0];
        var tournamentLocation = orgaAndLocation[1];
        var tournamentId = "?";
        var tournamentDate = tournamentDateElement.asNormalizedText();

        return new TournamentHeaderInfo(tournamentName, tournamentOrganisation, tournamentLocation, tournamentId, tournamentDate);
    }
}
