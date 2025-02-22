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
    TreeTableColumn<PlayerMatchStatistics, String> colTPOne;
    TreeTableColumn<PlayerMatchStatistics, String> colTPTwo;
    TreeTableColumn<PlayerMatchStatistics, String> colMatchResult;

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
        colTournamentDate.setSortable(false);
        colTournamentDate.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getTournamentDate()));
        colTournamentDate.prefWidthProperty().bind(ttView.widthProperty().multiply(0.1));

        colTournamentName = new TreeTableColumn<>("Name");
        colTournamentName.setSortable(false);
        colTournamentName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getTournamentName()));
        colTournamentName.prefWidthProperty().bind(ttView.widthProperty().multiply(0.2));

        colTournamentLocation = new TreeTableColumn<>("Ort");
        colTournamentLocation.setSortable(false);
        colTournamentLocation.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getTournamentLocation()));
        colTournamentLocation.prefWidthProperty().bind(ttView.widthProperty().multiply(0.05));

        colDiscipline = new TreeTableColumn<>("Disziplin");
        colDiscipline.setSortable(false);
        colDiscipline.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getDisciplineName()));
        colDiscipline.prefWidthProperty().bind(ttView.widthProperty().multiply(0.05));

        colRoundName = new TreeTableColumn<>("Runde");
        colRoundName.setSortable(false);
        colRoundName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getRoundName()));
        colRoundName.prefWidthProperty().bind(ttView.widthProperty().multiply(0.05));

        colTPOne = new TreeTableColumn<>("Player / Team");
        colTPOne.setSortable(false);
        colTPOne.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getPtOneName()));
        colTPOne.prefWidthProperty().bind(ttView.widthProperty().multiply(0.15));

        colTPTwo = new TreeTableColumn<>("Player / Team");
        colTPTwo.setSortable(false);
        colTPTwo.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getPtTwoName()));
        colTPTwo.prefWidthProperty().bind(ttView.widthProperty().multiply(0.15));

        colMatchResult = new TreeTableColumn<>("Gespielte Sätze");
        colMatchResult.setSortable(false);
        colMatchResult.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getMatchResult()));
        colMatchResult.prefWidthProperty().bind(ttView.widthProperty().multiply(0.25));

        ttView.getColumns().addAll(colTournamentDate, colTournamentName, colTournamentLocation, colDiscipline, colRoundName, colTPOne, colTPTwo, colMatchResult);
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
