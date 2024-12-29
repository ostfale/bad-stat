package de.ostfale.qk.parser.match.internal;

import de.ostfale.qk.parser.match.api.MatchParser;
import de.ostfale.qk.parser.match.internal.model.DoubleMatchDTO;
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

    @Override
    public SingleMatchDTO parseSingleMatch(HtmlDivision content) {
        log.debug("Parsing single match");
        String[] resultSplit = content.asNormalizedText().split("\n");
        var result = extractNumbersFromStrings(List.of(resultSplit));
        var sets = prepareSets(result);

        String firstPlayer = resultSplit[0];
        String secondPlayer = resultSplit[1];
        PlayerDTO firstPlayerDTO = new PlayerDTO(firstPlayer);
        PlayerDTO secondPlayerDTO = new PlayerDTO(secondPlayer);
        var matchDTO = new SingleMatchDTO(firstPlayerDTO, secondPlayerDTO, sets);
        log.debug("Single match parsed and winner is: {}", matchDTO.hasFirstPlayerWon() ? firstPlayer : secondPlayer);
        return matchDTO;
    }

    @Override
    public DoubleMatchDTO parseDoubleMatch(HtmlDivision content) {
        log.debug("Parsing double match");
        String[] resultSplit = content.asNormalizedText().split("\n");
        var result = extractNumbersFromStrings(List.of(resultSplit));
        var sets = prepareSets(result);

        List<PlayerDTO> playerDTOs = createPlayerDTOsFromResult(resultSplit);

        return new DoubleMatchDTO(playerDTOs.get(0), playerDTOs.get(1), playerDTOs.get(2), playerDTOs.get(3), sets);
    }

    // Helper function to eliminate repetitive PlayerDTO creation logic
    private List<PlayerDTO> createPlayerDTOsFromResult(String[] resultSplit) {
        return Arrays.stream(resultSplit)
                .filter(str -> !str.equalsIgnoreCase("W"))
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

