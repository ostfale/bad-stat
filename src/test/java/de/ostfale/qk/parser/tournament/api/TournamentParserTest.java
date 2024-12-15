package de.ostfale.qk.parser.tournament.api;

import de.ostfale.qk.parser.ConfiguredWebClient;
import de.ostfale.qk.parser.tournament.internal.HtmlTournamentParser;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test reading the header information from a tournament")
@QuarkusTest
class TournamentParserTest extends BaseTest{

    private static final Logger log = LoggerFactory.getLogger(TournamentParserTest.class);

    @Inject
    HtmlTournamentParser parser;

    @Test
    void parseHeader() {
        // given
        String testFileName = "SingleTournamentMatches.txt";
        String testHtmlString = readFile(testFileName);
        WebClient webClient = ConfiguredWebClient.getWebClient();

        // when





        try {
            final HtmlPage page = webClient.loadHtmlCodeIntoCurrentWindow(testHtmlString);
            List<HtmlDivision> tournamentsList = getPlayersTournaments(page);
            var result = parser.parseHeader(tournamentsList.getFirst());
            System.out.println(result);
        } catch (IOException e) {
            log.error("Failed to parse the header", e);
            throw new RuntimeException(e);
        }
    }

    private List<HtmlDivision> getPlayersTournaments(HtmlPage page) {
        final String FIRST_MODULE_CARD = "//div[contains(@class, 'module module--card')]";
        List<HtmlDivision> moduleCards = page.getByXPath(FIRST_MODULE_CARD);
        log.info("Found {} tournaments", moduleCards.size());
        return moduleCards;
    }

}

