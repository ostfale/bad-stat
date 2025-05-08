package de.ostfale.qk.persistence.player.matches;

import java.util.List;

public class TournamentMatchesDTO {

    private String tournamentName;
    private String tournamentDate;
    private String tournamentLocation;

    private List<DisciplineMatchesDTO> disciplineMatches;

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getTournamentDate() {
        return tournamentDate;
    }

    public void setTournamentDate(String tournamentDate) {
        this.tournamentDate = tournamentDate;
    }

    public String getTournamentLocation() {
        return tournamentLocation;
    }

    public void setTournamentLocation(String tournamentLocation) {
        this.tournamentLocation = tournamentLocation;
    }

    public List<DisciplineMatchesDTO> getDisciplineMatches() {
        return disciplineMatches;
    }

    public void setDisciplineMatches(List<DisciplineMatchesDTO> disciplineMatches) {
        this.disciplineMatches = disciplineMatches;
    }
}
