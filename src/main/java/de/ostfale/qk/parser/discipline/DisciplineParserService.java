package de.ostfale.qk.parser.discipline;

import de.ostfale.qk.parser.HtmlParser;
import de.ostfale.qk.parser.discipline.api.DisciplineParser;
import de.ostfale.qk.parser.discipline.internal.model.DisciplineRawModel;
import de.ostfale.qk.parser.match.api.MatchParser;
import de.ostfale.qk.parser.match.internal.model.DoubleMatchRawModel;
import de.ostfale.qk.parser.match.internal.model.MatchInfoRawModel;
import de.ostfale.qk.parser.match.internal.model.MixedMatchRawModel;
import de.ostfale.qk.parser.match.internal.model.SingleMatchRawModel;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlElement;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class DisciplineParserService implements DisciplineParser {

    @Inject
    HtmlParser htmlParser;

    @Inject
    MatchParser matchParser;

    private static final Logger log = Logger.getLogger(DisciplineParserService.class);

    @Override
    public List<DisciplineRawModel> parseDisciplines(HtmlElement moduleCard) {
        log.debug("Parsing disciplines for tournament");
        List<DisciplineRawModel> disciplineList = new ArrayList<>();

        // read all discipline header starting with
        List<HtmlElement> disciplineHeaderElements = htmlParser.getAllDisciplineInfos(moduleCard);
        for (HtmlElement disciplineHeaderElement : disciplineHeaderElements) {
            // read discipline type and age class
            DisciplineRawModel disciplineDTO = getDisciplineInfos(disciplineHeaderElement);
            disciplineList.add(disciplineDTO);
        }

        var isTreeMode = isTreeMode(moduleCard);
        if (isTreeMode) {
            parseAllTreeMatchesForThisDiscipline(disciplineList, moduleCard);
        } else {
            parseCombinedTreeAndGroupMatchesForThisDiscipline(disciplineList, moduleCard);
        }
        return disciplineList;
    }

    private void parseCombinedTreeAndGroupMatchesForThisDiscipline(List<DisciplineRawModel> disciplineList, HtmlElement moduleCard) {
        log.debug("Tree mode found -> parse only tree matches for this discipline");


    }

    private void parseAllTreeMatchesForThisDiscipline(List<DisciplineRawModel> disciplineList, HtmlElement moduleCard) {
        log.debug("Group mode found -> parse all tree and group matches for this discipline");

        int disciplineIndex = 0;

        // get a list of container for all matches for a discipline
        List<HtmlElement> disciplineMatchContainerList = htmlParser.getAllDisciplines(moduleCard);

        for (HtmlElement disciplineMatchContainer : disciplineMatchContainerList) {
            var currentDiscipline = disciplineList.get(disciplineIndex).getDiscipline();
            log.debugf("Parse all matches for discipline {} ", currentDiscipline.name());

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
                        log.errorf("Unknown discipline found: {}", currentDiscipline);
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
            log.debug("Found discipline mode TREE -> normal match modus");
            return true;
        } else if (disciplineMode.size() > disciplineHeaderElements.size()) {
            log.debug("Found a Tree and a Group phase -> additional group phase");
            return false;
        }
        log.error("Wrong discipline mode detected");
        return false;
    }

    private DisciplineRawModel getDisciplineInfos(HtmlElement headerElement) {
        String[] disciplineAge = headerElement.asNormalizedText().split(" ");

        var disciplineName = "";
        var disciplineAgeGroup = "";
        if (disciplineAge[2].startsWith("U") || disciplineAge[2].startsWith("O")) {
            disciplineName = disciplineAge[1];
            disciplineAgeGroup = disciplineAge[2];
        } else {
            disciplineName = disciplineAge[2];
            disciplineAgeGroup = disciplineAge[1];
        }

        var disciplineInfo = new DisciplineRawModel(disciplineName, disciplineAgeGroup);
        log.debugf("Tournament discipline info: {}", disciplineInfo);
        return disciplineInfo;
    }
}
