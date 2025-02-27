package de.ostfale.qk.ui.statistics;

import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.db.service.PlayerServiceProvider;
import de.ostfale.qk.ui.statistics.model.PlayerInfoStatisticsDTO;
import de.ostfale.qk.ui.statistics.model.SearchableYears;
import io.quarkiverse.fx.views.FxView;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Dependent
@FxView("player-stat-info")
public class PlayerInfoStatisticsController {

    private static final Logger log = Logger.getLogger(PlayerInfoStatisticsController.class);

    private List<Player> players = new ArrayList<>();

    @Inject
    PlayerServiceProvider playerServiceProvider;

    @FXML
    GridPane gpPlayerInfo;

    // filter
    @FXML
    private ComboBox<String> cbPlayer;

    @FXML
    private CheckComboBox<SearchableYears> ccbYear;

    @FXML
    private CustomTextField ctfSearchPlayer;


    // player general info
    @FXML
    private Label lblName;

    @FXML
    private Label lblPlayerId;

    @FXML
    private Label lblAgeClass;

    @FXML
    private Label lblBirthYear;

    @FXML
    public void initialize() {
        log.info("Initialize PlayerInfoStatisticsController");
        ccbYear.getItems().addAll(FXCollections.observableArrayList(SearchableYears.values()));
        initPlayerComboBox();
        initSearchPlayerTextField();
    }

    private void initSearchPlayerTextField() {
        if (players.isEmpty()) {
            players = playerServiceProvider.getAllPlayers();
            log.infof("Initialized  %d players", players.size());
        }

        Callback<AutoCompletionBinding.ISuggestionRequest, Collection<Player>> suggestionProvider =
                request -> players.stream()
                        .filter(suggestion -> suggestion.getName().contains(request.getUserText().toLowerCase()))
                        .toList();

        TextFields.bindAutoCompletion(ctfSearchPlayer, suggestionProvider);
    }

    private void initPlayerComboBox() {
        cbPlayer.getItems().addAll(createPlayersList());
        cbPlayer.setOnAction(this::handlePlayerSelection);

    }

    private void handlePlayerSelection(ActionEvent event) {
        log.infof("Player selected: %s", cbPlayer.getValue());
        var playerInfo = playerServiceProvider.getPlayerInfoStatisticsDTO(cbPlayer.getValue());
        updatePlayerInfo(playerInfo);
    }

    private void updatePlayerInfo(PlayerInfoStatisticsDTO playerInfo) {
        log.debugf("Update player info: %s", playerInfo);
        lblName.setText(playerInfo.getPlayerName());
        lblPlayerId.setText(playerInfo.getPlayerId());
        lblBirthYear.setText(playerInfo.getBirthYear());
        lblAgeClass.setText(playerInfo.getAgeClass());
    }

    // TODO replace with dynamic access of favorite players
    private ObservableList<String> createPlayersList() {
        return FXCollections.observableArrayList(
                "Louis Sauerbrei",
                "Tony Chengxi Wang",
                "Victoria Braun",
                "Emily Bischoff",
                "Frederik Volkert"
        );
    }
}
