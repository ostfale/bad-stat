package de.ostfale.qk.ui.statistics;

import de.ostfale.qk.db.api.tournament.Tournament;
import de.ostfale.qk.db.internal.match.BaseMatch;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class PlayerTournamentsStatisticsModel {

    private static final Logger log = Logger.getLogger(PlayerTournamentsStatisticsModel.class);

    private static final String SPACE = "";

    private String tournamentDate;
    private String tournamentName;
    private String tournamentLocation;
    private String disciplineName;
    private List<PlayerTournamentsStatisticsModel> matchRows = new ArrayList<>();

    public PlayerTournamentsStatisticsModel(Tournament tournament) {
        log.debug("Init PlayerTournamentsStatisticsModel from Tournament");
        this.tournamentDate = tournament.getTournamentDate();
        this.tournamentName = tournament.getTournamentName();
        this.tournamentLocation = tournament.getTournamentLocation();
        tournament.getSingleMatches().stream().map(this::mapMatch).forEach(this.matchRows::add);
        tournament.getDoubleMatches().stream().map(this::mapMatch).forEach(this.matchRows::add);
        tournament.getMixedMatches().stream().map(this::mapMatch).forEach(this.matchRows::add);
    }

    public PlayerTournamentsStatisticsModel() {
    }

    private <T extends BaseMatch> PlayerTournamentsStatisticsModel mapMatch(T match) {
        PlayerTournamentsStatisticsModel model = new PlayerTournamentsStatisticsModel();
        model.setTournamentDate(SPACE);
        model.setTournamentLocation(SPACE);
        model.setTournamentName(SPACE);
        model.setDisciplineName(match.getDisciplineName());
        return model;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public List<PlayerTournamentsStatisticsModel> getMatchRows() {
        return matchRows;
    }

    public void setMatchRows(List<PlayerTournamentsStatisticsModel> matchRows) {
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
