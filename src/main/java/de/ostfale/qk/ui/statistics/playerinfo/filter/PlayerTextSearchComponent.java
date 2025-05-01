package de.ostfale.qk.ui.statistics.playerinfo.filter;

import de.ostfale.qk.ui.statistics.playerinfo.PlayerInfoService;
import de.ostfale.qk.ui.statistics.playerinfo.masterdata.PlayerInfoDTO;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerTextSearchComponent {
    private static final Logger log = Logger.getLogger(PlayerTextSearchComponent.class);
    private static final String PROMPT_TEXT = "Spieler suchen";

    private final TextField searchField;
    private final PlayerAutoCompleteHandler autoCompleteHandler;

    public PlayerTextSearchComponent(PlayerInfoService playerInfoService, TextField searchField) {
        log.debug("Initialize PlayerTextSearchComponent");
        this.searchField = searchField;
        this.autoCompleteHandler = new PlayerAutoCompleteHandler(playerInfoService);
        initialize();
    }

    public void initialize() {
        searchField.setPromptText(PROMPT_TEXT);
        refreshPlayerData();
    }

    public void refreshPlayerData() {
        autoCompleteHandler.refreshPlayerList();
        TextFields.bindAutoCompletion(searchField, autoCompleteHandler.createSuggestionProvider());
    }

    private static class PlayerAutoCompleteHandler {
        private final PlayerInfoService playerInfoService;
        private final List<PlayerInfoDTO> playerList = new ArrayList<>();

        public PlayerAutoCompleteHandler(PlayerInfoService playerInfoService) {
            this.playerInfoService = playerInfoService;
        }

        public void refreshPlayerList() {
            List<PlayerInfoDTO> allPlayers = playerInfoService.getPlayerInfoList();
            log.debugf("Update player list with %d players", allPlayers.size());
            playerList.clear();
            playerList.addAll(allPlayers);
        }


        public Callback<AutoCompletionBinding.ISuggestionRequest, Collection<PlayerInfoDTO>> createSuggestionProvider() {
            return request -> {
                String searchText = request.getUserText().toLowerCase();

                if (searchText.isEmpty()) {
                    return List.of();
                }

                if (playerList.isEmpty()) {
                    refreshPlayerList();
                }

                return filterPlayersByName(playerList, searchText);
            };
        }

        private Collection<PlayerInfoDTO> filterPlayersByName(Collection<PlayerInfoDTO> players, String searchText) {
            return players.stream()
                    .filter(player -> player.getPlayerName().toLowerCase().contains(searchText))
                    .toList();
        }
    }
}
