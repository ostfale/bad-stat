package de.ostfale.qk.parser.discipline;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.parser.HtmlParser;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.ParsedComponent;
import de.ostfale.qk.parser.discipline.model.DisciplineParserModel;
import de.ostfale.qk.parser.match.api.MatchParser;
import de.ostfale.qk.parser.match.internal.model.DoubleMatchRawModel;
import de.ostfale.qk.parser.match.internal.model.MatchInfoRawModel;
import de.ostfale.qk.parser.match.internal.model.MixedMatchRawModel;
import de.ostfale.qk.parser.match.internal.model.SingleMatchRawModel;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class DisciplineParserService implements DisciplineParser {

    private static final int MINIMUM_DISCIPLINE_PARTS = 2;
    private static final int EXPECTED_DISCIPLINE_PARTS = 3;
    private static final int MAX_AGE_GROUP_LENGTH = 3;
    private static final String[] AGE_GROUP_PREFIXES = {"U", "O"};

    @Inject
    HtmlParser htmlParser;

    @Inject
    MatchParser matchParser;

    @Override
    public List<DisciplineParserModel> parseDisciplines(HtmlElement moduleCard) throws HtmlParserException {
        Log.debug("Parsing disciplines for tournament");
        List<DisciplineParserModel> disciplineList = new ArrayList<>();

        try {
            // read all discipline header starting with
            List<HtmlElement> disciplineHeaderElements = htmlParser.getAllDisciplineInfos(moduleCard);
            for (HtmlElement disciplineHeaderElement : disciplineHeaderElements) {
                // read discipline type and age class
                DisciplineParserModel disciplineDTO = getDisciplineInfos(disciplineHeaderElement);
                disciplineList.add(disciplineDTO);
            }

            var isTreeMode = isTreeMode(moduleCard);
            if (isTreeMode) {
                parseAllTreeMatchesForThisDiscipline(disciplineList, moduleCard);
            } else {
                parseCombinedTreeAndGroupMatchesForThisDiscipline(disciplineList, moduleCard);
            }
            return disciplineList;
        } catch (Exception e) {
            throw new HtmlParserException(ParsedComponent.DISCIPLINE, e);
        }
    }

    private void parseCombinedTreeAndGroupMatchesForThisDiscipline(List<DisciplineParserModel> disciplineList, HtmlElement moduleCard) {
        Log.debug("Tree mode found -> parse only tree matches for this discipline");


    }

    private void parseAllTreeMatchesForThisDiscipline(List<DisciplineParserModel> disciplineList, HtmlElement moduleCard) {
        Log.debug("Group mode found -> parse all tree and group matches for this discipline");

        int disciplineIndex = 0;

        // get a list of container for all matches for a discipline
        List<HtmlElement> disciplineMatchContainerList = htmlParser.getAllDisciplines(moduleCard);

        for (HtmlElement disciplineMatchContainer : disciplineMatchContainerList) {
            var currentDiscipline = disciplineList.get(disciplineIndex).getDiscipline();
            Log.debugf("Parse all matches for discipline {} ", currentDiscipline.name());

            List<HtmlElement> matchContainerList = htmlParser.getAllMatchesForDisciplineContainer(disciplineMatchContainer);
            for (HtmlElement matchContainer : matchContainerList) {

                MatchInfoRawModel matchInfoRawModel = matchParser.parseMatchGroupInfo(matchContainer);
                HtmlElement matchBody = htmlParser.getMatchBodyElement(matchContainer);

                switch (currentDiscipline) {
                    case SINGLE -> {
                        SingleMatchRawModel singleMatch = (SingleMatchRawModel) matchParser.parseSingleMatch(matchBody);
                        singleMatch.setMatchInfoDTO(matchInfoRawModel);
                        disciplineList.get(disciplineIndex).getMatches().add(singleMatch);
                    }
                    case DOUBLE -> {
                        DoubleMatchRawModel doubleMatch = (DoubleMatchRawModel) matchParser.parseDoubleMatch(matchBody);
                        doubleMatch.setMatchInfoDTO(matchInfoRawModel);
                        disciplineList.get(disciplineIndex).getMatches().add(doubleMatch);
                    }
                    case MIXED -> {
                        MixedMatchRawModel mixedMatch = (MixedMatchRawModel) matchParser.parseMixedMatch(matchBody);
                        mixedMatch.setMatchInfoDTO(matchInfoRawModel);
                        disciplineList.get(disciplineIndex).getMatches().add(mixedMatch);
                    }
                    default -> {
                        Log.errorf("Unknown discipline found: {}", currentDiscipline);
                    }
                }
            }
            disciplineIndex++;
        }
    }

    // check if there are less header than match groups -> there is a group phase
    private boolean isTreeMode(HtmlElement moduleCard) {
        List<HtmlElement> disciplineHeaderElements = htmlParser.getAllDisciplineInfos(moduleCard);
        List<HtmlElement> disciplineMode = htmlParser.getDisciplineTreeGroupContainerList(moduleCard);
        if (disciplineMode.size() == disciplineHeaderElements.size()) {
            Log.debug("Found discipline mode TREE -> normal match modus");
            return true;
        } else if (disciplineMode.size() > disciplineHeaderElements.size()) {
            Log.debug("Found a Tree and a Group phase -> additional group phase");
            return false;
        }
        Log.error("Wrong discipline mode detected");
        return false;
    }

    private DisciplineParserModel getDisciplineInfos(HtmlElement headerElement) {
        String[] disciplineParts = headerElement.asNormalizedText().split(" ");

        if (disciplineParts.length < MINIMUM_DISCIPLINE_PARTS) {
            Log.warnf("DisciplineParserService :: Invalid discipline format: %s",
                    String.join(" ", disciplineParts));
            return createDefaultDisciplineModel();
        }
        return parseDisciplineParts(disciplineParts);
    }

    private DisciplineParserModel parseDisciplineParts(String[] parts) {
        String disciplineName;
        String ageGroup = AgeClass.UOX.name();

        if (parts.length >= EXPECTED_DISCIPLINE_PARTS && containsAgeClassMarker(parts)) {
            return parseFullDisciplineFormat(parts);
        } else if (parts.length == MINIMUM_DISCIPLINE_PARTS) {
            return parseShortDisciplineFormat(parts);
        } else {
            Log.warnf("DisciplineParserService :: no info about age class found: %s", String.join(" ", parts));
            disciplineName = parts[1] + " " + parts[2];
            return new DisciplineParserModel(disciplineName, ageGroup);
        }
    }

    private DisciplineParserModel parseFullDisciplineFormat(String[] parts) {
        String disciplineName;
        String ageGroup;

        if (startsWithAgeGroupPrefix(parts[2])) {
            disciplineName = parts[1];
            ageGroup = parts[2];
        } else {
            disciplineName = parts[2];
            ageGroup = parts[1];
        }

        return new DisciplineParserModel(disciplineName, ageGroup);
    }

    private DisciplineParserModel parseShortDisciplineFormat(String[] parts) {
        if (!startsWithAgeGroupPrefix(parts[1])) {
            return new DisciplineParserModel(parts[1], AgeClass.UOX.name());
        }

        String ageGroup = parts[1];
        String disciplineName = parts[1];

        if (ageGroup.length() > MAX_AGE_GROUP_LENGTH) {
            Log.warnf("DisciplineParserService :: discipline age group is longer than 3 chars: %s", ageGroup);
            disciplineName = ageGroup.substring(MAX_AGE_GROUP_LENGTH);
            ageGroup = ageGroup.substring(0, MAX_AGE_GROUP_LENGTH);
        }

        return new DisciplineParserModel(disciplineName, ageGroup);
    }

    private boolean startsWithAgeGroupPrefix(String text) {
        return Arrays.stream(AGE_GROUP_PREFIXES).anyMatch(text::startsWith);
    }

    private DisciplineParserModel createDefaultDisciplineModel() {
        return new DisciplineParserModel("", AgeClass.UOX.name());
    }

    private boolean containsAgeClassMarker(String[] rawData) {
        return Arrays.stream(rawData).anyMatch(rawDataElement -> rawDataElement.startsWith("U") || rawDataElement.startsWith("O"));
    }
}
