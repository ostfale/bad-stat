package de.ostfale.qk.parser.tournament.internal;


public class TournamentDisciplineInfoDTO {

    private String disciplineName;
    private String disciplineAgeGroup;

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

    public void setDisciplineAgeGroup(String disciplineAgeGroup) {
        this.disciplineAgeGroup = disciplineAgeGroup;
    }
}

