package de.ostfale.qk.ui.statistics;

import jakarta.enterprise.context.ApplicationScoped;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PlayerTournamentsStatisticsTreeTableController {

    private static final Logger log = Logger.getLogger(PlayerTournamentsStatisticsTreeTableController.class);

    private final TreeTableView<PlayerTournamentsStatisticsModel> plStatTreeView;
    private final TreeItem<PlayerTournamentsStatisticsModel> root;

    // declare tree table columns
    TreeTableColumn<PlayerTournamentsStatisticsModel, String> colTournamentDate;
    TreeTableColumn<PlayerTournamentsStatisticsModel, String> colTournamentName;
    TreeTableColumn<PlayerTournamentsStatisticsModel, String> colTournamentLocation;

    public PlayerTournamentsStatisticsTreeTableController() {
        log.debug("Init PlayerTournamentsStatisticsTreeTableController");
        this.plStatTreeView = new TreeTableView<>();
        this.root = createTreeItemRoot();
        plStatTreeView.setShowRoot(false);
        plStatTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        initTreeTableView();
    }

    private void initTreeTableView() {
        colTournamentDate = new TreeTableColumn<>("Datum");
        colTournamentDate.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getTournamentDate()));

        colTournamentName = new TreeTableColumn<>("Name");
        colTournamentName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getTournamentName()));

        colTournamentLocation = new TreeTableColumn<>("Ort");
        colTournamentLocation.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getTournamentLocation()));

        plStatTreeView.getColumns().addAll(colTournamentDate, colTournamentName, colTournamentLocation);
        plStatTreeView.setRoot(root);
    }

    public TreeTableView<PlayerTournamentsStatisticsModel> getPlStatTreeView() {
        return plStatTreeView;
    }

    private TreeItem<PlayerTournamentsStatisticsModel> createTreeItemRoot() {
        var rOpsRoot = new PlayerTournamentsStatisticsModel();
        return new TreeItem<>(rOpsRoot);
    }

}
