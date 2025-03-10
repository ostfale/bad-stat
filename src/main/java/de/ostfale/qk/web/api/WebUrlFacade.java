package de.ostfale.qk.web.api;

public interface WebUrlFacade {

    // general
    String BASE_DBV_URL = "https://dbv.turnier.de/";


    // tournaments

    // player
    String PLAYER_TOURNAMENTS_URL = BASE_DBV_URL + "player-profile/{player-tournament-id}/tournaments";


    String preparePlayerTournamentsUrl(String playerTournamentId);

    String preparePlayerTournamentsUrl(String playerTournamentId, String year);
}
