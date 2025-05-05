package de.ostfale.qk.ui.playerstats.info.tournamentdata;

import de.ostfale.qk.domain.tournament.RecentYears;
import io.quarkus.logging.Log;

import java.util.ArrayList;
import java.util.List;

public class TournamentsStatisticDTO {

    private String playerId;
    private Integer yearPlayedTournaments = 0;
    private Integer yearDownloadedTournaments = 0;
    private Integer yearMinusOnePlayedTournaments = 0;
    private Integer yearMinusOneDownloadedTournaments = 0;
    private Integer yearMinusTwoPlayedTournaments = 0;
    private Integer yearMinusTwoDownloadedTournaments = 0;
    private Integer yearMinusThreePlayedTournaments = 0;
    private Integer yearMinusThreeDownloadedTournaments = 0;

    public TournamentsStatisticDTO(List<TournamentsStatistic> tournamentsStatistic) {
        Log.debug("TournamentsStatisticDTO :: Create TournamentsStatisticDTO from List<TournamentsStatistic>");

        for (TournamentsStatistic ts : tournamentsStatistic) {
            if (ts.year() == null) {
                Log.warn("TournamentsStatisticDTO :: Encountered TournamentsStatistic with null year. Skipping.");
                continue;
            }
            try {
                RecentYears recentYear = RecentYears.lookup(ts.year());

                switch (recentYear) {
                    case CURRENT_YEAR:
                        yearPlayedTournaments = ts.allTournaments();
                        yearDownloadedTournaments = ts.savedTournaments();
                        break;
                    case YEAR_MINUS_1:
                        yearMinusOnePlayedTournaments = ts.allTournaments();
                        yearMinusOneDownloadedTournaments = ts.savedTournaments();
                        break;
                    case YEAR_MINUS_2:
                        yearMinusTwoPlayedTournaments = ts.allTournaments();
                        yearMinusTwoDownloadedTournaments = ts.savedTournaments();
                        break;
                    case YEAR_MINUS_3:
                        yearMinusThreePlayedTournaments = ts.allTournaments();
                        yearMinusThreeDownloadedTournaments = ts.savedTournaments();
                        break;
                }
            } catch (IllegalArgumentException e) {
                Log.warnf("TournamentsStatisticDTO :: TournamentsStatistic for year %d is not considered recent. Skipping.", ts.year());
            }
        }
    }


    public Boolean hasStatisticForPlayer() {
        return yearPlayedTournaments > 0 ||
                yearMinusOnePlayedTournaments > 0 ||
                yearMinusTwoPlayedTournaments > 0 ||
                yearMinusThreePlayedTournaments > 0;
    }

    public List<String> getTournamentsStatisticAsString() {
        List<String> tournamentsStatistic = new ArrayList<>();
        tournamentsStatistic.add(yearPlayedTournaments.toString() + " / " + yearDownloadedTournaments.toString());
        tournamentsStatistic.add(yearMinusOnePlayedTournaments.toString() + " / " + yearMinusOneDownloadedTournaments.toString());
        tournamentsStatistic.add(yearMinusTwoPlayedTournaments.toString() + " / " + yearMinusTwoDownloadedTournaments.toString());
        tournamentsStatistic.add(yearMinusThreePlayedTournaments.toString() + " / " + yearMinusThreeDownloadedTournaments.toString());
        return tournamentsStatistic;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Integer getYearPlayedTournaments() {
        return yearPlayedTournaments;
    }

    public void setYearPlayedTournaments(Integer yearPlayedTournaments) {
        this.yearPlayedTournaments = yearPlayedTournaments;
    }

    public Integer getYearDownloadedTournaments() {
        return yearDownloadedTournaments;
    }

    public void setYearDownloadedTournaments(Integer yearDownloadedTournaments) {
        this.yearDownloadedTournaments = yearDownloadedTournaments;
    }

    public Integer getYearMinusOnePlayedTournaments() {
        return yearMinusOnePlayedTournaments;
    }

    public void setYearMinusOnePlayedTournaments(Integer yearMinusOnePlayedTournaments) {
        this.yearMinusOnePlayedTournaments = yearMinusOnePlayedTournaments;
    }

    public Integer getYearMinusOneDownloadedTournaments() {
        return yearMinusOneDownloadedTournaments;
    }

    public void setYearMinusOneDownloadedTournaments(Integer yearMinusOneDownloadedTournaments) {
        this.yearMinusOneDownloadedTournaments = yearMinusOneDownloadedTournaments;
    }

    public Integer getYearMinusTwoPlayedTournaments() {
        return yearMinusTwoPlayedTournaments;
    }

    public void setYearMinusTwoPlayedTournaments(Integer yearMinusTwoPlayedTournaments) {
        this.yearMinusTwoPlayedTournaments = yearMinusTwoPlayedTournaments;
    }

    public Integer getYearMinusTwoDownloadedTournaments() {
        return yearMinusTwoDownloadedTournaments;
    }

    public void setYearMinusTwoDownloadedTournaments(Integer yearMinusTwoDownloadedTournaments) {
        this.yearMinusTwoDownloadedTournaments = yearMinusTwoDownloadedTournaments;
    }

    public Integer getYearMinusThreePlayedTournaments() {
        return yearMinusThreePlayedTournaments;
    }

    public void setYearMinusThreePlayedTournaments(Integer yearMinusThreePlayedTournaments) {
        this.yearMinusThreePlayedTournaments = yearMinusThreePlayedTournaments;
    }

    public Integer getYearMinusThreeDownloadedTournaments() {
        return yearMinusThreeDownloadedTournaments;
    }

    public void setYearMinusThreeDownloadedTournaments(Integer yearMinusThreeDownloadedTournaments) {
        this.yearMinusThreeDownloadedTournaments = yearMinusThreeDownloadedTournaments;
    }
}
