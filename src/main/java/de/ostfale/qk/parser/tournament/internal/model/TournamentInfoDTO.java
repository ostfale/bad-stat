package de.ostfale.qk.parser.tournament.internal.model;

public record TournamentInfoDTO(
        String tournamentId,
        String tournamentName,
        String tournamentOrganisation,
        String tournamentLocation,
        String tournamentDate
) { }
