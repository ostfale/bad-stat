package de.ostfale.qk.domain.tourcal;

import java.util.ArrayList;
import java.util.List;

public class PlannedTournaments {

    private final List<PlannedTournament> allPlannedTournaments = new ArrayList<>();

    public List<PlannedTournament> getAllPlannedTournaments() {
        return allPlannedTournaments;
    }
}
