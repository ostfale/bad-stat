package de.ostfale.qk.domain.tournament;

import de.ostfale.qk.domain.player.PlayerId;

import java.util.List;

public record TournamentYearWrapper(
        String playerName,
        PlayerId playerId,
        Integer tournamentYear,
        List<Tournament> tournaments
) {
}
