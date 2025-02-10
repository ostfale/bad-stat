package de.ostfale.qk.ui.statistics;

import org.jboss.logging.Logger;

public class PlayerTournamentsStatisticsModel {

    private static final Logger log = Logger.getLogger(PlayerTournamentsStatisticsModel.class);

    private String tournamentDate;
    private String tournamentName;
    private String tournamentLocation;


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
