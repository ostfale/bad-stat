package de.ostfale.qk.data.player.model;

import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.domain.player.PlayerTournamentId;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import de.ostfale.qk.ui.playerstats.info.tournamentdata.PlayerTourStatDTO;
import io.quarkus.logging.Log;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class FavPlayerListData {

    private final Set<FavPlayerData> favoritePlayers = new HashSet<>();

    public Set<FavPlayerData> getFavoritePlayers() {
        return favoritePlayers;
    }

    public void addFavoritePlayer(PlayerInfoDTO playerInfoDTO) {
        Log.debugf("PlayerCustomDataListHandler :: addPlayerCustomData(%s)", playerInfoDTO.getPlayerInfoMasterDataDTO().getPlayerName());
        var favPlayerData = fromPlayerInfoDTO(playerInfoDTO);
        favoritePlayers.add(favPlayerData);
    }

    public void removeFavoritePlayer(PlayerInfoDTO playerInfoDTO) {
        Log.debugf("PlayerCustomDataListHandler :: removePlayerCustomData(%s)", playerInfoDTO.getPlayerInfoMasterDataDTO().getPlayerName());
        var favPlayerData = fromPlayerInfoDTO(playerInfoDTO);
        favoritePlayers.remove(favPlayerData);
    }

    public FavPlayerData getFavPlayerDataByPlayerName(String name) {
        return favoritePlayers.stream().filter(favPlayerData -> favPlayerData.playerName().equals(name)).findFirst().orElse(null);
    }

    private FavPlayerData fromPlayerInfoDTO(PlayerInfoDTO playerInfoDTO) {
        var masterData = playerInfoDTO.getPlayerInfoMasterDataDTO();
        Log.debugf("FavPlayerListData :: map fromPlayerInfoDTO(%s)", masterData.getPlayerName());

        var favPlayerData = new FavPlayerData(
                new PlayerId(masterData.getPlayerId()),
                new PlayerTournamentId(masterData.getPlayerTournamentId()),
                masterData.getPlayerName()
        );

        var tourStats = playerInfoDTO.getPlayerTourStatDTO();
        int currentYear = OffsetDateTime.now().getYear();
        final int YEARS_TO_PROCESS = 4;

        IntStream.range(0, YEARS_TO_PROCESS)
                .forEach(yearOffset -> addYearStatistics(
                        favPlayerData,
                        currentYear - yearOffset,
                        getPlayedTournamentsForYear(tourStats, yearOffset),
                        getDownloadedTournamentsForYear(tourStats, yearOffset)
                ));

        return favPlayerData;
    }

    private void addYearStatistics(FavPlayerData playerData, int year, int playedTournaments, int downloadedTournaments) {
        playerData.addYearStat(new FavPlayerYearStat(year, playedTournaments, downloadedTournaments));
    }

    private int getPlayedTournamentsForYear(PlayerTourStatDTO stats, int yearOffset) {
        return switch (yearOffset) {
            case 0 -> stats.getYearPlayedTournaments();
            case 1 -> stats.getYearMinusOnePlayedTournaments();
            case 2 -> stats.getYearMinusTwoPlayedTournaments();
            case 3 -> stats.getYearMinusThreePlayedTournaments();
            default -> throw new IllegalArgumentException("Unsupported year offset: " + yearOffset);
        };
    }

    private int getDownloadedTournamentsForYear(PlayerTourStatDTO stats, int yearOffset) {
        return switch (yearOffset) {
            case 0 -> stats.getYearDownloadedTournaments();
            case 1 -> stats.getYearMinusOneDownloadedTournaments();
            case 2 -> stats.getYearMinusTwoDownloadedTournaments();
            case 3 -> stats.getYearMinusThreeDownloadedTournaments();
            default -> throw new IllegalArgumentException("Unsupported year offset: " + yearOffset);
        };
    }
}



