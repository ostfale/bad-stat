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

    private final TreeTableView<PlToStatDTO> ttView;
    private final TreeItem<PlToStatDTO> root;

    // declare tree table columns
    TreeTableColumn<PlToStatDTO, String> colTournamentDate;
    TreeTableColumn<PlToStatDTO, String> colTournamentName;
    TreeTableColumn<PlToStatDTO, String> colTournamentLocation;
    TreeTableColumn<PlToStatDTO, String> colDiscipline;

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

        ttView.getColumns().addAll(colTournamentDate, colTournamentName, colTournamentLocation, colDiscipline);
    }

    public TreeTableView<PlToStatDTO> getPlStatTreeView() {
        return ttView;
    }

    public void updateTreeTable(List<PlToStatDTO> playerTournaments) {
        log.debugf("Update tree table view with %d entries", playerTournaments.size());
        var treeItemList = playerTournaments.stream().map(this::createTreeItem).toList();
        root.getChildren().clear();
        root.getChildren().addAll(treeItemList);
        ttView.setRoot(root);
    }

    private TreeItem<PlToStatDTO> createTreeItemRoot() {
        var rOpsRoot = new PlToStatDTO();
        return new TreeItem<>(rOpsRoot);
    }

    private TreeItem<PlToStatDTO> createTreeItem(PlToStatDTO ptm) {
        // root item
        TreeItem<PlToStatDTO> parentTreeItem = new TreeItem<>(ptm);

        if (ptm.getMatchRows().isEmpty()) {
            return parentTreeItem;
        }

        // create child tree items
        List<TreeItem<PlToStatDTO>> childTreeItems = ptm.getMatchRows().stream().map(TreeItem::new).toList();
        parentTreeItem.getChildren().addAll(childTreeItems);
        return parentTreeItem;
    }
}
