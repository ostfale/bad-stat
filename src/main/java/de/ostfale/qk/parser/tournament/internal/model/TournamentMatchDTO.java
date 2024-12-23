package de.ostfale.qk.parser.tournament.internal.model;

import java.util.List;

public record TournamentMatchDTO(
        MatchPlayerGroupDTO firstGroup,
        MatchPlayerGroupDTO secondGroup,
        List<SetDTO> sets
) {
}
