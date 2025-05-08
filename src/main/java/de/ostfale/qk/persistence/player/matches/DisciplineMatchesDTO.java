package de.ostfale.qk.persistence.player.matches;

import java.util.List;

public class DisciplineMatchesDTO {

    private String disciplineName;
    private List<MatchesDTO> matchesDTOs;

    public String getDisciplineName() {
        return disciplineName;
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
