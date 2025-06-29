package de.ostfale.qk.parser.match.internal;

import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.parser.match.api.MatchParser;
import de.ostfale.qk.parser.match.internal.model.*;
import de.ostfale.qk.parser.player.PlayerRawModel;
import de.ostfale.qk.parser.set.SetNo;
import de.ostfale.qk.parser.set.SetRawModel;
import io.quarkus.logging.Log;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class MatchParserService implements MatchParser {

    private static final String WINNER_MARKER = "W";
    private static final String LOSER_MARKER = "L";
    private static final String WALKOVER_MARKER = "Walkover";
    private static final String WALKOVER_MARKER_LOST = "Walkover L";
    private static final String RETIRED_MARKER = "Retired.";
    private static final String RETIRED_MARKER_LOST = "Retired. L";
    private static final String RETIRED_MARKER_NO_MATCH = "Kein Spiel";
    private static final String H2H_MARKER = "H2H";
    private static final String MATCH_RAST = "Rast";
    private static final String EMPTY_PLAYER_NAME = "";

    @Override
    public SingleMatchRawModel parseSingleMatch(HtmlElement content) {
        Log.debug("Parsing single match");
        String[] resultSplit = splitRawData(content);
        var result = extractNumbersFromStrings(List.of(resultSplit));
        var sets = prepareSets(result);
        List<PlayerRawModel> playerRawModelList = createPlayerDTOsFromResult(resultSplit);

        if (containsWalkover(resultSplit)) {
            Log.debug("Walkover detected in single match");
            var singleMatchDTO = new SingleMatchRawModel(playerRawModelList.get(0), playerRawModelList.get(1));
            if (resultSplit[2].equalsIgnoreCase(WALKOVER_MARKER_LOST)) {
                singleMatchDTO.setHasFirstPlayerWonProp(Boolean.TRUE);
            } else {
                singleMatchDTO.setHasFirstPlayerWonProp(Boolean.FALSE);
            }
            return singleMatchDTO;
        }

        if (containsRetired(resultSplit)) {
            Log.debug("Retired detected in single match");
            var singleMatchDTO = new SingleMatchRawModel(playerRawModelList.get(0), playerRawModelList.get(1));
            singleMatchDTO.setMatchRetired(Boolean.TRUE);

            if (!sets.isEmpty()) {
                Log.debugf("Found {} sets found for retired match", sets.size());
                singleMatchDTO.getPlayersSets().addAll(sets);
            }

            if (resultSplit[1].equalsIgnoreCase(RETIRED_MARKER_LOST)) {
                singleMatchDTO.setHasFirstPlayerWonProp(Boolean.FALSE);
            } else if (resultSplit[1].equalsIgnoreCase(WINNER_MARKER)) {
                singleMatchDTO.setHasFirstPlayerWonProp(Boolean.TRUE);
            } else {
                singleMatchDTO.setHasFirstPlayerWonProp(Boolean.TRUE);
            }
            return singleMatchDTO;
        }

        return new SingleMatchRawModel(playerRawModelList.get(0), playerRawModelList.get(1), sets);
    }

    @Override
    public DoubleMatchRawModel parseDoubleMatch(HtmlElement content) {
        Log.debug("Parsing double match");
        String[] resultSplit = splitRawData(content);
        List<PlayerRawModel> playerRawModelList = createPlayerDTOsFromResult(resultSplit);

        if (containsWalkover(resultSplit)) {
            Log.debug("Walkover detected in double match");
            var doubleMatchDT = new DoubleMatchRawModel(playerRawModelList.get(0), playerRawModelList.get(1), playerRawModelList.get(2), playerRawModelList.get(3));
            if (resultSplit[2].equalsIgnoreCase(WINNER_MARKER)) {
                doubleMatchDT.setHasFirstPlayerWonProp(Boolean.TRUE);
            } else {
                doubleMatchDT.setHasFirstPlayerWonProp(Boolean.FALSE);
            }
            return doubleMatchDT;
        }

        var result = extractNumbersFromStrings(List.of(resultSplit));
        var sets = prepareSets(result);

        return prepareDoubleMatch(playerRawModelList,sets);
    }

    @Override
    public MixedMatchRawModel parseMixedMatch(HtmlElement content) {
        Log.debug("Parsing mixed  match");
        String[] resultSplit = splitRawData(content);
        var result = extractNumbersFromStrings(List.of(resultSplit));
        var sets = prepareSets(result);

        List<PlayerRawModel> playerRawModelList = createPlayerDTOsFromResult(resultSplit);

        if (containsWalkover(resultSplit)) {
            Log.debug("Walkover detected in mixed match");
            var mixedMatchDT = new MixedMatchRawModel(playerRawModelList.get(0), playerRawModelList.get(1), playerRawModelList.get(2), playerRawModelList.get(3));
            if (resultSplit[2].equalsIgnoreCase(WINNER_MARKER)) {
                mixedMatchDT.setHasFirstPlayerWonProp(Boolean.TRUE);
            } else {
                mixedMatchDT.setHasFirstPlayerWonProp(Boolean.FALSE);
            }
            return mixedMatchDT;
        }
        return prepareMixedMatch(playerRawModelList,sets);
    }

    private enum MatchType {
        DOUBLE("Double"),
        MIXED("Mixed");

        private final String name;

        MatchType(String name) {
            this.name = name;
        }
    }

    private DoubleMatchRawModel prepareDoubleMatch(List<PlayerRawModel> playerList, List<SetRawModel> sets) {
        return prepareMatch(playerList, sets, MatchType.DOUBLE, DoubleMatchRawModel::new);
    }

    private MixedMatchRawModel prepareMixedMatch(List<PlayerRawModel> playerList, List<SetRawModel> sets) {
        return prepareMatch(playerList, sets, MatchType.MIXED, MixedMatchRawModel::new);
    }

    private <T> T prepareMatch(List<PlayerRawModel> playerList,
                               List<SetRawModel> sets,
                               MatchType matchType,
                               MatchConstructor<T> constructor) {
        if (playerList.size() == 4) {
            Log.debugf("%s match has 4 player entries", matchType.name);
            return constructor.create(playerList.get(0), playerList.get(1),
                    playerList.get(2), playerList.get(3), sets);
        }

        if (playerList.size() == 3) {
            Log.debugf("%s match has 3 player entries", matchType.name);
            boolean hasRastPlayer = playerList.stream()
                    .anyMatch(it -> it.name.equalsIgnoreCase(MATCH_RAST));

            if (hasRastPlayer) {
                Log.debugf("%s match has a Rast player", matchType.name);
                var validPlayers = playerList.stream()
                        .filter(it -> !it.name.equalsIgnoreCase(MATCH_RAST))
                        .toList();
                return constructor.create(validPlayers.getFirst(), validPlayers.getLast(),
                        new PlayerRawModel(EMPTY_PLAYER_NAME),
                        new PlayerRawModel(EMPTY_PLAYER_NAME), sets);
            }
        }

        Log.errorf("%s match has invalid number of player entries: %d",
                matchType.name, playerList.size());
        return null;
    }

    @FunctionalInterface
    private interface MatchConstructor<T> {
        T create(PlayerRawModel p1, PlayerRawModel p2, PlayerRawModel p3,
                 PlayerRawModel p4, List<SetRawModel> sets);
    }


    final String MATCH_ROUND_NAME = ".//li[contains(@class, 'match__header-title-item')]";
    final String MATCH_ROUND_LOCATION_DATE = ".//li[contains(@class, 'match__footer-list-item')]";
    final String MATCH_ROUND_DURATION = ".//div[contains(@class, 'match__header-aside')]";

    @Override
    public MatchInfoRawModel parseMatchGroupInfo(HtmlElement matchGroup) {
        Log.debug("Parsing match group info ");
        HtmlElement matchRoundNameDiv = matchGroup.getFirstByXPath(MATCH_ROUND_NAME);
        List<HtmlElement> matchRoundDateLocDiv = matchGroup.getByXPath(MATCH_ROUND_LOCATION_DATE);
        HtmlElement matchRoundDurationDiv = matchGroup.getFirstByXPath(MATCH_ROUND_DURATION);

        var matchRoundDuration = matchRoundDurationDiv != null ? matchRoundDurationDiv.asNormalizedText() : "";
        var matchRoundName = matchRoundNameDiv != null ? matchRoundNameDiv.asNormalizedText() : "";
        var matchRoundDate = matchRoundDateLocDiv.getFirst() != null ? matchRoundDateLocDiv.getFirst().asNormalizedText() : "";
        var matchRoundCourt = matchRoundDateLocDiv.getLast() != null ? matchRoundDateLocDiv.getLast().asNormalizedText() : "";

        return new MatchInfoRawModel(matchRoundName, matchRoundDate, matchRoundCourt, matchRoundDuration);
    }


    @Override
    public List<Match> parseMatchDiscipline(DisciplineType disciplineType, List<HtmlElement> matchGroups) {
        Log.debugf("Read all matches for discipline: {}", disciplineType);
        for (HtmlElement matchGroup : matchGroups) {
            var result = parseSingleMatch(matchGroup);
            System.out.println("dd");
        }

        return List.of();
    }

    private boolean containsWalkover(String[] resultSplit) {
        return Arrays.stream(resultSplit).anyMatch(s -> s.equalsIgnoreCase(WALKOVER_MARKER) || s.equalsIgnoreCase(WALKOVER_MARKER_LOST));
    }

    private boolean containsRetired(String[] resultSplit) {
        return Arrays.stream(resultSplit).anyMatch(s ->
                s.equalsIgnoreCase(RETIRED_MARKER_LOST)
                        || s.equalsIgnoreCase(RETIRED_MARKER)
                        || s.equalsIgnoreCase(RETIRED_MARKER_NO_MATCH));
    }

    private String[] splitRawData(HtmlElement inputDiv) {
        final String SEPARATOR = "\n";
        return inputDiv.asNormalizedText().split(SEPARATOR);
    }

    // Helper function to eliminate repetitive PlayerDTO creation logic
    private List<PlayerRawModel> createPlayerDTOsFromResult(String[] resultSplit) {
        return Arrays.stream(resultSplit)
                .filter(this::isValidPlayerString)
                .map(PlayerRawModel::new)
                .collect(Collectors.toList());
    }

    private List<SetRawModel> prepareSets(List<Integer> pointsList) {
        final int numberOfSets = pointsList.size() / 2;
        final int POINTS_PER_SET = 2; // Each set contains two points
        List<SetRawModel> setRawModelList = new ArrayList<>();

        if (pointsList.size() == numberOfSets * POINTS_PER_SET) {
            for (int i = 0; i < numberOfSets; i++) {
                SetRawModel set = new SetRawModel(SetNo.values()[i], pointsList.get(i * 2), pointsList.get(i * 2 + 1));
                setRawModelList.add(set);
            }
        }
        return setRawModelList;
    }

    private boolean isValidPlayerString(String str) {
        return !str.equalsIgnoreCase(WINNER_MARKER)
                && !str.equalsIgnoreCase(LOSER_MARKER)
                && !str.equalsIgnoreCase(RETIRED_MARKER_LOST)
                && !str.equalsIgnoreCase(RETIRED_MARKER)
                && !str.equalsIgnoreCase(H2H_MARKER)
                && !isNumeric(str);
    }

    private List<Integer> extractNumbersFromStrings(List<String> inputStrings) {
        List<Integer> numbers = new ArrayList<>();
        for (String str : inputStrings) {
            if (isNumeric(str))
                numbers.add(Integer.parseInt(str));
        }
        return numbers;
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}

