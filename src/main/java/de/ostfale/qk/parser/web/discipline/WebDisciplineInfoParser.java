package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.domain.discipline.*;
import de.ostfale.qk.domain.tournament.Tournament;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.htmlunit.html.HtmlElement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static de.ostfale.qk.domain.discipline.AgeClass.*;
import static de.ostfale.qk.domain.discipline.DisciplineType.*;

@ApplicationScoped
public class WebDisciplineInfoParser {

    private static final String SPLIT_DELIMITER = "\\s+";
    private static final int EXPECTED_DISCIPLINE_PARTS = 3;
    private static final int TOO_LONG_TOKEN_FOR_DISCIPLINE = 5;
    private static final int TOO_SHORT_TOKEN_FOR_DISCIPLINE = 2;
    private static final String[] AGE_GROUP_PREFIXES = {"U", "O"};
    private static final Set<String> AGE_CATEGORY_PREFIXES = Set.of("U", "O");

    public void parseDisciplineInfos(Tournament tournament, HtmlElement disciplineToken) {
        String headerToken = disciplineToken.asNormalizedText();
        Log.infof("WebDisciplineInfoParser :: Parse discipline infos: %s", headerToken);

        // fix input string
        headerToken = headerToken.replace("[SG]", "");  // example: Konkurrenz: U11 [SG] Mädcheneinzel Samstag

        String[] disciplineParts = headerToken.split(SPLIT_DELIMITER);

        if (tokenWithoutSpaces(disciplineParts)) {
            Log.warnf("WebDisciplineInfoParser :: Invalid discipline age format : %s (without spaces)", disciplineToken);
            var ageToken = disciplineParts[1].substring(0, 3);
            var discToken = disciplineParts[1].substring(3);
            var ageAndDisc = createDisciplineParserModel(new DisciplineAndAge(DisciplineType.lookup(discToken), AgeClass.fromString(ageToken)));
            processTournamentDisciplines(tournament, ageAndDisc);
        }

        if (!hasAgeCategoryPrefix(disciplineParts) || containsHyphen(disciplineParts)) {
            Log.warnf("WebDisciplineInfoParser :: Invalid discipline age format : %s (read from map)", disciplineToken);
            var mapSearchResult = getDisciplineAndAgeByToken(headerToken);
            if (mapSearchResult != null) {
                processTournamentDisciplines(tournament, mapSearchResult);
            }
        }

        if (disciplineParts.length >= EXPECTED_DISCIPLINE_PARTS) {
            Log.debugf("WebDisciplineInfoParser :: Parse discipline infos with %d parts ", disciplineParts.length);
            processTournamentDisciplines(tournament, parseStandardDisciplineInfos(disciplineParts));
        }

        if (disciplineParts.length == TOO_SHORT_TOKEN_FOR_DISCIPLINE) {
            Log.warnf("DisciplineAgeParserService :: Invalid discipline age format : %s (too few parts)", disciplineToken);
            var discipline = DisciplineType.lookup(disciplineParts[1]);
            processTournamentDisciplines(tournament, new DisciplineAndAge(discipline, UOX));
        }
    }

    private void processTournamentDisciplines(Tournament tournament, DisciplineAndAge disciplineAndAge) {
        try {
            Discipline discipline = tournament.getDisciplineByType(disciplineAndAge.disciplineType);
            if (discipline instanceof TournamentDiscipline tournamentDiscipline) {
                tournamentDiscipline.setDisciplineAgeClass(disciplineAndAge.ageClass());
                tournamentDiscipline.setDisciplineOrder(tournament.getNextDisciplineOrder());
            }
        } catch (IllegalArgumentException e) {
            Log.warnf("WebDisciplineInfoParser :: Invalid discipline type: %s", disciplineAndAge.disciplineType);
        }
    }

    private DisciplineAndAge parseStandardDisciplineInfos(String[] disciplineParts) {
        Log.debugf("WebDisciplineInfoParser :: Parse standard discipline infos with %S parts ", String.join(" ", disciplineParts));
        if (startsWithAgeGroupPrefix(disciplineParts[2])) {
            var ageToken = AgeClass.fromString(disciplineParts[2]);
            var discToken = DisciplineType.lookup(disciplineParts[1]);
            return new DisciplineAndAge(discToken, ageToken);
        } else {
            var ageToken = AgeClass.fromString(disciplineParts[1]);
            var discToken = DisciplineType.lookup(disciplineParts[2]);
            return new DisciplineAndAge(discToken, ageToken);
        }
    }

    private DisciplineAndAge getDisciplineAndAgeByToken(String token) {
        return IRREGULAR_DISC_AGE.get(token);
    }

    private DisciplineAndAge createDisciplineParserModel(DisciplineAndAge disciplineAge) {
        return new DisciplineAndAge(disciplineAge.disciplineType(), disciplineAge.ageClass());
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

    static final Map<String, DisciplineAndAge> IRREGULAR_DISC_AGE = new HashMap<>() {{
        put("Konkurrenz: JE-U9 A", new DisciplineAndAge(SINGLE, U9));
        put("Konkurrenz: ME-U9 A", new DisciplineAndAge(SINGLE, U9));

        put("Konkurrenz: JE-U11 A", new DisciplineAndAge(SINGLE, U11));
        put("Konkurrenz: ME-U11 A", new DisciplineAndAge(SINGLE, U11));
        put("Konkurrenz: MD-U11 A", new DisciplineAndAge(DOUBLE, U11));
        put("Konkurrenz: JD-U11 A", new DisciplineAndAge(DOUBLE, U11));

        put("Konkurrenz: JE-U13 A", new DisciplineAndAge(SINGLE, U13));
        put("Konkurrenz: ME-U13 A", new DisciplineAndAge(SINGLE, U13));
        put("Konkurrenz: U13B Mädcheneinzel", new DisciplineAndAge(SINGLE, U13));
        put("Konkurrenz: MD-U13 A", new DisciplineAndAge(DOUBLE, U13));
        put("Konkurrenz: JD-U13 A", new DisciplineAndAge(DOUBLE, U13));
        put("Konkurrenz: MX-U13 A", new DisciplineAndAge(MIXED, U13));

        put("Konkurrenz: JE-U15 A", new DisciplineAndAge(SINGLE, U15));
        put("Konkurrenz: U15B Jungeneinzel", new DisciplineAndAge(SINGLE, U15));
        put("Konkurrenz: JD-U15 A", new DisciplineAndAge(DOUBLE, U15));
        put("Konkurrenz: MD-U15 A", new DisciplineAndAge(DOUBLE, U15));

        put("Konkurrenz: HE-U17 A", new DisciplineAndAge(SINGLE, U17));
        put("Konkurrenz: DE-U17 A", new DisciplineAndAge(SINGLE, U17));
        put("Konkurrenz: HD-U17 A", new DisciplineAndAge(DOUBLE, U17));
        put("Konkurrenz: DD-U17 A", new DisciplineAndAge(DOUBLE, U17));
        put("Konkurrenz: MX-U17 A", new DisciplineAndAge(MIXED, U17));

        put("Konkurrenz: HE-U19 A", new DisciplineAndAge(SINGLE, U19));
        put("Konkurrenz: DE-U19 A", new DisciplineAndAge(SINGLE, U19));
        put("Konkurrenz: HD-U19 A", new DisciplineAndAge(DOUBLE, U19));
        put("Konkurrenz: DD-U19 A", new DisciplineAndAge(DOUBLE, U19));
        put("Konkurrenz: MX-U19 A", new DisciplineAndAge(MIXED, U19));

        put("Konkurrenz: HE-U22 A", new DisciplineAndAge(SINGLE, U22));
        put("Konkurrenz: DE-U22 A", new DisciplineAndAge(SINGLE, U22));
        put("Konkurrenz: HD-U22 A", new DisciplineAndAge(DOUBLE, U22));
        put("Konkurrenz: DD-U22 A", new DisciplineAndAge(DOUBLE, U22));
        put("Konkurrenz: MX-U22 A", new DisciplineAndAge(MIXED, U22));

        put("Konkurrenz: Mixed Doubles", new DisciplineAndAge(MIXED, UOX));
        put("Konkurrenz: Men's Singles", new DisciplineAndAge(SINGLE, UOX));
        put("Konkurrenz: Men's Doubles", new DisciplineAndAge(DOUBLE, UOX));
        put("Konkurrenz: Women's Singles", new DisciplineAndAge(SINGLE, UOX));
        put("Konkurrenz: Women's Doubles", new DisciplineAndAge(DOUBLE, UOX));
    }};

    private record DisciplineAndAge(DisciplineType disciplineType, AgeClass ageClass) {
    }
}
