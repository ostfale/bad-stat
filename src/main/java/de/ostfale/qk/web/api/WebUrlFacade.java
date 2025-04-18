package de.ostfale.qk.web.api;

public interface WebUrlFacade {

    // general
    String BASE_DBV_URL = "https://dbv.turnier.de/";

    // ranking files download page
    String RANKING_FILES_DOWNLOAD_URL = "https://www.badminton.de/der-dbv/jugend-wettkampf/ranglistentabelle/";
    String CURRENT_RANKING_FILE_URL = "https://turniere.badminton.de/ranking/download?save=1&gender=&gruppe=&lvname=&bezirk=&firstname=&lastname=&club=&colortype=";

    // tournaments

    // player
    String PLAYER_TOURNAMENTS_URL = BASE_DBV_URL + "player-profile/{player-tournament-id}/tournaments";

    String preparePlayerTournamentsUrl(String playerTournamentId);

    String preparePlayerTournamentsUrl(String playerTournamentId, String year);
}
