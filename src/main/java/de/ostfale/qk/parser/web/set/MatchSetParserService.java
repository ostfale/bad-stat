package de.ostfale.qk.parser.web.set;

import de.ostfale.qk.domain.set.MatchSet;
import de.ostfale.qk.domain.set.SetNumber;
import de.ostfale.qk.parser.web.match.MatchParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.htmlunit.html.HtmlElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MatchSetParserService implements MatchParser {

    private static final String MATCH_RAST = "Rast";

    public List<MatchSet> parseMatchSets(HtmlElement matchGroupElement) {
        Log.debug("MatchSetParser :: parse match set");
        String[] rawSetScores = extractMatchBodyElements(matchGroupElement);

        if (isRastSet(List.of(rawSetScores))) {
            MatchSet matchSet = new MatchSet(MATCH_RAST);
            return List.of(matchSet);
        }

        List<Integer> setScores = extractNumbersFromStrings(List.of(rawSetScores));

        return createMatchSetsFromScores(setScores);
    }

    private List<MatchSet> createMatchSetsFromScores(List<Integer> setScores) {
        final int POINTS_PER_SET = 2;
        final int numberOfSets = setScores.size() / POINTS_PER_SET;
        List<MatchSet> matchSets = new ArrayList<>();

        if (setScores.size() != numberOfSets * POINTS_PER_SET) {
            return matchSets;
        }

        for (int setIndex = 0; setIndex < numberOfSets; setIndex++) {
            matchSets.add(createSingleMatchSet(setIndex, setScores));
        }
        return matchSets;
    }

    private MatchSet createSingleMatchSet(int setIndex, List<Integer> setScores) {
        SetNumber setNumber = SetNumber.values()[setIndex];
        int firstPlayerScore = setScores.get(setIndex * 2);
        int secondPlayerScore = setScores.get(setIndex * 2 + 1);

        return new MatchSet(setNumber, firstPlayerScore, secondPlayerScore);
    }

    private List<Integer> extractNumbersFromStrings(List<String> inputStrings) {
        return inputStrings.stream()
                .filter(this::isNumeric)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private boolean isRastSet(List<String> inputStrings) {
        Log.debug("MatchSetParser :: isRastSet");
        return inputStrings.stream()
                .anyMatch(str -> str.equalsIgnoreCase(MATCH_RAST));
    }
}

