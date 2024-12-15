package de.ostfale.qk.parser.tournament.internal;

public record TournamentHeaderInfo(
        String tournamentId,
        String tournamentName,
        String tournamentOrganisation,
        String tournamentLocation,
        String tournamentDate
) { }
