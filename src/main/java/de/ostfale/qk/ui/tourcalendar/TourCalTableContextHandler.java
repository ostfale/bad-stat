package de.ostfale.qk.ui.tourcalendar;

import de.ostfale.qk.ui.tourcalendar.model.TourCalUIModel;
import de.ostfale.qk.ui.tourcalendar.popup.TourCalDetailsHandler;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;


@ApplicationScoped
public class TourCalTableContextHandler {

    @Inject
    TourCalDetailsHandler tourCalDetailsHandler;

    private TableView<TourCalUIModel> tableView;

    public void setTableView(TableView<TourCalUIModel> tableView) {
        this.tableView = tableView;
    }

    public void initContextMenu() {
        Log.debug("TourCalTableContextHandler :: Init context menu");
        tableView.setRowFactory(tv -> {
            ContextMenu contextMenu = new ContextMenu();
            final TableRow<TourCalUIModel> row = new TableRow<>();
            contextMenu.getItems().addAll(
                    showTournamentDetails(row)
            );
            row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
            return row;
        });
    }

    private MenuItem showTournamentDetails(TableRow<TourCalUIModel> row) {
        Log.trace("TourCalTableContextHandler :: showTournamentDetails");
        MenuItem menuItem = new MenuItem("Zeige Turnier Details");
        menuItem.setOnAction(e -> showDetailsInPopup(row.getItem()));
        return menuItem;
    }

    private void showDetailsInPopup(TourCalUIModel model) {
        Log.trace("TourCalTableContextHandler :: showDetailsInPopup");
        var rootUi = tourCalDetailsHandler.getRootNode();
        tourCalDetailsHandler.getController().updateUI(model);
        Alert tournamentInfoDialog = new Alert(Alert.AlertType.INFORMATION);
        tournamentInfoDialog.setTitle("Turnier Details");
        tournamentInfoDialog.setHeaderText("Zeige alle Daten für das ausgewählte Turnier");
        AnchorPane anchorPane = new AnchorPane(rootUi);
        AnchorPane.setLeftAnchor(anchorPane, 5.0);
        AnchorPane.setTopAnchor(anchorPane, 5.0);
        tournamentInfoDialog.getDialogPane().setContent(anchorPane);
        tournamentInfoDialog.showAndWait();
    }
}
