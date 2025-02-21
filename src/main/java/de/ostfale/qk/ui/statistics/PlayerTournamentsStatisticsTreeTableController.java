package de.ostfale.qk.ui.statistics;

import jakarta.enterprise.context.ApplicationScoped;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class PlayerTournamentsStatisticsTreeTableController {

    private static final Logger log = Logger.getLogger(PlayerTournamentsStatisticsTreeTableController.class);

    private final TreeTableView<PlayerMatchStatistics> ttView;
    private final TreeItem<PlayerMatchStatistics> root;

    // declare tree table columns
    TreeTableColumn<PlayerMatchStatistics, String> colTournamentDate;
    TreeTableColumn<PlayerMatchStatistics, String> colTournamentName;
    TreeTableColumn<PlayerMatchStatistics, String> colTournamentLocation;
    TreeTableColumn<PlayerMatchStatistics, String> colDiscipline;
    TreeTableColumn<PlayerMatchStatistics, String> colRoundName;


    public PlayerTournamentsStatisticsTreeTableController() {
        log.debug("Init PlayerTournamentsStatisticsTreeTableController");
        this.root = createTreeItemRoot();
        this.ttView = new TreeTableView<>();

        ttView.setShowRoot(false);
        ttView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        initTreeTableView();
    }

    private void initTreeTableView() {
        colTournamentDate = new TreeTableColumn<>("Datum");
        colTournamentDate.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getTournamentDate()));

        colTournamentName = new TreeTableColumn<>("Name");
        colTournamentName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getTournamentName()));

        colTournamentLocation = new TreeTableColumn<>("Ort");
        colTournamentLocation.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getTournamentLocation()));

        colDiscipline = new TreeTableColumn<>("Disziplin");
        colDiscipline.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getDisciplineName()));

        colRoundName = new TreeTableColumn<>("Runde");
        colRoundName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getRoundName()));

        ttView.getColumns().addAll(colTournamentDate, colTournamentName, colTournamentLocation, colDiscipline, colRoundName);
    }

    public TreeTableView<PlayerMatchStatistics> getPlStatTreeView() {
        return ttView;
    }

    public void updateTreeTable(List<PlayerMatchStatistics> playerTournaments) {
        log.debugf("Update tree table view with %d entries", playerTournaments.size());
        //   var treeItemList = playerTournaments.stream().map(this::createTreeItem).toList();

        root.getChildren().clear();
        playerTournaments.forEach(ptm -> {
            var result = createTreeItem(ptm);
            root.getChildren().add(result);
        });

        ttView.setRoot(root);
    }

    private TreeItem<PlayerMatchStatistics> createTreeItemRoot() {
        var rOpsRoot = new PlayerMatchStatistics();
        return new TreeItem<>(rOpsRoot);
    }

    private TreeItem<PlayerMatchStatistics> createTreeItem(PlayerMatchStatistics ptm) {
        // root item
        TreeItem<PlayerMatchStatistics> parentTreeItem = new TreeItem<>(ptm);

        if (ptm.getMatchDetails().isEmpty()) {
            return parentTreeItem;
        }

        // create child tree items
        List<TreeItem<PlayerMatchStatistics>> childTreeItems = ptm.getMatchDetails().stream().map(TreeItem::new).toList();
        parentTreeItem.getChildren().addAll(childTreeItems);
        System.out.println(ptm.getMatchDetails());
        return parentTreeItem;
    }
}
