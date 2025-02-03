package de.ostfale.qk.db.internal.match;

import de.ostfale.qk.db.internal.Tournament;
import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.match.internal.model.MatchInfoDTO;
import de.ostfale.qk.parser.match.internal.model.MixedMatchDTO;
import jakarta.persistence.*;

@Entity
public class MixedMatch extends BaseMatch {

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
        this.malePlayerOneName = mixedMatchDTO.getFirstMixedPlayerOne().getName();
        this.femalePlayerOneName = mixedMatchDTO.getFirstMixedPlayerTwo().getName();
        this.malePlayerTwoName = mixedMatchDTO.getSecondMixedPlayerOne().getName();
        this.femalePlayerTwoName = mixedMatchDTO.getSecondMixedPlayerTwo().getName();
        this.playersSets = mapPlayerSetsToString(mixedMatchDTO);
    }

    @Override
    public Discipline getDiscipline() {
        return Discipline.MIXED;
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
