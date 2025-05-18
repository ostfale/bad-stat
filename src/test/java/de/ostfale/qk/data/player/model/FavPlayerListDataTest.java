package de.ostfale.qk.data.player.model;

import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoMasterDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test FavPlayerListData record initialization")
@Tag("unittest")
class FavPlayerListDataTest {

    @Test
    void shouldAddFavoritePlayerSuccessfully() {
        // Given
        FavPlayerListData favPlayerListData = new FavPlayerListData();

        PlayerInfoMasterDTO playerInfoMasterDTO = Mockito.mock(PlayerInfoMasterDTO.class);
        Mockito.when(playerInfoMasterDTO.getPlayerId()).thenReturn("P001");
        Mockito.when(playerInfoMasterDTO.getPlayerName()).thenReturn("John Doe");

        PlayerInfoDTO playerInfoDTO = Mockito.mock(PlayerInfoDTO.class);
        Mockito.when(playerInfoDTO.getPlayerInfoMasterDataDTO()).thenReturn(playerInfoMasterDTO);

        // When
        favPlayerListData.addFavoritePlayer(playerInfoDTO);

        // Then
        Set<FavPlayerData> favoritePlayers = favPlayerListData.getFavoritePlayers();
        assertTrue(favoritePlayers.contains(new FavPlayerData(new PlayerId("P001"), "John Doe")));
    }

    @Test
    void shouldNotAddDuplicateFavoritePlayer() {
        // Given
        FavPlayerListData favPlayerListData = new FavPlayerListData();

        PlayerInfoMasterDTO playerInfoMasterDTO = Mockito.mock(PlayerInfoMasterDTO.class);
        Mockito.when(playerInfoMasterDTO.getPlayerId()).thenReturn("P001");
        Mockito.when(playerInfoMasterDTO.getPlayerName()).thenReturn("John Doe");

        PlayerInfoDTO playerInfoDTO = Mockito.mock(PlayerInfoDTO.class);
        Mockito.when(playerInfoDTO.getPlayerInfoMasterDataDTO()).thenReturn(playerInfoMasterDTO);

        // When
        favPlayerListData.addFavoritePlayer(playerInfoDTO);
        favPlayerListData.addFavoritePlayer(playerInfoDTO);

        // Then
        Set<FavPlayerData> favoritePlayers = favPlayerListData.getFavoritePlayers();
        assertEquals(1, favoritePlayers.size());
        assertTrue(favoritePlayers.contains(new FavPlayerData(new PlayerId("P001"), "John Doe")));
    }

    @Test
    void shouldNotContainPlayerBeforeAdding() {
        // Given
        FavPlayerListData favPlayerListData = new FavPlayerListData();

        PlayerInfoMasterDTO playerInfoMasterDTO = Mockito.mock(PlayerInfoMasterDTO.class);
        Mockito.when(playerInfoMasterDTO.getPlayerId()).thenReturn("P001");
        Mockito.when(playerInfoMasterDTO.getPlayerName()).thenReturn("John Doe");

        PlayerInfoDTO playerInfoDTO = Mockito.mock(PlayerInfoDTO.class);
        Mockito.when(playerInfoDTO.getPlayerInfoMasterDataDTO()).thenReturn(playerInfoMasterDTO);

        // When
        Set<FavPlayerData> favoritePlayers = favPlayerListData.getFavoritePlayers();

        // Then
        assertFalse(favoritePlayers.contains(new FavPlayerData(new PlayerId("P001"), "John Doe")));
    }
}
