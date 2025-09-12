package de.ostfale.qk.parser;

import de.ostfale.qk.domain.tourcal.PlannedTournament;
import de.ostfale.qk.domain.tourcal.TourCategory;
import de.ostfale.qk.domain.tourcal.TournamentType;

public abstract class BaseTournamentTest {

    protected static final String TOURNAMENT_START_DATE = "12.07.2025";
    protected static final String TOURNAMENT_END_DATE = "13.07.2025";
    protected static final String TOURNAMENT_NAME = " C-RLT NRW U13-U19";
    protected static final TournamentType TOURNAMENT_TYPE = TournamentType.Ranking;
    protected static final int TOURNAMENT_ORD_NO = 1;
    protected static final String TOURNAMENT_COUNTRY_CODE = "DE";
    protected static final String TOURNAMENT_LOCATION = "Hamburg";
    protected static final String TOURNAMENT_POSTAL_CODE = "22765";
    protected static final String TOURNAMENT_REGION = "Nordrhein-Westfalen";
    protected static final String TOURNAMENT_OPEN_NAME = "NRW Tour";
    protected static final String TOURNAMENT_ORGANIZER = "HAM - TSG Bergedorf";
    protected static final TourCategory TOURNAMENT_CATEGORY = TourCategory.C2;
    protected static final String TOURNAMENT_CLOSING_DATE = "01.07.2025";
    protected static final String TOURNAMENT_WEBSITE = "https://www.tour-calendar.de/tour/12072025/crlt-nrw-u13-u19";
    protected static final String TOURNAMENT_PDF_URL = "https://www.tour-calendar.de/tour/12072025/crlt-nrw-u13-u19/crlt-nrw-u13-u19.pdf";
    protected static final String TOURNAMENT_PDF_AVAILABLE = "true";
    protected static final String TOURNAMENT_CREATION_DATE = "15.05.2025";
    protected static final String TOURNAMENT_VISIBLE_DATE = "25.05.2025";
    protected static final String TOURNAMENT_INVITATION_CREATION_DATE = "05.07.2025";
    protected static final String TOURNAMENT_LINK_CREATION_DATE = "21.06.2025";
    protected static final String TOURNAMENT_AK_U09 = "Einzel";
    protected static final String TOURNAMENT_AK_U11 = "Einzel/Mixed";
    protected static final String TOURNAMENT_AK_U13 = "";
    protected static final String TOURNAMENT_AK_U15 = "Einzel/Doppel/Mixed";
    protected static final String TOURNAMENT_AK_U17 = "Einzel/Doppel/Mixed";
    protected static final String TOURNAMENT_AK_U19 = "";
    protected static final String TOURNAMENT_AK_U22 = "";
    protected static final String TOURNAMENT_AK_O19 = "Einzel/Doppel/Mixed";
    protected static final String TOURNAMENT_AK_O35 = "";

    protected PlannedTournament createPlannedTournament() {
        return new PlannedTournament(
                TOURNAMENT_START_DATE,
                TOURNAMENT_END_DATE,
                TOURNAMENT_NAME,
                TOURNAMENT_TYPE,
                TOURNAMENT_ORD_NO,
                TOURNAMENT_COUNTRY_CODE,
                TOURNAMENT_LOCATION,
                TOURNAMENT_POSTAL_CODE,
                TOURNAMENT_REGION,
                TOURNAMENT_OPEN_NAME,
                TOURNAMENT_ORGANIZER,
                TOURNAMENT_CATEGORY,
                TOURNAMENT_CLOSING_DATE,
                TOURNAMENT_WEBSITE,
                TOURNAMENT_PDF_URL,
                TOURNAMENT_PDF_AVAILABLE,
                TOURNAMENT_CREATION_DATE,
                TOURNAMENT_VISIBLE_DATE,
                TOURNAMENT_INVITATION_CREATION_DATE,
                TOURNAMENT_LINK_CREATION_DATE,
                TOURNAMENT_AK_U09,
                TOURNAMENT_AK_U11,
                TOURNAMENT_AK_U13,
                TOURNAMENT_AK_U15,
                TOURNAMENT_AK_U17,
                TOURNAMENT_AK_U19,
                TOURNAMENT_AK_U22,
                TOURNAMENT_AK_O19,
                TOURNAMENT_AK_O35
        );
    }
}
