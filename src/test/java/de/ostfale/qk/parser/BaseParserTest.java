package de.ostfale.qk.parser;

import de.ostfale.qk.parser.web.discipline.WebDisciplineInfoParserService;
import de.ostfale.qk.parser.web.discipline.WebDisciplineParserService;
import de.ostfale.qk.parser.web.match.WebMatchParserService;
import de.ostfale.qk.parser.web.player.MatchPlayerParserService;
import de.ostfale.qk.parser.web.set.MatchSetParserService;
import de.ostfale.qk.parser.web.tournament.WebTournamentInfoParser;
import de.ostfale.qk.parser.web.tournament.WebTournamentParserService;
import de.ostfale.qk.web.common.ConfiguredWebClient;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

public abstract class BaseParserTest {

    protected final WebClient webClient = ConfiguredWebClient.getWebClient();

    protected HtmlPage content;

    protected HtmlPage loadHtmlPage(String fileName) {
        try {
            var htmlString = readFileToString(fileName);
            return webClient.loadHtmlCodeIntoCurrentWindow(htmlString);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String readFileToString(String fileName) throws IOException, URISyntaxException {
        var file = readFile(fileName);
        return Files.readString(file.toPath());
    }

    protected File readFile(String fileName) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = Objects.requireNonNull(classLoader.getResource(fileName), "file not found! " + fileName);
        return new File(resource.toURI());
    }

    protected WebTournamentInfoParser prepareWebTournamentInfoParser() {
        return new WebTournamentInfoParser();
    }

    protected WebTournamentParserService prepareWebTournamentParser() {

        WebDisciplineParserService webDisciplineParserService = prepareWebDisciplineParser() ;
        WebTournamentInfoParser webTournamentInfoParser = prepareWebTournamentInfoParser();

        return new WebTournamentParserService(webTournamentInfoParser,webDisciplineParserService);
    }

    protected WebDisciplineParserService prepareWebDisciplineParser() {
        MatchSetParserService matchSetParserService = new MatchSetParserService();
        MatchPlayerParserService matchPlayerParserService = new MatchPlayerParserService();

        WebMatchParserService webMatchParserService = new WebMatchParserService(matchSetParserService, matchPlayerParserService);
        WebDisciplineInfoParserService webDisciplineInfoParserService = new WebDisciplineInfoParserService();
        return new WebDisciplineParserService(webDisciplineInfoParserService,webMatchParserService);
    }
}
