package de.ostfale.qk.parser.tournament.internal.model;

public record TournamentMatchDTO(
        MatchPlayerGroupDTO firstGroup,
        MatchPlayerGroupDTO secondGroup
) {
}
