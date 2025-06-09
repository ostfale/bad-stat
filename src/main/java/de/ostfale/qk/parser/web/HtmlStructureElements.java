package de.ostfale.qk.parser.web;

public interface HtmlStructureElements {

    String TOURNAMENT_MODULE_CARD = "//div[contains(@class, 'module module--card')]";  // complete tournament

    // tournament header information
    String TOURNAMENT_TITLE_ELEMENT = ".//h4[contains(@class, 'media__title media__title--medium')]"; // name of the tournament
    String TOURNAMENT_LOC_ORGAN_ELEMENT = ".//small[contains(@class, 'media__subheading')]"; //organisation and location
    String TOURNAMENT_DATE_ELEMENT = ".//small[contains(@class, 'media__subheading media__subheading--muted')]"; // from to date
}

