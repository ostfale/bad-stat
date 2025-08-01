package de.ostfale.qk.domain.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import de.ostfale.qk.domain.set.MatchSet;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class DisciplineMatch implements Match {

    private static final String PLAYER_SEPARATOR = " / ";

    private String matchDate;
    private String roundName;
    private String playerOneName;
    private String partnerOneName = null;
    private String playerTwoName;
    private String partnerTwoName = null;

    private final List<MatchSet> matchSets = new ArrayList<>();

    public DisciplineMatch() {
    }

    @JsonIgnore
    @Override
    public String getFirstPlayerOrWithPartnerName() {
        if (partnerOneName != null) {
            return playerOneName + PLAYER_SEPARATOR + partnerOneName;
        }
        return playerOneName;
    }

    @JsonIgnore
    public String prepareRoundNameForGroupMatch(String groupName) {
        return roundName + " (" + groupName + ")";
    }

    @JsonIgnore
    @Override
    public String getSecondPlayerOrWithPartnerName() {
        if (partnerTwoName != null) {
            return playerTwoName + PLAYER_SEPARATOR + partnerTwoName;
        }
        return playerTwoName;
    }

    @Override
    public String getRoundName() {
        return roundName;
    }

    @Override
    public String getMatchDate() {
        return matchDate != null ? matchDate : "";
    }

    @Override
    public List<MatchSet> getMatchSets() {
        return matchSets;
    }

    @JsonIgnore
    public List<String> getSetResults() {
        return matchSets.stream().map(MatchSet::getDisplayString).toList();
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }

    public String getPartnerOneName() {
        return partnerOneName;
    }

    public void setPartnerOneName(String partnerOneName) {
        this.partnerOneName = partnerOneName;
    }

    public String getPartnerTwoName() {
        return partnerTwoName;
    }

    public void setPartnerTwoName(String partnerTwoName) {
        this.partnerTwoName = partnerTwoName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }
}
