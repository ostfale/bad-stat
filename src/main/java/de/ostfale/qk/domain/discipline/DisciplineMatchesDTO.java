package de.ostfale.qk.domain.discipline;

import de.ostfale.qk.domain.match.MatchesDTO;

import java.util.List;

public class DisciplineMatchesDTO {

    private String disciplineName;
    private String ageClass;
    private List<MatchesDTO> matchesDTOs;

    public String getDisciplineName() {
        return disciplineName;
    }

    public String getAgeClass() {
        return ageClass;
    }

    public void setAgeClass(String ageClass) {
        this.ageClass = ageClass;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public List<MatchesDTO> getMatchesDTOs() {
        return matchesDTOs;
    }

    public void setMatchesDTOs(List<MatchesDTO> matchesDTOs) {
        this.matchesDTOs = matchesDTOs;
    }
}
