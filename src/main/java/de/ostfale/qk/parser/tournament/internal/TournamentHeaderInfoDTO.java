package de.ostfale.qk.parser.tournament.internal;

public record TournamentHeaderInfoDTO(
        String tournamentId,
        String tournamentName,
        String tournamentOrganisation,
        String tournamentLocation,
        String tournamentDate
) { }
