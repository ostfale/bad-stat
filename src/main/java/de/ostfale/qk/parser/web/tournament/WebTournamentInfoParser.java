package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.domain.tournament.TournamentInfo;
import de.ostfale.qk.parser.web.HtmlStructureParser;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Singleton
public class WebTournamentInfoParser {

    @Inject
    HtmlStructureParser htmlStructureParser;

    public TournamentInfo parseTournamentInfo(HtmlElement content) {
        Log.debug("WebTournamentInfoParser :: Parsing tournament info");
        var tournamentName = extractTournamentName(content);

        OrgLoc tournamentOrganisationAndLocation = extractTournamentOrganisation(content);
        var tournamentOrganisation = tournamentOrganisationAndLocation.organisation();
        var tournamentLocation = tournamentOrganisationAndLocation.location();

        DateYear tournamentDateAndYear = extractTournamentDate(content);
        var tournamentDate = tournamentDateAndYear.date();
        var tournamentYear = tournamentDateAndYear.getYear();
        return new TournamentInfo(tournamentName, tournamentOrganisation, tournamentLocation, tournamentDate, tournamentYear);
    }

    private String extractTournamentName(HtmlElement content) {
        var result = htmlStructureParser.extractTournamentTitleElement(content).asNormalizedText();
        Log.debugf("WebTournamentInfoParser :: Parsing tournament name -> %s", result);
        return result;
    }

    private OrgLoc extractTournamentOrganisation(HtmlElement content) {
        var result = htmlStructureParser.extractTournamentOrgAndLocationElement(content).asNormalizedText();
        var location = "";
        if (result.contains("|")) {
            var organisationAndLocation = result.split("\\|");
            var organisation = organisationAndLocation[0].trim();
            location = organisationAndLocation[1].trim();

            if (location.contains("[")) {
                Log.tracef("WebTournamentInfoParser :: Location contains '[' -> will be removed");
                location = location.substring(0, location.indexOf("["));
            }

            Log.debugf("WebTournamentInfoParser :: Parsing tournament organisation and location -> %s | %s", organisation, location);
            return new OrgLoc(organisation, location);
        }
        Log.debugf("WebTournamentInfoParser :: Parsing tournament organisation (no location) -> %s", result);
        return new OrgLoc(result, location);
    }

    private DateYear extractTournamentDate(HtmlElement content) {
        var result = htmlStructureParser.extractTournamentDateElement(content).asNormalizedText();
        Log.debugf("WebTournamentInfoParser :: Parsing tournament date -> %s", result);
        return new DateYear(result);
    }

    private record OrgLoc(
            String organisation,
            String location
    ) {
    }

    private record DateYear(
            String date
    ) {
        public int getYear() {
            if (date == null || date.isEmpty()) {
                return 1970;
            }

            if (date.contains("bis")) {
                var splitFromTo = date.split("bis");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                var result = LocalDate.parse(splitFromTo[0].trim(), formatter);
                return result.getYear();
            }
            return 1970;
        }
    }
}

