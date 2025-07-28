package de.ostfale.qk.parser.web.set;

import de.ostfale.qk.domain.match.MatchResultType;
import de.ostfale.qk.domain.set.MatchSet;
import de.ostfale.qk.domain.set.SetNumber;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.web.match.MatchResultAnalyzer;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SetParserService implements SetParser {

    private static final int REGULAR_POINTS_PER_SET = 2;

    private MatchResultAnalyzer matchResultAnalyzer;

    @Override
    public List<MatchSet> parseSets(String[] rawSetElements) throws HtmlParserException {
        Log.debug("SetParserService :: parse sets");
        matchResultAnalyzer = new MatchResultAnalyzer(rawSetElements);

        // check for existence of a retired set
        if (matchResultAnalyzer.isRetiredMatch()) {
            return createMatchSetsForRetiredSet(rawSetElements);
        }

        // walkover will have no points only the reference to the result type
        if (matchResultAnalyzer.isWalkOverMatch()) {
            return createMatchForWalkoverSet();
        }

        // extract scores for a match with regular sets
        List<Integer> setScores = matchResultAnalyzer.getMatchResultScores();
        return createMatchSetsFromScores(setScores);
    }

    private List<MatchSet> createMatchForWalkoverSet() {
        MatchSet matchSet = new MatchSet(MatchResultType.WALKOVER);
        return List.of(matchSet);
    }

    private List<MatchSet> createMatchSetsForRetiredSet(String[] rawSetElements) {
        List<Integer> setScores = matchResultAnalyzer.getMatchResultScores();
        if (setScores.isEmpty()) {
            Log.debug("SetParserService :: No set scores found for retired set");
            var retiredSet = createMatchSet(0, List.of(0, 0));
            retiredSet.setMatchResultType(MatchResultType.RETIRED);
            return List.of(retiredSet);
        }
        return createMatchSetsFromScores(setScores);
    }

    private List<MatchSet> createMatchSetsFromScores(List<Integer> setScores) {
        final int numberOfSets = setScores.size() / REGULAR_POINTS_PER_SET;
        List<MatchSet> matchSets = new ArrayList<>(numberOfSets);

        for (int setIndex = 0; setIndex < numberOfSets; setIndex++) {
            matchSets.add(createMatchSet(setIndex, setScores));
        }
        return matchSets;
    }

    private MatchSet createMatchSet(int setIndex, List<Integer> setScores) {
        SetNumber setNumber = SetNumber.values()[setIndex];
        int firstPlayerScore = setScores.get(setIndex * 2);
        int secondPlayerScore = setScores.get(setIndex * 2 + 1);
        return new MatchSet(setNumber, firstPlayerScore, secondPlayerScore);
    }
}
