package de.ostfale.qk.parser.file;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public interface FileParser {

    String CSV_SEPARATOR = ";";
    String DATE_FORMAT = "dd.MM.yyyy";
    String EMPTY_STRING = "";

    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    int START_DATE_INDEX = 0;
    int END_DATE_INDEX = 1;
    int TOURNAMENT_NAME_INDEX = 2;
    int TOURNAMENT_TYPE_INDEX = 3;
    int TOURNAMENT_ORD_NO_INDEX = 4;
    int COUNTRY_INDEX = 5;
    int LOCATION_INDEX = 6;
    int POSTAL_CODE_INDEX = 7;
    int REGION_INDEX = 8;
    int OPEN_NAME_INDEX = 9;
    int ORGANIZER_INDEX = 10;
    int CATEGORY_INDEX = 11;
    int CLOSE_DATE_INDEX = 12;
    int WEB_URL_INDEX = 13;
    int PDF_URL_INDEX = 14;
    int PDF_AVAILABLE_INDEX = 15;
    int TOUR_CREATION_DATE_INDEX = 16;
    int TOUR_VISIBLE_DATE_INDEX = 17;
    int INVITATION_CREATION_DATE_INDEX = 18;
    int TURNIER_LINK_CREATION_DATE_INDEX = 19;
    int AK_U9_INDEX = 20;
    int AK_U11_INDEX = 21;
    int AK_U13_INDEX = 22;
    int AK_U15_INDEX = 23;
    int AK_U17_INDEX = 24;
    int AK_U19_INDEX = 25;
    int AK_U22_INDEX = 26;
    int AK_O19_INDEX = 27;
    int AK_O35_INDEX = 27;

    default InputStream readFileFromResource(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return Objects.requireNonNull(classLoader.getResourceAsStream(fileName), "file not found! " + fileName);
    }
}
