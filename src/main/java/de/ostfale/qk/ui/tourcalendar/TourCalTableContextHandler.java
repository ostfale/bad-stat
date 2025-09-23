package de.ostfale.qk.ui.tourcalendar;

import de.ostfale.qk.ui.popup.points.PointsViewHandler;
import de.ostfale.qk.ui.popup.tourdetails.TourCalDetailsHandler;
import de.ostfale.qk.ui.tourcalendar.model.TourCalUIModel;
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

    @Inject
    PointsViewHandler agePointsHandler;

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
                    showTournamentDetails(row),
                    showPointsOverview(row)
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

    private MenuItem showPointsOverview(TableRow<TourCalUIModel> row) {
        Log.trace("TourCalTableContextHandler :: showPointsOverview");
        MenuItem menuItem = new MenuItem("Zeige Ranglistenpunkte");
        menuItem.setOnAction(e -> showPoints(row));
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

    private void showPoints(TableRow<TourCalUIModel> row) {
        Log.trace("TourCalTableContextHandler :: showPoints");
        AnchorPane root = (AnchorPane) agePointsHandler.getRootNode();
        Dialog<AnchorPane> dialog = new Dialog<>();
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.setTitle("Punktetabelle");
        dialog.getDialogPane().setContent(root);
        dialog.showAndWait();
    }
}
