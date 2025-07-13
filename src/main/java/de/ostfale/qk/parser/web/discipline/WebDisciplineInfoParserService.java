package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.discipline.DisciplineInfo;
import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.ParsedComponent;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.Set;

import static de.ostfale.qk.domain.discipline.AgeClass.UOX;

@ApplicationScoped
public class WebDisciplineInfoParserService implements WebDisciplineParser {

    private static final String EMPTY_STRING = "";
    private static final String GROUP_STRING = "Gruppe";
    private static final String SPLIT_DELIMITER = "\\s+";
    private static final int EXPECTED_DISCIPLINE_PARTS = 3;
    private static final int TOO_LONG_TOKEN_FOR_DISCIPLINE = 5;
    private static final int TOO_SHORT_TOKEN_FOR_DISCIPLINE = 2;
    private static final int SUBHEADER_DEFAULT_TOKEN_NUMBER = 2;
    private static final String[] AGE_GROUP_PREFIXES = {"U", "O"};
    private static final Set<String> AGE_CATEGORY_PREFIXES = Set.of("U", "O");

    public DisciplineInfo extractDisciplineHeaderInfo(String disciplineInfoString) throws HtmlParserException {
        Log.infof("WebDisciplineInfoParser :: Parse discipline infos: %s", disciplineInfoString);
        String[] splitToken = fixAndSplitHeaderToken(disciplineInfoString);

        var result = checkTokenAgainstMap(disciplineInfoString);
        if (result != null) {
            Log.infof("WebDisciplineInfoParser :: Found discipline info in map: %s", result);
            return result;
        }

        if (splitToken.length == TOO_SHORT_TOKEN_FOR_DISCIPLINE) {
            Log.warnf("DisciplineAgeParserService :: Invalid discipline age format : %s (too few parts)", splitToken.length);
            var discipline = DisciplineType.lookup(splitToken[1]);
            if (discipline != null) {
                return new DisciplineInfo(disciplineInfoString, UOX, discipline);
            }
        }

        if (splitToken.length >= EXPECTED_DISCIPLINE_PARTS && !containsHyphen(splitToken)) {
            Log.debugf("WebDisciplineInfoParser :: Parse discipline infos with %d parts ", splitToken.length);
            DisciplineAndAge disciplineAndAge = parseStandardDisciplineInfos(splitToken[1], splitToken[2]);
            return new DisciplineInfo(disciplineInfoString, disciplineAndAge.ageClass(), disciplineAndAge.disciplineType());
        }
        var errorMessage = String.format("Invalid discipline age format : %s (read from string)", disciplineInfoString);
        throw new HtmlParserException(ParsedComponent.DISCIPLINE_INFO, errorMessage);
    }

    public DisciplineInfo extractDisciplineSubHeaderInfo(String disciplineSubstring) throws HtmlParserException {
        Log.infof("WebDisciplineInfoParser :: Parse discipline header format: %s", disciplineSubstring);

        if (disciplineSubstring.startsWith(GROUP_STRING)) {
            Log.debugf("WebDisciplineInfoParser :: Parse discipline infos with group string: %s", disciplineSubstring);
            return new DisciplineInfo(disciplineSubstring);
        }

        String[] splitToken = disciplineSubstring.split(SPLIT_DELIMITER);

        var result = checkTokenAgainstSubHeaderMap(disciplineSubstring);
        if (result != null) {
            Log.infof("WebDisciplineInfoParser :: Found discipline info in map: %s", result);
            return result;
        }

        if (splitToken.length >= SUBHEADER_DEFAULT_TOKEN_NUMBER) {
            var disciplineAndAge = parseStandardDisciplineInfos(splitToken[0], splitToken[1]);
            return new DisciplineInfo(disciplineSubstring, disciplineAndAge.ageClass(), disciplineAndAge.disciplineType());
        }


        var errorMessage = String.format("Invalid discipline subheader format : %s", disciplineSubstring);
        throw new HtmlParserException(ParsedComponent.DISCIPLINE_SUB_INFO, errorMessage);

    }

    private String[] fixAndSplitHeaderToken(String headerToken) {
        var result = headerToken.replace("[SG]", EMPTY_STRING);
        return result.split(SPLIT_DELIMITER);
    }

    private DisciplineAndAge parseStandardDisciplineInfos(String firstDisciplinePart, String secondDisciplinePart) {
        Log.debugf("WebDisciplineInfoParser :: Parse standard discipline infos with %S parts ", firstDisciplinePart + " " + secondDisciplinePart);
        if (startsWithAgeGroupPrefix(secondDisciplinePart)) {
            var ageToken = AgeClass.fromString(secondDisciplinePart);
            var discToken = DisciplineType.lookup(firstDisciplinePart);
            return new DisciplineAndAge(discToken, ageToken);
        } else {
            var ageToken = AgeClass.fromString(firstDisciplinePart);
            var discToken = DisciplineType.lookup(secondDisciplinePart);
            return new DisciplineAndAge(discToken, ageToken);
        }
    }

    private DisciplineInfo checkTokenAgainstSubHeaderMap(String token) {
        var result = DISCIPLINE_SUB_HEADER_MAPPING.get(token);
        if (result != null) {
            return new DisciplineInfo(token, result.ageClass(), result.disciplineType());
        }
        return null;
    }

    private DisciplineInfo checkTokenAgainstMap(String token) {
        String fixedToken = token.replace("Konkurrenz:", EMPTY_STRING).trim();
        var foundMapping = DISCIPLINE_SUB_HEADER_MAPPING.get(fixedToken);
        if (foundMapping != null) {
            return new DisciplineInfo(fixedToken, foundMapping.ageClass(), foundMapping.disciplineType());
        }
        return null;
    }

    private boolean startsWithAgeGroupPrefix(String ageToken) {
        return Arrays.stream(AGE_GROUP_PREFIXES).anyMatch(ageToken::startsWith);
    }

    private boolean hasAgeCategoryPrefix(String[] tokens) {
        return Arrays.stream(tokens)
                .anyMatch(token -> AGE_CATEGORY_PREFIXES.stream()
                        .anyMatch(token::startsWith));
    }

    private boolean containsHyphen(String[] tokens) {
        return Arrays.stream(tokens).anyMatch(token -> token.contains("-"));
    }

    private boolean tokenWithoutSpaces(String[] tokens) {
        return (tokens[1].length() == TOO_LONG_TOKEN_FOR_DISCIPLINE && hasAgeCategoryPrefix(tokens));
    }

    private record DisciplineAndAge(DisciplineType disciplineType, AgeClass ageClass) {
    }
}
