package de.ostfale.qk.ui.statistics;

import de.ostfale.qk.db.api.tournament.Tournament;
import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class PlayerTournamentsStatisticsModel {

    private static final Logger log = Logger.getLogger(PlayerTournamentsStatisticsModel.class);

    private static final String SPACE = "";


    private String tournamentDate;
    private String tournamentName;
    private String tournamentLocation;
    private String discipline = SPACE;

    public PlayerTournamentsStatisticsModel(Tournament tournament) {
        log.debug("Init PlayerTournamentsStatisticsModel from Tournament");
        this.tournamentDate = tournament.getTournamentDate();
        this.tournamentName = tournament.getTournamentName();
        this.tournamentLocation = tournament.getTournamentLocation();
    }

    public PlayerTournamentsStatisticsModel() {
    }

    public static List<PlayerTournamentsStatisticsModel> fromTournamentCompetition(Tournament tournament) {
        log.debugf("Init PlayerTournamentsStatisticsModel match rows for each discipline");
        List<PlayerTournamentsStatisticsModel> childList = new ArrayList<>();
        tournament.getSingleMatches().forEach(match->{
            PlayerTournamentsStatisticsModel model = new PlayerTournamentsStatisticsModel(tournament);
            model.setTournamentDate(SPACE);
            model.setTournamentLocation(SPACE);
            model.setTournamentName(SPACE);
            model.setDiscipline(Discipline.SINGLE.name());
            childList.add(model);
        });
        return childList;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
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
