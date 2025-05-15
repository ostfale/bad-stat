package de.ostfale.qk.web.player;

import de.ostfale.qk.web.common.CookieDialogHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class PlayerWebParserService implements PlayerUrlFacade {

    private static final Logger log = Logger.getLogger(PlayerWebParserService.class);

    @Inject
    CookieDialogHandler cookieDialogHandler;

    public PlayerTournamentId getPlayerTournamentId(String playerId) {
        log.debugf("Get player tournament id for %s", playerId);

        HtmlPage playerProfilePage = loadPlayerProfilePage(playerId);
        String tournamentId = extractTournamentIdFromPage(playerProfilePage);

        log.debugf("Found player tournament id %s", tournamentId);
        return new PlayerTournamentId(tournamentId);
    }

    private HtmlPage loadPlayerProfilePage(String playerId) {
        String playerProfileUrl = prepareSearchPlayerUrl(playerId);
        return cookieDialogHandler.loadWebsite(playerProfileUrl);
    }

    private String extractTournamentIdFromPage(HtmlPage playerProfilePage) {
        List<HtmlElement> tournamentElements = getPlayerProfileTournamentId(playerProfilePage.getActiveElement());

        if (tournamentElements.isEmpty()) {
            throw new IllegalStateException("Tournament ID element not found on page");
        }

        String profileUrl = tournamentElements.getFirst().getAttribute("href");
        return profileUrl.substring(profileUrl.lastIndexOf("/") + 1);
    }
}
