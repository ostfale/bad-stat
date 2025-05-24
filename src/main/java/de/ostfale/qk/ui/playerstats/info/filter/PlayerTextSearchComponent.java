package de.ostfale.qk.ui.playerstats.info.filter;

import de.ostfale.qk.ui.playerstats.info.PlayerInfoService;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
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
    private final static List<PlayerInfoDTO> playerList = new ArrayList<>();

    public PlayerTextSearchComponent(PlayerInfoService aPlayerInfoService, TextField searchField) {
        log.debug("Initialize PlayerTextSearchComponent");
        this.searchField = searchField;
        this.autoCompleteHandler = new PlayerAutoCompleteHandler(aPlayerInfoService);
        initialize();
    }

    public void initialize() {
        searchField.setPromptText(PROMPT_TEXT);
        refreshPlayerData();
    }

    public void refreshPlayerData() {
        TextFields.bindAutoCompletion(searchField, autoCompleteHandler.createSuggestionProvider());
    }

    private static class PlayerAutoCompleteHandler {

        private final PlayerInfoService playerInfoService;

        public PlayerAutoCompleteHandler(PlayerInfoService playerInfoService) {
            playerList.addAll(playerInfoService.getPlayerInfoList());
            this.playerInfoService = playerInfoService;
        }

        public Callback<AutoCompletionBinding.ISuggestionRequest, Collection<PlayerInfoDTO>> createSuggestionProvider() {
            return request -> {
                String searchText = request.getUserText().toLowerCase();

                if (searchText.isEmpty()) {
                    return List.of();
                }

                return filterPlayersByName(searchText);
            };
        }

        private Collection<PlayerInfoDTO> filterPlayersByName(String searchText) {
            if (playerList.isEmpty()) {
                playerList.addAll(playerInfoService.getPlayerInfoList());
            }

            return playerList.stream()
                    .filter(player -> player.getPlayerInfoMasterDataDTO().getPlayerName().toLowerCase().contains(searchText))
                    .toList();
        }
    }
}
