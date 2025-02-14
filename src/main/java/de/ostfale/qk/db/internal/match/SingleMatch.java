package de.ostfale.qk.db.internal.match;

import de.ostfale.qk.db.api.tournament.Tournament;
import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.match.internal.model.MatchInfoDTO;
import de.ostfale.qk.parser.match.internal.model.SingleMatchDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class SingleMatch extends BaseMatch {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = TOURNAMENT_ID_COLUMN, referencedColumnName = TOURNAMENT_REFERENCED_ID_COLUMN, nullable = false)
    private Tournament associatedTournament;

    private String firstPlayerName;
    private String secondPlayerName;

    private String disciplineName = "";
    private String roundName = "";
    private String matchDuration = "";
    private String playersSets = "";

    public SingleMatch() {
    }

    public SingleMatch(Tournament tournament, SingleMatchDTO singleMatchDTO,String disciplineName) {
        MatchInfoDTO matchInfoDTO = singleMatchDTO.getMatchInfoDTO();
        this.disciplineName = disciplineName;
        this.roundName = matchInfoDTO.getRoundName();
        this.matchDuration = matchInfoDTO.getRoundDuration();
        this.associatedTournament = tournament;
        this.firstPlayerName = singleMatchDTO.getFirstPlayer().getName();
        this.secondPlayerName = singleMatchDTO.getSecondPlayer().getName();
        this.playersSets = mapPlayerSetsToString(singleMatchDTO);
    }

    @Override
    public Discipline getDiscipline() {
        return Discipline.SINGLE;
    }

    @Override
    public List<String> getPlayerNames() {
        return List.of(firstPlayerName, secondPlayerName);
    }

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

    public String getFirstPlayerName() {
        return firstPlayerName;
    }

    public void setFirstPlayerName(String firstPlayerName) {
        this.firstPlayerName = firstPlayerName;
    }

    public String getSecondPlayerName() {
        return secondPlayerName;
    }

    public void setSecondPlayerName(String secondPlayerName) {
        this.secondPlayerName = secondPlayerName;
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
