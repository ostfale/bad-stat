package de.ostfale.qk.db.internal.match;

import de.ostfale.qk.db.api.tournament.Tournament;
import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.match.internal.model.DoubleMatchRawModel;
import de.ostfale.qk.parser.match.internal.model.MatchInfoRawModel;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class DoubleMatch extends BaseMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = TOURNAMENT_ID_COLUMN, referencedColumnName = TOURNAMENT_REFERENCED_ID_COLUMN, nullable = false)
    private Tournament associatedTournament;

    private String onePlayerOneName;
    private String onePlayerTwoName;
    private String twoPlayerOneName;
    private String twoPlayerTwoName;

    private String disciplineName = "";
    private String roundName = "";
    private String matchDuration = "";
    private String playersSets = "";

    public DoubleMatch() {
    }

    public DoubleMatch(Tournament tournament, DoubleMatchRawModel doubleMatchRawModel, String disciplineName) {
        this.disciplineName = disciplineName;
        this.roundName = doubleMatchRawModel.getRoundName();
        this.matchDuration = doubleMatchRawModel.getRoundDuration();
        this.associatedTournament = tournament;
        this.onePlayerOneName = doubleMatchRawModel.getFirstDoublePlayerOne().getName();
        this.onePlayerTwoName = doubleMatchRawModel.getFirstDoublePlayerTwo().getName();
        this.twoPlayerOneName = doubleMatchRawModel.getSecondDoublePlayerOne().getName();
        this.twoPlayerTwoName = doubleMatchRawModel.getSecondDoublePlayerTwo().getName();
        this.playersSets = mapPlayerSetsToString(doubleMatchRawModel);
    }

    @Override
    public List<String> getPlayerNames() {
        return List.of(onePlayerOneName, onePlayerTwoName, twoPlayerOneName, twoPlayerTwoName);
    }

    @Override
    public Discipline getDiscipline() {
        return Discipline.DOUBLE;
    }

    @Override
    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public String getPlayersSets() {
        return playersSets;
    }

    public void setPlayersSets(String playersSets) {
        this.playersSets = playersSets;
    }

    public String getOnePlayerOneName() {
        return onePlayerOneName;
    }

    public void setOnePlayerOneName(String onePlayerOneName) {
        this.onePlayerOneName = onePlayerOneName;
    }

    public String getOnePlayerTwoName() {
        return onePlayerTwoName;
    }

    public void setOnePlayerTwoName(String onePlayerTwoName) {
        this.onePlayerTwoName = onePlayerTwoName;
    }

    public String getTwoPlayerOneName() {
        return twoPlayerOneName;
    }

    public void setTwoPlayerOneName(String twoPlayerOneName) {
        this.twoPlayerOneName = twoPlayerOneName;
    }

    public String getTwoPlayerTwoName() {
        return twoPlayerTwoName;
    }

    public void setTwoPlayerTwoName(String twoPlayerTwoName) {
        this.twoPlayerTwoName = twoPlayerTwoName;
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

    public void setAssociatedTournament(Tournament associatedTournament) {
        this.associatedTournament = associatedTournament;
    }
}
