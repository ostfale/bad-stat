package de.ostfale.qk.ui.tourcalendar;

import de.ostfale.qk.app.HostServicesProvider;
import de.ostfale.qk.domain.tourcal.filter.ViewRange;
import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.app.DataModel;
import de.ostfale.qk.ui.tourcalendar.model.TourCalUIModel;
import io.quarkiverse.fx.views.FxView;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

    private final BooleanProperty filterChanged = new SimpleBooleanProperty(false);

    @Inject
    TourCalService tourCalService;

    @Inject
    HostServicesProvider hostServicesProvider;

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
        calculateColSize();
        initDataModel();
        initTournamentsViewPortFilter();
        btnRefresh.disableProperty().bind(filterChanged.not());
    }

    private void initTournamentsViewPortFilter() {
        cbViewRange.getItems().addAll(ViewRange.values());
        cbViewRange.getSelectionModel().select(ViewRange.REMAINING);
        cbViewRange.valueProperty().addListener((observable, oldValue, newValue) -> {
            Log.debugf("Range filter changed from %s to %s", oldValue, newValue);
            if (oldValue != null && !oldValue.equals(newValue)) {
                filterChanged.set(true);
                Log.debugf("Range filter changed to %s", newValue);
            } else {
                filterChanged.set(false);
            }
        });
    }

    @FXML
    void refresh(ActionEvent event) {
     List<TourCalUIModel> rangeFilterResult=   tourCalService.updateRangeView(cbViewRange.getSelectionModel().getSelectedItem());
     update(rangeFilterResult);
     filterChanged.set(false);
    }

    public ViewRange getSelectedRange() {
        var selectedRange = cbViewRange.getSelectionModel().getSelectedItem();
        Log.debugf("Selected range is %s", selectedRange);
        return selectedRange;
    }

    private void calculateColSize() {
        colStartDate.prefWidthProperty().bind(tblCalTour.widthProperty().multiply(0.05));
        colClosingDate.prefWidthProperty().bind(tblCalTour.widthProperty().multiply(0.05));
        colTourName.prefWidthProperty().bind(tblCalTour.widthProperty().multiply(0.45));
        colTourCategory.prefWidthProperty().bind(tblCalTour.widthProperty().multiply(0.05));
        colTourLocation.prefWidthProperty().bind(tblCalTour.widthProperty().multiply(0.2));
        colTourOrganizer.prefWidthProperty().bind(tblCalTour.widthProperty().multiply(0.05));
        colWebLink.prefWidthProperty().bind(tblCalTour.widthProperty().multiply(0.05));
        colPdfLink.prefWidthProperty().bind(tblCalTour.widthProperty().multiply(0.05));
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
        colWebLink.setCellFactory(p -> new WebLinkTableCell(hostServicesProvider));
        colPdfLink.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().pdfLinkUrl()));
        colPdfLink.setCellFactory(p -> new PDFLinkTableCell(hostServicesProvider));

    }

    public void update(List<TourCalUIModel> tournaments) {
        dataModel.updateModel(tournaments, tblCalTour);
    }

    private void initDataModel() {
        dataModel = new DataModel<>();
    }
}
