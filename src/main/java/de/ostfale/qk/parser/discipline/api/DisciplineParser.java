package de.ostfale.qk.parser.discipline.api;

import de.ostfale.qk.parser.discipline.internal.model.DisciplineDTO;
import org.htmlunit.html.HtmlElement;

import java.util.List;

public interface DisciplineParser {

    List<DisciplineDTO> parseDisciplines (HtmlElement content);

}
