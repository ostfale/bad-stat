package de.ostfale.qk.parser.discipline;

import de.ostfale.qk.parser.HtmlParser;
import de.ostfale.qk.parser.discipline.api.DisciplineParser;
import de.ostfale.qk.parser.discipline.internal.model.AgeClass;
import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.discipline.internal.model.DisciplineDTO;
import de.ostfale.qk.parser.match.api.MatchParser;
import de.ostfale.qk.parser.match.internal.model.Match;
import de.ostfale.qk.parser.tournament.internal.model.TournamentDisciplineDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlHeading5;
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
    public List<TournamentDisciplineDTO> parseTournamentDisciplines(HtmlElement moduleCard) {
        log.debug("Parsing tournament disciplines for info and matches");
        List<TournamentDisciplineDTO> disciplineList = new ArrayList<>();

        // read all discipline header starting with
        List<HtmlElement> disciplineHeaderElements = htmlParser.getAllDisciplineInfos(moduleCard);
        for (HtmlElement disciplineHeaderElement : disciplineHeaderElements) {
            TournamentDisciplineDTO tournamentDisciplineDTO = getTournamentDisciplineInfoDTO(disciplineHeaderElement);
            disciplineList.add(tournamentDisciplineDTO);
        }
        return disciplineList;
    }

    private TournamentDisciplineDTO getTournamentDisciplineInfoDTO(HtmlElement headerElement) {
        String[] disciplineAge = headerElement.asNormalizedText().split(" ");
        var disciplineName = disciplineAge[1];
        var disciplineAgeGroup = disciplineAge[2];
        var disciplineInfo = new TournamentDisciplineDTO(disciplineName, disciplineAgeGroup);
        log.debug("Tournament discipline info: {}", disciplineInfo);
        return disciplineInfo;
    }


















    private static final String DISCIPLINE_MARKER = "Konkurrenz";
    private static final String DISCIPLINE_SEPARATOR = " ";

    final String DISCIPLINE_INFO = ".//h4[contains(@class, 'module-divider')]";
    final String DISCIPLINE_MODE = ".//h5[contains(@class, 'module-divider')]";
    final String DISCIPLINE_MATCH_GROUP = ".//li[contains(@class, 'match-group__item')]";
    final String DISCIPLINE_MATCH_BODY = ".//div[contains(@class, 'match')]";



    @Override
    public DisciplineDTO parseDiscipline(HtmlDivision content) {
        log.debug("Parsing tournament discipline ");
        HtmlElement disciplineInfo = content.getFirstByXPath(DISCIPLINE_INFO);
        String disciplineName = disciplineInfo.asNormalizedText();
        if (disciplineName.contains(DISCIPLINE_MARKER)) {
            log.debug("Found discipline header: {}", disciplineName);
            String[] disciplineParts = disciplineName.split(DISCIPLINE_SEPARATOR);
            var disciplineString = disciplineParts[1];
            var ageString = disciplineParts[2];
            log.debug("Discipline: {} Age: {}", disciplineString, ageString);

            DisciplineDTO disciplineDTO = new DisciplineDTO(Discipline.fromString(disciplineString), AgeClass.fromString(ageString));

            // check for simple tree or an additional group phase
            List<HtmlHeading5> mode = content.getByXPath(DISCIPLINE_MODE);
            if (mode.size() == 1) {
                log.debug("Found mode: {}", mode.getFirst().asNormalizedText());
                List<HtmlDivision> matchGroup = content.getByXPath(DISCIPLINE_MATCH_BODY);
                log.debug("Found {} match groups", matchGroup.size());
                List<Match> matchResults = matchParser.parseMatchDiscipline(Discipline.fromString(disciplineString), matchGroup);

               // matchGroup.forEach(htmlDivision ->
                      //  disciplineDTO.addTreeMatch(matchParser.parseSingleMatch(htmlDivision)));
            }

            return disciplineDTO;
        } else {
            log.error("Could not find discipline {}", disciplineName);
            throw new RuntimeException("Could not find discipline");
        }
    }



    private Discipline findDisciplineByName(String disciplineName) {
        log.debug("Finding discipline for -> {}", disciplineName);
        // if (disciplineName.)
        return null;
    }
}
