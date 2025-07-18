package de.ostfale.qk.parser.web.set;

import de.ostfale.qk.domain.match.MatchResultType;
import de.ostfale.qk.domain.set.MatchSet;
import de.ostfale.qk.domain.set.SetNumber;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SetParserService implements SetParser {

    private static final int REGULAR_POINTS_PER_SET = 2;

    @Override
    public List<MatchSet> parseSets(String[] rawSetElements) {
        Log.debug("SetParserService :: parse sets");

        // check for existence of a retired set
        if (isRetiredSet(List.of(rawSetElements))) {
            return createMatchSetsForRetiredSet(rawSetElements);
        }

        // extract scores for a match with regular sets
        List<Integer> setScores = extractNumbersFromStrings(List.of(rawSetElements));
        return createMatchSetsFromScores(setScores);
    }

    private List<MatchSet> createMatchSetsForRetiredSet(String[] rawSetElements) {
        List<Integer> setScores = extractNumbersFromStrings(List.of(rawSetElements));
        if (setScores.isEmpty()) {
            Log.debug("SetParserService :: No set scores found for retired set");
            var retiredSet = createMatchSet(0, List.of(0, 0));
            retiredSet.setMatchResultType(MatchResultType.RETIRED);
            return List.of(retiredSet);
        }
        return createMatchSetsFromScores(setScores);
    }

    private List<Integer> extractNumbersFromStrings(List<String> inputStrings) {
        return inputStrings.stream()
                .filter(this::isNumeric)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private List<MatchSet> createMatchSetsFromScores(List<Integer> setScores) {
        final int numberOfSets = setScores.size() / REGULAR_POINTS_PER_SET;
        List<MatchSet> matchSets = new ArrayList<>(numberOfSets);

        for (int setIndex = 0; setIndex < numberOfSets; setIndex++) {
            matchSets.add(createMatchSet(setIndex, setScores));
        }
        return matchSets;
    }

    private boolean isRetiredSet(List<String> inputStrings) {
        Log.debug("SetParserService :: isRetiredSet");
        return inputStrings.stream()
                .anyMatch(str -> str.startsWith(MatchResultType.RETIRED.getDisplayName()));
    }

    private MatchSet createMatchSet(int setIndex, List<Integer> setScores) {
        SetNumber setNumber = SetNumber.values()[setIndex];
        int firstPlayerScore = setScores.get(setIndex * 2);
        int secondPlayerScore = setScores.get(setIndex * 2 + 1);
        return new MatchSet(setNumber, firstPlayerScore, secondPlayerScore);
    }
}
