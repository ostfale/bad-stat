package de.ostfale.qk.web.player;

import de.ostfale.qk.web.api.BaseWebUrlFacade;
import org.htmlunit.html.HtmlElement;

import java.util.List;

public interface PlayerUrlFacade extends BaseWebUrlFacade {

    String SEARCH_PLAYER_URL = BASE_DBV_URL + "/find/player?q=";

   // String PLAYER_PROFILE_TOURNAMENT_ID = ".//div[contains(@class, 'media__wrapper')]";
    String PLAYER_PROFILE_TOURNAMENT_ID = ".//a[contains(@href, 'player-profile')]";

    default String prepareSearchPlayerUrl(String playerId) {
        return SEARCH_PLAYER_URL + playerId;
    }

    default List<HtmlElement> getPlayerProfileTournamentId(HtmlElement playerElement) {
        return playerElement.getByXPath(PLAYER_PROFILE_TOURNAMENT_ID);
    }
}
