package de.ostfale.qk.domain.tournament;

public record TournamentInfo(
        String tournamentName,
        String tournamentOrganizer,
        String tournamentLocation,
        String tournamentDate,
        Integer tournamentYear
) {
    public TournamentInfo() {
        this("", "", "", "", 1970);
    }
}
