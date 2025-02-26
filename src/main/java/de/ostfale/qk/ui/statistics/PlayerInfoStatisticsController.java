package de.ostfale.qk.ui.statistics;

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
import org.controlsfx.control.CheckComboBox;
import org.jboss.logging.Logger;

@Dependent
@FxView("player-stat-info")
public class PlayerInfoStatisticsController {

    private static final Logger log = Logger.getLogger(PlayerInfoStatisticsController.class);

    @Inject
    PlayerServiceProvider playerServiceProvider;

    @FXML
    GridPane gpPlayerInfo;

    @FXML
    private ComboBox<String> cbPlayer;

    @FXML
    private CheckComboBox<SearchableYears> ccbYear;

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
