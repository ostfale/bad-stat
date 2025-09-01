package de.ostfale.qk.domain.tourcal;

import de.ostfale.qk.app.TimeHandlerFacade;
import io.quarkus.logging.Log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlannedTournaments implements TimeHandlerFacade {

    private List<PlannedTournament> allPlannedTournaments = new ArrayList<>();

    Predicate<Integer> isThisYearsTournament = year -> year == getActualCalendarYear();
    Predicate<Integer> isNextYearsTournament = year -> year == getActualCalendarYear() + 1;

    public PlannedTournaments(List<PlannedTournament> allPlannedTournaments) {
        this.allPlannedTournaments = allPlannedTournaments;
    }

    public PlannedTournaments() {
    }

    public void addPlannedTournament(PlannedTournament plannedTournament) {
        allPlannedTournaments.add(plannedTournament);
    }

    public void addAllPlannedTournaments(List<PlannedTournament> plannedTournaments) {
        this.allPlannedTournaments.addAll(plannedTournaments);
    }


    // filter tournaments view range

    // all tournaments for this and the next year
    public List<PlannedTournament> getAllPlannedTournaments() {
        return allPlannedTournaments;
    }

    // all tournaments for next year
    public List<PlannedTournament> getAllTournamentsForNextYear() {
        return getTournamentsByYear(isNextYearsTournament);
    }

    // all tournaments for this year
    public List<PlannedTournament> getAllTournamentsForThisYear() {
        return getTournamentsByYear(isThisYearsTournament);
    }

    // all tournaments which are in the future
    public List<PlannedTournament> getAllFuturePlannedTournaments(LocalDate checkDate) {
        var thisYear = getActualCalendarYear();
        var foundTournaments = allPlannedTournaments.stream()
                .filter(tournament -> parseDateToTournamentFormat(tournament.startDate()).isAfter(checkDate))
                .toList();
        Log.debugf("PlannedTournaments:: get all future tournaments : found %d ", foundTournaments.size());
        return foundTournaments;
    }

    // all tournaments for this year which are in the future
    public List<PlannedTournament> getAllFutureTournamentsThisYear(LocalDate checkDate) {
        var thisYear = getActualCalendarYear();
        var foundTournaments = allPlannedTournaments.stream()
                .filter(tournament -> {
                    var tournamentStartDate = parseDateToTournamentFormat(tournament.startDate());
                    return tournamentStartDate.isAfter(checkDate) &&
                            tournamentStartDate.getYear() == thisYear;
                })
                .toList();
        Log.debugf("PlannedTournaments:: get all future tournaments : found %d ", foundTournaments.size());
        return foundTournaments;
    }

    public List<PlannedTournament> getAllNotYetFinishedTournaments(LocalDate checkDate) {
        var thisYear = getActualCalendarYear();
        var foundTournaments = allPlannedTournaments.stream()
                .filter(tournament -> {
                    var tournamentStartDate = parseDateToTournamentFormat(tournament.startDate());
                    return tournamentStartDate.isAfter(checkDate) &&
                            tournamentStartDate.getYear() == thisYear;
                })
                .toList();
        Log.debugf("PlannedTournaments:: get all future tournaments : found %d ", foundTournaments.size());
        return foundTournaments;
    }


    public int getNumberOfAllPlannedTournaments() {
        var nofAllTournaments = allPlannedTournaments.size();
        Log.debugf("PlannedTournaments:: number of all tournament : found %d ", nofAllTournaments);
        return nofAllTournaments;
    }



    private List<PlannedTournament> getTournamentsByYear(Predicate<Integer> yearPredicate) {
        return allPlannedTournaments.stream()
                .filter(tournament -> yearPredicate.test(
                        parseDateToTournamentFormat(tournament.startDate()).getYear()))
                .collect(Collectors.toList());
    }
}
