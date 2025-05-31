package de.ostfale.qk.ui.playerstats.info.favplayer;

import de.ostfale.qk.data.player.FavoritePlayerDataJsonHandler;
import de.ostfale.qk.data.player.model.FavPlayerData;
import de.ostfale.qk.data.player.model.FavPlayerListData;
import de.ostfale.qk.data.player.model.FavPlayerYearStat;
import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.domain.tournament.TournamentMatchesListDTO;
import de.ostfale.qk.ui.playerstats.info.PlayerInfoService;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import io.quarkus.logging.Log;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FavPlayerService {

    @Inject
    FavoritePlayerDataJsonHandler favoritePlayerDataJsonHandler;

    @Inject
    PlayerInfoService playerInfoService;

    private FavPlayerListData favoritePlayerListData;

    @PostConstruct
    public void readFavoritePlayersFromData() {
        Log.info("FavPlayerService :: Read favorite players from data");
        favoritePlayerListData = favoritePlayerDataJsonHandler.readFavoritePlayersList();
    }

    @PreDestroy
    public void writeFavoritePlayersToData() {
        if (favoritePlayerListData != null) {
            Log.info("FavPlayerService :: Persist favorite players");
            favoritePlayerDataJsonHandler.writeFavoritePlayersList(favoritePlayerListData);
        }
    }

    public FavPlayerListData getFavoritePlayerListData() {
        return favoritePlayerListData;
    }

    public void addFavPlayer(PlayerInfoDTO player) {
        Log.debugf("FavPlayerService :: Adding player to favorite list: %s", player);
        favoritePlayerListData.addFavoritePlayer(player);
    }

    public void addFavPlayer(String playerName) {
        PlayerInfoDTO playerInfoDTO = playerInfoService.getPlayerInfosForPlayerName(playerName);
        if (playerInfoDTO != null) {
            addFavPlayer(playerInfoDTO);
        } else {
            Log.warnf("No unique player found with name: %s", playerName);
        }
    }

    public void removeFavoritePlayer(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            Log.warn("Cannot remove favorite player: player name is null or empty");
            return;
        }

        Log.debugf("FavPlayerService :: Removing player from favorite list: %s", playerName);

        boolean removed = favoritePlayerListData.getFavoritePlayers()
                .removeIf(player -> player.playerName().equals(playerName));

        if (!removed) {
            Log.warnf("Player not found in favorites: %s", playerName);
        }
    }

    public void updateDownloadedTournaments(TournamentMatchesListDTO tournamentMatchesListDTO) {
        Log.debugf("FavPlayerService :: Update downloaded tournaments for player %s", tournamentMatchesListDTO.getPlayerName());

        var playerId = new PlayerId(tournamentMatchesListDTO.getPlayerId());
        FavPlayerData favPlayerData = favoritePlayerListData.getFavPlayerDataByPlayerId(playerId);

        if (favPlayerData == null) {
            return;
        }

        YearStatUpdate yearStatUpdate = createYearStatUpdate(tournamentMatchesListDTO);
        updatePlayerYearStatistics(favPlayerData, yearStatUpdate);
    }

    private YearStatUpdate createYearStatUpdate(TournamentMatchesListDTO tournamentMatchesListDTO) {
        int year = Integer.parseInt(tournamentMatchesListDTO.getTournamentYear());
        int loadedMatches = tournamentMatchesListDTO.getTournamentMatchesList().size();
        String playerName = tournamentMatchesListDTO.getPlayerName();

        return new YearStatUpdate(year, loadedMatches, playerName);
    }

    private void updatePlayerYearStatistics(FavPlayerData favPlayerData, YearStatUpdate update) {
        FavPlayerYearStat oldYearStat = favPlayerData.getYearStat(update.year());

        Log.debugf("FavPlayerService :: Updating downloaded tournaments for player %s for year %d",
                update.playerName(), update.year());

        FavPlayerYearStat newYearStat = new FavPlayerYearStat(
                update.year(),
                oldYearStat.played(),
                update.loadedMatches()
        );

        favPlayerData.removeYearStat(oldYearStat);
        favPlayerData.addYearStat(newYearStat);
    }

    private record YearStatUpdate(int year, int loadedMatches, String playerName) {}
}
