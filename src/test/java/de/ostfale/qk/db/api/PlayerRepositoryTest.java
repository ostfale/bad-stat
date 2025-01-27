package de.ostfale.qk.db.api;

import de.ostfale.qk.db.internal.PlayerEntity;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class PlayerRepositoryTest {

    @InjectMock
    PlayerRepository playerRepository;

    @Test
    @DisplayName("Test empty repository")
    void testEmptyRepository() {
        assertEquals(0, playerRepository.count());
    }


    @Test
    void testFindByPlayerId_PlayerExists() {
        // given
        PlayerEntity player = createPlayerEntity();

        // when
        when(playerRepository.findByPlayerId("player123")).thenReturn(player);

        PlayerEntity result = playerRepository.findByPlayerId("player123");

        // then
        assertNotNull(result);
        assertEquals("player123", result.getPlayerId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(1990, result.getYearOfBirth());

        verify(playerRepository, times(1)).findByPlayerId("player123");
    }

    @Test
    void testFindByPlayerId_PlayerDoesNotExist() {
        PlayerRepository mockRepository = mock(PlayerRepository.class);

        when(mockRepository.findByPlayerId("invalidId")).thenReturn(null);

        PlayerEntity result = mockRepository.findByPlayerId("invalidId");

        assertNull(result);

        verify(mockRepository, times(1)).findByPlayerId("invalidId");
    }

    @Test
    void testFindByPlayerId_NullId() {
        PlayerRepository mockRepository = mock(PlayerRepository.class);

        when(mockRepository.findByPlayerId(null)).thenReturn(null);

        PlayerEntity result = mockRepository.findByPlayerId(null);

        assertNull(result);

        verify(mockRepository, times(1)).findByPlayerId(null);
    }

    private PlayerEntity createPlayerEntity() {
        PlayerEntity player = new PlayerEntity();
        player.setId(1L);
        player.setPlayerId("player123");
        player.setFirstName("John");
        player.setLastName("Doe");
        player.setYearOfBirth(1990);
        return player;
    }
}
