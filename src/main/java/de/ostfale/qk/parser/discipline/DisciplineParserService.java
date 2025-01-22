package de.ostfale.qk.parser.discipline;

import de.ostfale.qk.parser.HtmlParser;
import de.ostfale.qk.parser.discipline.api.DisciplineParser;
import de.ostfale.qk.parser.discipline.internal.model.DisciplineDTO;
import de.ostfale.qk.parser.match.api.MatchParser;
import de.ostfale.qk.parser.match.internal.model.DoubleMatchDTO;
import de.ostfale.qk.parser.match.internal.model.MatchInfoDTO;
import de.ostfale.qk.parser.match.internal.model.MixedMatchDTO;
import de.ostfale.qk.parser.match.internal.model.SingleMatchDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class DisciplineParserService implements DisciplineParser {

    @Inject
    HtmlParser htmlParser;

    @Inject
    MatchParser matchParser;

    private static final Logger log = LoggerFactory.getLogger(DisciplineParserService.class);

    @Override
    public List<DisciplineDTO> parseDisciplines(HtmlElement moduleCard) {
        log.debug("Parsing disciplines for tournament");
        List<DisciplineDTO> disciplineList = new ArrayList<>();

        // read all discipline header starting with
        List<HtmlElement> disciplineHeaderElements = htmlParser.getAllDisciplineInfos(moduleCard);
        for (HtmlElement disciplineHeaderElement : disciplineHeaderElements) {
            // read discipline type and age class
            DisciplineDTO disciplineDTO = getDisciplineInfos(disciplineHeaderElement);
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

    private void parseCombinedTreeAndGroupMatchesForThisDiscipline(List<DisciplineDTO> disciplineList, HtmlElement moduleCard) {
        log.debug("Tree mode found -> parse only tree matches for this discipline");


    }

    private void parseAllTreeMatchesForThisDiscipline(List<DisciplineDTO> disciplineList, HtmlElement moduleCard) {
        log.debug("Group mode found -> parse all tree and group matches for this discipline");

        int disciplineIndex = 0;

        // get a list of container for all matches for a discipline
        List<HtmlElement> disciplineMatchContainerList = htmlParser.getAllDisciplines(moduleCard);

        for (HtmlElement disciplineMatchContainer : disciplineMatchContainerList) {
            var currentDiscipline = disciplineList.get(disciplineIndex).getDiscipline();
            log.debug("Parse all matches for discipline {} ", currentDiscipline.name());

            List<HtmlElement> matchContainerList = htmlParser.getAllMatchesForDisciplineContainer(disciplineMatchContainer);
            for (HtmlElement matchContainer : matchContainerList) {

                MatchInfoDTO matchInfoDTO = matchParser.parseMatchGroupInfo(matchContainer);
                HtmlElement matchBody = htmlParser.getMatchBodyElement(matchContainer);

                switch (currentDiscipline) {
                    case SINGLE -> {
                        SingleMatchDTO singleMatch = (SingleMatchDTO) matchParser.parseSingleMatch(matchBody);
                        singleMatch.setMatchInfoDTO(matchInfoDTO);
                        disciplineList.get(disciplineIndex).getMatches().add(singleMatch);
                    }
                    case DOUBLE -> {
                        DoubleMatchDTO doubleMatch = (DoubleMatchDTO) matchParser.parseDoubleMatch(matchBody);
                        doubleMatch.setMatchInfoDTO(matchInfoDTO);
                        disciplineList.get(disciplineIndex).getMatches().add(doubleMatch);
                    }
                    case MIXED -> {
                        MixedMatchDTO mixedMatch = (MixedMatchDTO) matchParser.parseMixedMatch(matchBody);
                        mixedMatch.setMatchInfoDTO(matchInfoDTO);
                        disciplineList.get(disciplineIndex).getMatches().add(mixedMatch);
                    }
                    default -> {
                        log.error("Unknown discipline found: {}", currentDiscipline);
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

    private DisciplineDTO getDisciplineInfos(HtmlElement headerElement) {
        String[] disciplineAge = headerElement.asNormalizedText().split(" ");
        var disciplineName = disciplineAge[1];
        var disciplineAgeGroup = disciplineAge[2];
        var disciplineInfo = new DisciplineDTO(disciplineName, disciplineAgeGroup);
        log.debug("Tournament discipline info: {}", disciplineInfo);
        return disciplineInfo;
    }
}
