package de.ostfale.qk.parser.discipline;

import de.ostfale.qk.parser.discipline.api.DisciplineParser;
import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.discipline.internal.model.DisciplineDTO;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DisciplineParserService  implements DisciplineParser {

    private static final Logger log = LoggerFactory.getLogger(DisciplineParserService.class);

    private static final String DISCIPLINE_MARKER = "Konkurrenz";

    final String DISCIPLINE_INFO = ".//h4[contains(@class, 'module-divider')]";

    @Override
    public DisciplineDTO parseDiscipline(HtmlDivision content) {
        log.debug("Parsing tournament discipline ");
        HtmlElement disciplineInfo = content.getFirstByXPath(DISCIPLINE_INFO);
        String disciplineName = disciplineInfo.asNormalizedText();
        if (disciplineName.contains(DISCIPLINE_MARKER)) {
            log.debug("Found discipline {}", disciplineName);
            String[] disciplineParts = disciplineName.split(DISCIPLINE_MARKER);
        }
        else {
            log.error("Could not find discipline {}", disciplineName);
            throw new RuntimeException("Could not find discipline");
        }

        return null;
    }

    private Discipline findDisciplineByName(String disciplineName) {
        log.debug("Finding discipline for -> {}", disciplineName);
       // if (disciplineName.)
        return null;
    }
}
