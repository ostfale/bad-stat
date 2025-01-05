package de.ostfale.qk.parser.discipline.api;

import de.ostfale.qk.parser.discipline.internal.model.DisciplineDTO;
import org.htmlunit.html.HtmlDivision;

public interface DisciplineParser {

    DisciplineDTO parseDiscipline(HtmlDivision content);
}
