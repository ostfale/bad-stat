package de.ostfale.qk.app;

// @formatter:off
/**
 *  ~/.bad-stat
 *      config
 *      data
 *          dashboard
 *              <dashboardRankingData.json
 *          favPlayer
 *              <favPlayerList.json>
 *              matches
 *                  <06-153250_Emily_Bischoff_2025.json>
 *          favTournaments
 *      logs
 *          <bad-stat.log>
 *      ranking
 *          <Ranking_2025_KW32.xlsx>
 *      tournament
 *          <Tournament_2026_2025-01-25.csv>
 *          <Tournament_2025_2025-01-25.csv>
 */
// @formatter:on

public enum DirTypes {
    CONFIG("config"),
    DATA("data"),
    DATA_DASHBOARD("data/dashboard"),
    DATA_FAV_PLAYER("data/favPlayer"),
    DATA_FAV_PLAYER_MATCHES("data/favPlayer/matches"),
    DATA_FAV_TOURNAMENTS("data/favTournaments"),
    DATA_FAV_TOURNAMENTS_FAVORITES("data/favTournaments/favorites"),
    LOGS("logs"),
    TOURNAMENT("tournament"),
    RANKING("ranking");

    public final String displayName;

    DirTypes(String aName) {
        this.displayName = aName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
