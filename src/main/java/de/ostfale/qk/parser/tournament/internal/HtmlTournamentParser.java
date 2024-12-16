package de.ostfale.qk.parser.tournament.internal;

import de.ostfale.qk.parser.tournament.api.TournamentParser;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class HtmlTournamentParser implements TournamentParser {

    private static final Logger log = LoggerFactory.getLogger(HtmlTournamentParser.class);

    final String TOURNAMENT_NAME = ".//h4[contains(@class, 'media__title media__title--medium')]";
    final String TOURNAMENT_ORGANISATION = ".//small[contains(@class, 'media__subheading')]";
    final String TOURNAMENT_DATE = ".//small[contains(@class, 'media__subheading media__subheading--muted')]";
    final String TOURNAMENT_ID = "//a[contains(@class, 'media__img')]";

    @Override
    public TournamentHeaderInfo parseHeader(HtmlDivision content) {
        log.info("Parsing tournament header ");

        HtmlElement tournamentNameElement = content.getFirstByXPath(TOURNAMENT_NAME);
        HtmlElement tournamentOrgElement = content.getFirstByXPath(TOURNAMENT_ORGANISATION);
        HtmlElement tournamentDateElement = content.getFirstByXPath(TOURNAMENT_DATE);
        List<HtmlElement>tournamentIdElements = content.getByXPath(TOURNAMENT_ID);

        var tournamentIdArray = tournamentIdElements.getFirst().getAttribute("href").split("=");
        var orgaAndLocation = tournamentOrgElement.asNormalizedText().split("\\|");

        var tournamentName = tournamentNameElement.asNormalizedText();
        var tournamentOrganisation = orgaAndLocation[0].trim();
        var tournamentLocation = orgaAndLocation[1].trim();
        var tournamentId = tournamentIdArray[ tournamentIdArray.length - 1];
        var tournamentDate = tournamentDateElement.asNormalizedText();

        return new TournamentHeaderInfo(tournamentId,tournamentName, tournamentOrganisation, tournamentLocation,  tournamentDate);
    }
}
