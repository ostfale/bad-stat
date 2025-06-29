package de.ostfale.qk.parser.web;

public interface BaseParser {

    HtmlStructureParser htmlStructureParser = new HtmlStructureParser();

    default boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.trim().matches("\\d+");
    }
}
