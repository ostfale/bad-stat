package de.ostfale.qk.db.internal;

import de.ostfale.qk.parser.match.internal.model.MatchInfoDTO;
import de.ostfale.qk.parser.match.internal.model.MixedMatchDTO;
import de.ostfale.qk.parser.set.SetDTO;
import jakarta.persistence.*;

import java.util.stream.Collectors;

@Entity
public class MixedMatch {

    public static final String TOURNAMENT_ID_COLUMN = "tournament_id";
    public static final String TOURNAMENT_REFERENCED_ID_COLUMN = "id";

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = TOURNAMENT_ID_COLUMN, referencedColumnName = TOURNAMENT_REFERENCED_ID_COLUMN, nullable = false)
    private Tournament associatedTournament;

    private String femalePlayerOneName ;
    private String malePlayerOneName ;
    private String femalePlayerTwoName ;
    private String malePlayerTwoName ;

    private String roundName ="";
    private String matchDuration ="";
    private String playersSets = "";

    public MixedMatch() {
    }

    public MixedMatch(Tournament associatedTournament, MixedMatchDTO mixedMatchDTO) {
        MatchInfoDTO matchInfoDTO = mixedMatchDTO.getMatchInfoDTO();
        this.roundName = matchInfoDTO.getRoundName();
        this.matchDuration = matchInfoDTO.getRoundDuration();
        this.associatedTournament = associatedTournament;
        this.femalePlayerOneName = mixedMatchDTO.getFirstMixedPlayerOne().getName();
        this.malePlayerOneName = mixedMatchDTO.getFirstMixedPlayerTwo().getName();
        this.femalePlayerTwoName = mixedMatchDTO.getSecondMixedPlayerOne().getName();
        this.malePlayerTwoName = mixedMatchDTO.getSecondMixedPlayerTwo().getName();

        playersSets = mixedMatchDTO.getPlayersSets()
                .stream()
                .map(SetDTO::getSetAsString)
                .collect(Collectors.joining(";"));
    }

    public String getPlayersSets() {
        return playersSets;
    }

    public void setPlayersSets(String playersSets) {
        this.playersSets = playersSets;
    }

    public String getFemalePlayerOneName() {
        return femalePlayerOneName;
    }

    public void setFemalePlayerOneName(String femalePlayerOneName) {
        this.femalePlayerOneName = femalePlayerOneName;
    }

    public String getMalePlayerOneName() {
        return malePlayerOneName;
    }

    public void setMalePlayerOneName(String malePlayerOneName) {
        this.malePlayerOneName = malePlayerOneName;
    }

    public String getFemalePlayerTwoName() {
        return femalePlayerTwoName;
    }

    public void setFemalePlayerTwoName(String femalePlayerTwoName) {
        this.femalePlayerTwoName = femalePlayerTwoName;
    }

    public String getMalePlayerTwoName() {
        return malePlayerTwoName;
    }

    public void setMalePlayerTwoName(String twoPlayerTwoName) {
        this.malePlayerTwoName = twoPlayerTwoName;
    }

    public String getMatchDuration() {
        return matchDuration;
    }

    public void setMatchDuration(String matchDuration) {
        this.matchDuration = matchDuration;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tournament getAssociatedTournament() {
        return associatedTournament;
    }

    public void setAssociatedTournament(Tournament tournament) {
        this.associatedTournament = tournament;
    }
}
