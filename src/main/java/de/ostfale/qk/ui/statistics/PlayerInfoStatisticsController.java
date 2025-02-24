package de.ostfale.qk.ui.statistics;

import de.ostfale.qk.ui.statistics.model.PlayerInfoStatisticsUIModel;
import de.ostfale.qk.ui.statistics.model.SearchableYears;
import io.quarkiverse.fx.views.FxView;
import jakarta.enterprise.context.Dependent;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.CheckComboBox;
import org.jboss.logging.Logger;

@Dependent
@FxView("player-stat-info")
public class PlayerInfoStatisticsController {

    private static final Logger log = Logger.getLogger(PlayerInfoStatisticsController.class);

    @FXML
    GridPane gpPlayerInfo;

    @FXML
    private ComboBox<PlayerInfoStatisticsUIModel> cbPlayer;

    @FXML
    private CheckComboBox<SearchableYears> ccbYear;

    @FXML
    public void initialize() {
        log.info("Initialize PlayerInfoStatisticsController");
        ccbYear.getItems().addAll(FXCollections.observableArrayList(SearchableYears.values()));
    }
}
