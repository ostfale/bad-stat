package de.ostfale.qk.parser.discipline;

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
import java.util.List;

@Singleton
public class DisciplineParserService implements DisciplineParser {
    @Inject
    HtmlParser htmlParser;

    @Inject
    MatchParser matchParser;

    @Inject
    DisciplineAgeParserService ageParserService;

    @Override
    public List<DisciplineParserModel> parseDisciplines(HtmlElement moduleCard) throws HtmlParserException {
        Log.debug("Parsing disciplines for tournament");
        List<DisciplineParserModel> disciplineList = new ArrayList<>();

        try {
            // read all discipline header starting with
            List<HtmlElement> disciplineHeaderElements = htmlParser.getAllDisciplineInfos(moduleCard);
            for (HtmlElement disciplineHeaderElement : disciplineHeaderElements) {
                // read discipline type and age class
                DisciplineParserModel disciplineDTO = ageParserService.parseDisciplineInfos(disciplineHeaderElement.asNormalizedText());
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
}
