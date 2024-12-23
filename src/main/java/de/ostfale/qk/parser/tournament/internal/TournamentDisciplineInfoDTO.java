package de.ostfale.qk.parser.tournament.internal;


import java.util.ArrayList;
import java.util.List;

public class TournamentDisciplineInfoDTO {

    private String disciplineName;
    private String disciplineAgeGroup;
    private List<TournamentMatchInfo> tournamentMatchInfos = new ArrayList<>();

    @Override
    public String toString() {
        return "TournamentDisciplineInfoDTO{" +
                "disciplineName='" + disciplineName + '\'' +
                ", disciplineAgeGroup='" + disciplineAgeGroup + '\'' +
                '}';
    }

    public TournamentDisciplineInfoDTO(String disciplineName, String disciplineAgeGroup) {
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

    public void addMatchInfo(TournamentMatchInfo tournamentMatchInfo) {
        tournamentMatchInfos.add(tournamentMatchInfo);
    }

    public void setDisciplineAgeGroup(String disciplineAgeGroup) {
        this.disciplineAgeGroup = disciplineAgeGroup;
    }

    public List<TournamentMatchInfo> getTournamentMatchInfos() {
        return tournamentMatchInfos;
    }

    public void setTournamentMatchInfos(List<TournamentMatchInfo> tournamentMatchInfos) {
        this.tournamentMatchInfos = tournamentMatchInfos;
    }
}

