package de.ostfale.qk.db.internal;

import de.ostfale.qk.parser.match.internal.model.MatchInfoDTO;
import de.ostfale.qk.parser.match.internal.model.SingleMatchDTO;
import de.ostfale.qk.parser.set.SetDTO;
import jakarta.persistence.*;

import java.util.stream.Collectors;

@Entity
public class SingleMatch {

    public static final String TOURNAMENT_ID_COLUMN = "tournament_id";
    public static final String TOURNAMENT_REFERENCED_ID_COLUMN = "id";
    private static final String PLAYERS_SETS_DELIMITER = ";";

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = TOURNAMENT_ID_COLUMN, referencedColumnName = TOURNAMENT_REFERENCED_ID_COLUMN, nullable = false)
    private Tournament associatedTournament;

    private String firstPlayerName;
    private String secondPlayerName;

    private String roundName = "";
    private String matchDuration = "";
    private String playersSets = "";

    public SingleMatch() {
    }

    public SingleMatch(Tournament tournament, SingleMatchDTO singleMatchDTO) {
        MatchInfoDTO matchInfoDTO = singleMatchDTO.getMatchInfoDTO();
        this.roundName = matchInfoDTO.getRoundName();
        this.matchDuration = matchInfoDTO.getRoundDuration();
        this.associatedTournament = tournament;
        this.firstPlayerName = singleMatchDTO.getFirstPlayer().getName();
        this.secondPlayerName = singleMatchDTO.getSecondPlayer().getName();

        this.playersSets = mapPlayersSetsToString(singleMatchDTO);
    }

    private String mapPlayersSetsToString(SingleMatchDTO singleMatchDTO) {
        return singleMatchDTO.getPlayersSets()
                .stream()
                .map(SetDTO::getSetAsString)
                .collect(Collectors.joining(PLAYERS_SETS_DELIMITER));
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
