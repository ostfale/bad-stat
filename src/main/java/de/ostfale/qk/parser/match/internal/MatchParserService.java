package de.ostfale.qk.parser.match.internal;

import de.ostfale.qk.parser.match.api.MatchParser;
import de.ostfale.qk.parser.match.internal.model.DoubleMatchDTO;
import de.ostfale.qk.parser.match.internal.model.MixedMatchDTO;
import de.ostfale.qk.parser.match.internal.model.SingleMatchDTO;
import de.ostfale.qk.parser.player.PlayerDTO;
import de.ostfale.qk.parser.set.SetDTO;
import de.ostfale.qk.parser.set.SetNo;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlDivision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class MatchParserService implements MatchParser {

    private static final Logger log = LoggerFactory.getLogger(MatchParserService.class);

    private static final String WINNER_MARKER = "W";
    private static final String LOSER_MARKER = "L";
    private static final String WALKOVER_MARKER = "Walkover";
    private static final String WALKOVER_MARKER_LOST = "Walkover L";

    @Override
    public SingleMatchDTO parseSingleMatch(HtmlDivision content) {
        log.debug("Parsing single match");
        String[] resultSplit = splitRawData(content);
        var result = extractNumbersFromStrings(List.of(resultSplit));
        var sets = prepareSets(result);
        List<PlayerDTO> playerDTOs = createPlayerDTOsFromResult(resultSplit);

        if (containsWalkover(resultSplit)) {
            log.debug("Walkover detected in single match");
            var singleMatchDTO = new SingleMatchDTO(playerDTOs.get(0), playerDTOs.get(1));
            if (resultSplit[2].equalsIgnoreCase(WALKOVER_MARKER_LOST)) {
                singleMatchDTO.setHasFirstPlayerWonProp(Boolean.TRUE);
            } else {
                singleMatchDTO.setHasFirstPlayerWonProp(Boolean.FALSE);
            }
            return singleMatchDTO;
        }

        return new SingleMatchDTO(playerDTOs.get(0), playerDTOs.get(1), sets);
    }

    @Override
    public DoubleMatchDTO parseDoubleMatch(HtmlDivision content) {
        log.debug("Parsing double match");
        String[] resultSplit = splitRawData(content);
        List<PlayerDTO> playerDTOs = createPlayerDTOsFromResult(resultSplit);

        if (containsWalkover(resultSplit)) {
            log.debug("Walkover detected in double match");
            var doubleMatchDT = new DoubleMatchDTO(playerDTOs.get(0), playerDTOs.get(1), playerDTOs.get(2), playerDTOs.get(3));
            if (resultSplit[2].equalsIgnoreCase(WINNER_MARKER)) {
                doubleMatchDT.setHasFirstPlayerWonProp(Boolean.TRUE);
            } else {
                doubleMatchDT.setHasFirstPlayerWonProp(Boolean.FALSE);
            }
            return doubleMatchDT;
        }

        var result = extractNumbersFromStrings(List.of(resultSplit));
        var sets = prepareSets(result);
        return new DoubleMatchDTO(playerDTOs.get(0), playerDTOs.get(1), playerDTOs.get(2), playerDTOs.get(3), sets);
    }

    @Override
    public MixedMatchDTO parseMixedMatch(HtmlDivision content) {
        log.debug("Parsing mixed  match");
        String[] resultSplit = splitRawData(content);
        var result = extractNumbersFromStrings(List.of(resultSplit));
        var sets = prepareSets(result);

        List<PlayerDTO> playerDTOs = createPlayerDTOsFromResult(resultSplit);

        if (containsWalkover(resultSplit)) {
            log.debug("Walkover detected in mixed match");
            var mixedMatchDT = new MixedMatchDTO(playerDTOs.get(0), playerDTOs.get(1), playerDTOs.get(2), playerDTOs.get(3));
            if (resultSplit[2].equalsIgnoreCase(WINNER_MARKER)) {
                mixedMatchDT.setHasFirstPlayerWonProp(Boolean.TRUE);
            } else {
                mixedMatchDT.setHasFirstPlayerWonProp(Boolean.FALSE);
            }
            return mixedMatchDT;
        }

        return new MixedMatchDTO(playerDTOs.get(0), playerDTOs.get(1), playerDTOs.get(2), playerDTOs.get(3), sets);
    }

    private boolean containsWalkover(String[] resultSplit) {
        return Arrays.stream(resultSplit).anyMatch(s -> s.equalsIgnoreCase(WALKOVER_MARKER) || s.equalsIgnoreCase(WALKOVER_MARKER_LOST));
    }

    private String[] splitRawData(HtmlDivision inputDiv) {
        final String SEPARATOR = "\n";
        return inputDiv.asNormalizedText().split(SEPARATOR);
    }

    // Helper function to eliminate repetitive PlayerDTO creation logic
    private List<PlayerDTO> createPlayerDTOsFromResult(String[] resultSplit) {
        return Arrays.stream(resultSplit)
                .filter(this::isValidPlayerString)
                .map(PlayerDTO::new)
                .collect(Collectors.toList());
    }

    private List<SetDTO> prepareSets(List<Integer> pointsList) {
        final int numberOfSets = pointsList.size() / 2;
        final int POINTS_PER_SET = 2; // Each set contains two points
        List<SetDTO> setDTOs = new ArrayList<>();

        if (pointsList.size() == numberOfSets * POINTS_PER_SET) {
            for (int i = 0; i < numberOfSets; i++) {
                SetDTO set = new SetDTO(SetNo.values()[i], pointsList.get(i * 2), pointsList.get(i * 2 + 1));
                setDTOs.add(set);
            }
        }
        return setDTOs;
    }

    private boolean isValidPlayerString(String str) {
        return !str.equalsIgnoreCase(WINNER_MARKER) && !str.equalsIgnoreCase(LOSER_MARKER);
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

