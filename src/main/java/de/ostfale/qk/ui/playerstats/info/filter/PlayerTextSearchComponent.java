package de.ostfale.qk.ui.playerstats.info.filter;

import de.ostfale.qk.ui.playerstats.info.PlayerInfoService;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import io.quarkus.logging.Log;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerTextSearchComponent {

    private static final String PROMPT_TEXT = "Spieler suchen";

    private final TextField searchField;
    private final PlayerAutoCompleteHandler autoCompleteHandler;
    private final static List<PlayerInfoDTO> playerList = new ArrayList<>();

    public PlayerTextSearchComponent(PlayerInfoService aPlayerInfoService, TextField searchField) {
        Log.debug("Initialize PlayerTextSearchComponent");
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

    private record PlayerAutoCompleteHandler(PlayerInfoService playerInfoService) {

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
                    Log.debug("PlayerAutoCompleteHandler :: player list is empty, fetching data from web service");
                    var fetchedPlayers = playerInfoService.getPlayerInfoList();
                    playerList.addAll(fetchedPlayers);
                }

                return playerList.stream()
                        .filter(player -> player.getPlayerInfoMasterDataDTO().getPlayerName().toLowerCase().contains(searchText))
                        .toList();
            }
        }
}
