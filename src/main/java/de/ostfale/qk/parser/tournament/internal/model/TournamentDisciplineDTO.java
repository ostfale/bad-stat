package de.ostfale.qk.parser.tournament.internal.model;


import java.util.ArrayList;
import java.util.List;

public class TournamentDisciplineDTO {

    private String disciplineName;
    private String disciplineAgeGroup;
    private List<TournamentMatchInfoDTO> tournamentMatchInfoDTOS = new ArrayList<>();

    @Override
    public String toString() {
        return "TournamentDisciplineInfoDTO{" +
                "disciplineName='" + disciplineName + '\'' +
                ", disciplineAgeGroup='" + disciplineAgeGroup + '\'' +
                '}';
    }

    public TournamentDisciplineDTO(String disciplineName, String disciplineAgeGroup) {
        this.disciplineName = disciplineName;
        this.disciplineAgeGroup = disciplineAgeGroup;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public String getDisciplineAgeGroup() {
        return disciplineAgeGroup;
    }

    public void addMatchInfo(TournamentMatchInfoDTO tournamentMatchInfoDTO) {
        tournamentMatchInfoDTOS.add(tournamentMatchInfoDTO);
    }

    public void setDisciplineAgeGroup(String disciplineAgeGroup) {
        this.disciplineAgeGroup = disciplineAgeGroup;
    }

    public List<TournamentMatchInfoDTO> getTournamentMatchInfos() {
        return tournamentMatchInfoDTOS;
    }

    public void setTournamentMatchInfos(List<TournamentMatchInfoDTO> tournamentMatchInfoDTOS) {
        this.tournamentMatchInfoDTOS = tournamentMatchInfoDTOS;
    }
}

