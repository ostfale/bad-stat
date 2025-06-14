package de.ostfale.qk.parser.web;

public interface HtmlStructureElements {

    // general information for all tournaments for a tab (given year)
    String TOURNAMENT_MODULE_CARD = "//div[contains(@class, 'module module--card')]";  // complete tournament
    String TOURNAMENT_DISCIPLINES = ".//h4[contains(@class, 'module-divider')]"; // Konkurrenz -> discipline and age class
    String TOURNAMENT_DISCIPLINE_OR_GROUP = ".//h5[contains(@class, 'module-divider')]"; // discipline of Group

    
    // tournament header information
    String TOURNAMENT_HEADER_TITLE_ELEMENT = ".//h4[contains(@class, 'media__title media__title--medium')]"; // name of the tournament
    String TOURNAMENT_HEADER_LOC_ORGAN_ELEMENT = ".//small[contains(@class, 'media__subheading')]"; //organisation and location
    String TOURNAMENT_DATE_ELEMENT = ".//small[contains(@class, 'media__subheading media__subheading--muted')]"; // from to date

    // discipline information within a tournament
    final String DISCIPLINES_MATCHES_INFO = ".//h4[contains(@class, 'module-divider')]";

}

