package de.ostfale.qk.parser.discipline;

import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.discipline.model.DisciplineParserModel;
import org.htmlunit.html.HtmlElement;

import java.util.List;

public interface DisciplineParser {

    List<DisciplineParserModel> parseDisciplines (HtmlElement content) throws HtmlParserException;
}
