package de.ostfale.qk.ui.tourcalendar;

import de.ostfale.qk.domain.tourcal.filter.ViewRange;
import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.app.DataModel;
import de.ostfale.qk.ui.tourcalendar.model.TourCalUIModel;
import io.quarkiverse.fx.views.FxView;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

@Dependent
@FxView("tour-cal-view")
public class TourCalController extends BaseController<TourCalUIModel> {

    // declare buttons

    @FXML
    private Button btnRefresh;

    @FXML
    private ComboBox<ViewRange> cbViewRange;

    // table columns

    @FXML
    private TableColumn<TourCalUIModel, String> colClosingDate;

    @FXML
    private TableColumn<TourCalUIModel, String> colPdfLink;

    @FXML
    private TableColumn<TourCalUIModel, String> colStartDate;

    @FXML
    private TableColumn<TourCalUIModel, String> colTourCategory;

    @FXML
    private TableColumn<TourCalUIModel, String> colTourLocation;

    @FXML
    private TableColumn<TourCalUIModel, String> colTourName;

    @FXML
    private TableColumn<TourCalUIModel, String> colTourOrganizer;

    @FXML
    private TableColumn<TourCalUIModel, String> colWebLink;

    @FXML
    private TableView<TourCalUIModel> tblCalTour;

    @FXML
    public void initialize() {
        Log.debug("TourCalController :: Initialize TourCalController");
        initTable();
        initDataModel();
        initRangeFilter();
    }

    private void initRangeFilter() {
        cbViewRange.getItems().addAll(ViewRange.values());
        cbViewRange.getSelectionModel().select(ViewRange.REMAINING);
        cbViewRange.addEventHandler(ActionEvent.ACTION, event -> {
           Log.debugf("Range filter changed to %s", cbViewRange.getSelectionModel().getSelectedItem());
        });
    }

    @FXML
    void refresh(ActionEvent event) {

    }

    private void initTable() {
        Log.debug("TourCalController :: Initialize table");
        colStartDate.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().startDate()));
        colClosingDate.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().closedDate()));
        colTourName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().tournamentName()));
        colTourCategory.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().categoryName()));
        colTourLocation.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().location()));
        colTourOrganizer.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().organizer()));
        colWebLink.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().webLinkUrl()));
        colPdfLink.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().pdfLinkUrl()));
    }

    public void update(List<TourCalUIModel> tournaments) {
        dataModel.updateModel(tournaments, tblCalTour);
    }

    private void initDataModel() {
        dataModel = new DataModel<>();
    }
}
