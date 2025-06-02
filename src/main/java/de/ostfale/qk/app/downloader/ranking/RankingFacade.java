package de.ostfale.qk.app.downloader.ranking;

import de.ostfale.qk.app.FileSystemFacade;
import de.ostfale.qk.app.TimeHandlerFacade;
import io.quarkus.logging.Log;

public interface RankingFacade extends FileSystemFacade, TimeHandlerFacade {

    String CURRENT_RANKING_FILE_URL = "https://turniere.badminton.de/ranking/download?save=1&gender=&gruppe=&lvname=&bezirk=&firstname=&lastname=&club=&colortype=";
    String RANKING_URL = "https://turniere.badminton.de/uploads/ranking/";


    String FILENAME_PREFIX_SEP = "_";
    String FILENAME_EXTENSION_SEP = ".";
    String FILE_NAME = "Ranking_";
    String FILE_SUFFIX = ".xlsx";
    String FILE_NAME_CW = "_KW";

    default String prepareRankingFileName(int calendarWeek, int calendarYear) {
        String rFileName = FILE_NAME + calendarYear + FILE_NAME_CW + calendarWeek + FILE_SUFFIX;
        Log.debugf("RankingFacade :: Prepare ranking file name %s", rFileName);
        return rFileName;
    }

    default String createURLForCalendarWeek(int calendarWeek, int calendarYear) {
        String targetURL = RANKING_URL + FILE_NAME + calendarYear + FILE_NAME_CW + calendarWeek + FILE_SUFFIX;
        Log.debugf("RankingFile Download :: Created URL: %s", targetURL);
        return targetURL;
    }

    default String getCalenderWeekFromRankingFileName(String rankingFileName) {
        Log.debugf("RankingFacade :: Get calendar week from ranking file name %s", rankingFileName);
        int startOfCalendarWeek = rankingFileName.lastIndexOf(FILENAME_PREFIX_SEP) + 1;
        int endOfCalendarWeek = rankingFileName.lastIndexOf(FILENAME_EXTENSION_SEP);
        return rankingFileName.substring(startOfCalendarWeek, endOfCalendarWeek);
    }

    default String getCalendarYearFromRankingFileName(String rankingFileName) {
        Log.debugf("RankingFacade :: Get calendar year from ranking file name %s", rankingFileName);
        int startOfCalendarYear = rankingFileName.indexOf(FILENAME_PREFIX_SEP) + 1;
        int endOfCalendarYear = rankingFileName.lastIndexOf(FILENAME_PREFIX_SEP);
        return rankingFileName.substring(startOfCalendarYear, endOfCalendarYear);
    }
}
