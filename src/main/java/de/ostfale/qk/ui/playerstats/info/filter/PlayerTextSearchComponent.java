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
        TextFields.bindAutoCompletion(searchField, autoCompleteHandler.createSuggestionProvider());
    }

    private static class PlayerAutoCompleteHandler {
        private final List<PlayerInfoDTO> playerList = new ArrayList<>();

        public PlayerAutoCompleteHandler(PlayerInfoService playerInfoService) {
            playerList.addAll(playerInfoService.getPlayerInfoList());
        }

        public Callback<AutoCompletionBinding.ISuggestionRequest, Collection<PlayerInfoDTO>> createSuggestionProvider() {
            return request -> {
                String searchText = request.getUserText().toLowerCase();

                if (searchText.isEmpty()) {
                    return List.of();
                }

                return filterPlayersByName(playerList, searchText);
            };
        }

        private Collection<PlayerInfoDTO> filterPlayersByName(Collection<PlayerInfoDTO> players, String searchText) {
            return players.stream()
                    .filter(player -> player.getPlayerInfoMasterDataDTO().getPlayerName().toLowerCase().contains(searchText))
                    .toList();
        }
    }
}
