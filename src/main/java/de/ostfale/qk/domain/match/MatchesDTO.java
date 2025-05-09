package de.ostfale.qk.domain.match;

import java.util.List;

public class MatchesDTO {

    private String roundName;
    private String ptOneName;
    private String ptTwoName;
    private List<String> setResults;

    public List<String> getSetResults() {
        return setResults;
    }

    public void setSetResults(List<String> setResults) {
        this.setResults = setResults;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getPtOneName() {
        return ptOneName;
    }

    public void setPtOneName(String ptOneName) {
        this.ptOneName = ptOneName;
    }

    public String getPtTwoName() {
        return ptTwoName;
    }

    public void setPtTwoName(String ptTwoName) {
        this.ptTwoName = ptTwoName;
    }
}
