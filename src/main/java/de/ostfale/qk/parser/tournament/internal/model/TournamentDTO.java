package de.ostfale.qk.parser.tournament.internal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TournamentDTO {

    private TournamentInfoDTO tournamentInfo;
    private List<TournamentDisciplineDTO> tournamentDisciplines = new ArrayList<>();

    public TournamentDTO(TournamentInfoDTO tournamentInfo) {
        this.tournamentInfo = tournamentInfo;
    }

    public TournamentInfoDTO getTournamentInfo() {
        return tournamentInfo;
    }

    public void setTournamentInfo(TournamentInfoDTO tournamentInfo) {
        this.tournamentInfo = tournamentInfo;
    }

    public void addDiscipline(TournamentDisciplineDTO tournamentDiscipline) {
        this.tournamentDisciplines.add(tournamentDiscipline);
    }

    public List<TournamentDisciplineDTO> getTournamentDisciplines() {
        return tournamentDisciplines;
    }

    public void setTournamentDisciplines(List<TournamentDisciplineDTO> tournamentDisciplines) {
        this.tournamentDisciplines = tournamentDisciplines;
    }

    @Override
    public String toString() {
        return "TournamentDTO{" +
                "tournamentInfo=" + tournamentInfo +
                ", tournamentDisciplines=" + tournamentDisciplines +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TournamentDTO that = (TournamentDTO) o;
        return Objects.equals(tournamentInfo, that.tournamentInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tournamentInfo);
    }
}
