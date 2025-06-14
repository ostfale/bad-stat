package de.ostfale.qk.parser.discipline;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.parser.discipline.model.DisciplineAgeModel;
import de.ostfale.qk.parser.discipline.model.DisciplineParserModel;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static de.ostfale.qk.domain.discipline.AgeClass.*;
import static de.ostfale.qk.domain.discipline.DisciplineType.*;

@ApplicationScoped
public class DisciplineAgeParserService {

    private static final String SPLIT_DELIMITER = "\\s+";
    private static final int EXPECTED_DISCIPLINE_PARTS = 3;
    private static final int TOO_LONG_TOKEN_FOR_DISCIPLINE = 5;
    private static final int TOO_SHORT_TOKEN_FOR_DISCIPLINE = 2;
    private static final String[] AGE_GROUP_PREFIXES = {"U", "O"};
    private static final Set<String> AGE_CATEGORY_PREFIXES = Set.of("U", "O");

    public DisciplineParserModel parseDisciplineInfos(String disciplineToken) {
        Log.infof("DisciplineAgeParserService :: Parse discipline infos: %s", disciplineToken);

        // fix input string
        disciplineToken = disciplineToken.replace("[SG]", "");  // example: Konkurrenz: U11 [SG] Mädcheneinzel Samstag

        String[] disciplineParts = disciplineToken.split(SPLIT_DELIMITER);

        if (tokenWithoutSpaces(disciplineParts)) {
            Log.warnf("DisciplineAgeParserService :: Invalid discipline age format : %s (without spaces)", disciplineToken);
            var ageToken = disciplineParts[1].substring(0, 3);
            var discToken = disciplineParts[1].substring(3);
            return createDisciplineParserModel(new DisciplineAgeModel(DisciplineType.lookup(discToken), AgeClass.fromString(ageToken)));
        }

        if (!hasAgeCategoryPrefix(disciplineParts) || containsHyphen(disciplineParts)) {
            Log.warnf("DisciplineAgeParserService :: Invalid discipline age format : %s (read from map)", disciplineToken);
            var mapSearchResult = getDisciplineAgeModelByToken(disciplineToken);
            if (mapSearchResult != null) {
                return createDisciplineParserModel(mapSearchResult);
            }
        }

        if (disciplineParts.length >= EXPECTED_DISCIPLINE_PARTS) {
            Log.debugf("DisciplineAgeParserService :: Parse discipline infos with %d parts ", disciplineParts.length);
            return createDisciplineModelDefault(disciplineParts);
        }

        if (disciplineParts.length == TOO_SHORT_TOKEN_FOR_DISCIPLINE) {
            Log.warnf("DisciplineAgeParserService :: Invalid discipline age format : %s (too few parts)", disciplineToken);
            var discipline = DisciplineType.lookup(disciplineParts[1]);
            return new DisciplineParserModel(discipline, UOX);
        }

        Log.errorf("DisciplineAgeParserService :: Could not parse discipline infos: %s", disciplineToken);
        return null;
    }

    public DisciplineAgeModel getDisciplineAgeModelByToken(String token) {
        return IRREGULAR_DISC_AGE.get(token);
    }

    private DisciplineParserModel createDisciplineModelDefault(String[] disciplineParts) {
        DisciplineAgeModel disciplineAgeModel = parseStandardDisciplineInfos(disciplineParts);
        return createDisciplineParserModel(disciplineAgeModel);
    }

    private DisciplineParserModel createDisciplineParserModel(DisciplineAgeModel disciplineAge) {
        return new DisciplineParserModel(disciplineAge.disciplineType(), disciplineAge.ageClass());
    }

    private DisciplineAgeModel parseStandardDisciplineInfos(String[] disciplineParts) {
        Log.debugf("DisciplineAgeParserService :: Parse standard discipline infos with %S parts ", String.join(" ", disciplineParts));
        if (startsWithAgeGroupPrefix(disciplineParts[2])) {
            var ageToken = AgeClass.fromString(disciplineParts[2]);
            var discToken = DisciplineType.lookup(disciplineParts[1]);
            return new DisciplineAgeModel(discToken, ageToken);
        } else {
            var ageToken = AgeClass.fromString(disciplineParts[1]);
            var discToken = DisciplineType.lookup(disciplineParts[2]);
            return new DisciplineAgeModel(discToken, ageToken);
        }
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

    static final Map<String, DisciplineAgeModel> IRREGULAR_DISC_AGE = new HashMap<>() {{
        put("Konkurrenz: JE-U9 A", new DisciplineAgeModel(SINGLE, U9));
        put("Konkurrenz: ME-U9 A", new DisciplineAgeModel(SINGLE, U9));

        put("Konkurrenz: JE-U11 A", new DisciplineAgeModel(SINGLE, U11));
        put("Konkurrenz: ME-U11 A", new DisciplineAgeModel(SINGLE, U11));
        put("Konkurrenz: MD-U11 A", new DisciplineAgeModel(DOUBLE, U11));
        put("Konkurrenz: JD-U11 A", new DisciplineAgeModel(DOUBLE, U11));

        put("Konkurrenz: JE-U13 A", new DisciplineAgeModel(SINGLE, U13));
        put("Konkurrenz: ME-U13 A", new DisciplineAgeModel(SINGLE, U13));
        put("Konkurrenz: U13B Mädcheneinzel", new DisciplineAgeModel(SINGLE, U13));
        put("Konkurrenz: MD-U13 A", new DisciplineAgeModel(DOUBLE, U13));
        put("Konkurrenz: JD-U13 A", new DisciplineAgeModel(DOUBLE, U13));
        put("Konkurrenz: MX-U13 A", new DisciplineAgeModel(MIXED, U13));

        put("Konkurrenz: JE-U15 A", new DisciplineAgeModel(SINGLE, U15));
        put("Konkurrenz: U15B Jungeneinzel", new DisciplineAgeModel(SINGLE, U15));
        put("Konkurrenz: JD-U15 A", new DisciplineAgeModel(DOUBLE, U15));
        put("Konkurrenz: MD-U15 A", new DisciplineAgeModel(DOUBLE, U15));

        put("Konkurrenz: HE-U17 A", new DisciplineAgeModel(SINGLE, U17));
        put("Konkurrenz: DE-U17 A", new DisciplineAgeModel(SINGLE, U17));
        put("Konkurrenz: HD-U17 A", new DisciplineAgeModel(DOUBLE, U17));
        put("Konkurrenz: DD-U17 A", new DisciplineAgeModel(DOUBLE, U17));
        put("Konkurrenz: MX-U17 A", new DisciplineAgeModel(MIXED, U17));

        put("Konkurrenz: HE-U19 A", new DisciplineAgeModel(SINGLE, U19));
        put("Konkurrenz: DE-U19 A", new DisciplineAgeModel(SINGLE, U19));
        put("Konkurrenz: HD-U19 A", new DisciplineAgeModel(DOUBLE, U19));
        put("Konkurrenz: DD-U19 A", new DisciplineAgeModel(DOUBLE, U19));
        put("Konkurrenz: MX-U19 A", new DisciplineAgeModel(MIXED, U19));

        put("Konkurrenz: HE-U22 A", new DisciplineAgeModel(SINGLE, U22));
        put("Konkurrenz: DE-U22 A", new DisciplineAgeModel(SINGLE, U22));
        put("Konkurrenz: HD-U22 A", new DisciplineAgeModel(DOUBLE, U22));
        put("Konkurrenz: DD-U22 A", new DisciplineAgeModel(DOUBLE, U22));
        put("Konkurrenz: MX-U22 A", new DisciplineAgeModel(MIXED, U22));

        put("Konkurrenz: Mixed Doubles", new DisciplineAgeModel(MIXED, UOX));
        put("Konkurrenz: Men's Singles", new DisciplineAgeModel(SINGLE, UOX));
        put("Konkurrenz: Men's Doubles", new DisciplineAgeModel(DOUBLE, UOX));
        put("Konkurrenz: Women's Singles", new DisciplineAgeModel(SINGLE, UOX));
        put("Konkurrenz: Women's Doubles", new DisciplineAgeModel(DOUBLE, UOX));
    }};
}
