package de.ostfale.qk.ui.statistics;

import de.ostfale.qk.db.api.tournament.Tournament;
import de.ostfale.qk.db.internal.match.Match;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class PlToStatDTO {

    private static final Logger log = Logger.getLogger(PlToStatDTO.class);

    private static final String SPACE = "";

    private String tournamentDate;
    private String tournamentName;
    private String tournamentLocation;
    private String disciplineName;
    private String roundName;
    private List<PlToStatDTO> matchRows = new ArrayList<>();

    public static PlToStatDTO createChildData(Match match) {
        log.debug("PlToStatDTO :: Create new child row ");
        var dto = new PlToStatDTO();
        dto.setTournamentName(SPACE);
        dto.setTournamentDate(SPACE);
        dto.setTournamentLocation(SPACE);
        dto.setDisciplineName(match.getDisciplineName());
        dto.setRoundName(match.getRoundName());
        return dto;
    }

    public static PlToStatDTO createRootData(Tournament tournament) {
        log.debug("PlToStatDTO :: Create new root row ");
        var dto = new PlToStatDTO();
        dto.setTournamentName(tournament.getTournamentName());
        dto.setTournamentDate(tournament.getTournamentDate());
        dto.setTournamentLocation(tournament.getTournamentLocation());
        return dto;
    }

    public PlToStatDTO() {
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public List<PlToStatDTO> getMatchRows() {
        return matchRows;
    }

    public void setMatchRows(List<PlToStatDTO> matchRows) {
        this.matchRows = matchRows;
    }

    public String getTournamentDate() {
        return tournamentDate;
    }

    public void setTournamentDate(String tournamentDate) {
        this.tournamentDate = tournamentDate;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getTournamentLocation() {
        return tournamentLocation;
    }

    public void setTournamentLocation(String tournamentLocation) {
        this.tournamentLocation = tournamentLocation;
    }
}
