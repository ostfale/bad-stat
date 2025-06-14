package de.ostfale.qk.parser;

import de.ostfale.qk.domain.player.PlayerTournamentId;

public interface WebParser {

    String BASE_DBV_URL = "https://dbv.turnier.de/";
    String PLAYER_TOURNAMENTS_URL = BASE_DBV_URL + "player-profile/{player-tournament-id}/tournaments";

    String preparePlayerTournamentsUrl(PlayerTournamentId tournamentId);

    String preparePlayerTournamentsUrl(PlayerTournamentId tournamentId, int year);

}
