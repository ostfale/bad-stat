package de.ostfale.qk.domain.tournament;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class TournamentMatchesListDTO {

    Logger log = Logger.getLogger(TournamentMatchesListDTO.class);

    private String playerName;
    private String playerId;
    private String tournamentYear;

    private final List<TournamentMatchesDTO> tournamentMatchesList = new ArrayList<>();

    public List<TournamentMatchesDTO> getTournamentMatchesList() {
        return tournamentMatchesList;
    }

    @JsonIgnore
    public void addTournamentMatch(TournamentMatchesDTO tournamentMatchesDTO) {
        log.debugf("TournamentMatchesListHandler :: addTournamentMatch (%s)", tournamentMatchesDTO.getTournamentName());
        tournamentMatchesList.add(tournamentMatchesDTO);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getTournamentYear() {
        return tournamentYear;
    }

    public void setTournamentYear(String tournamentYear) {
        this.tournamentYear = tournamentYear;
    }
}
