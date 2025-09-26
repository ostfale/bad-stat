package de.ostfale.qk.ui.popup.points;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.points.TourPointsViewModel;
import de.ostfale.qk.domain.points.TourTypePointsList;
import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.app.DataModel;
import io.quarkiverse.fx.views.FxView;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

@Singleton
@FxView("points-view")
public class PointsViewController extends BaseController<TourPointsViewModel> {

    @Inject
    PointsViewService pointsViewService;

    private List<TableColumn<TourPointsViewModel, Integer>> columns;

    @FXML
    private ComboBox<AgeClass> cbAgePoints;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col01;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col02;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col03;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col04;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col05;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col06;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col07;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col08;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col09;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col10;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col11;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col12;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col13;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col14;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col15;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col16;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col17;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> col18;

    @FXML
    private TableColumn<TourPointsViewModel, Integer> colIndex;

    @FXML
    private TableView<TourPointsViewModel> tblPontView;

    @FXML
    public void initialize() {
        Log.debug("PointsViewController :: Initialize PointsViewController");
        initColumnList();
        initFilter();
        initDataModel();
    }

    private void initFilter() {
        cbAgePoints.getItems().addAll(AgeClass.getFilterValues());
        cbAgePoints.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Log.debugf("Age class changed to %s", newValue);
                var pointData = pointsViewService.getTourPointsViewModelsForAgeClass(newValue);
                updateColumnHeaders(newValue, pointData);
            }
        });
        cbAgePoints.getSelectionModel().select(0);
    }

    private void updateColumnHeaders(AgeClass newValue, List<TourTypePointsList> pointsList) {
        columns.forEach(col -> col.setText(col.getId()));
    }

    private void initColumnList() {
        columns = List.of(col01, col02, col03, col04, col05, col06, col07, col08, col09, col10,
                col11, col12, col13, col14, col15, col16, col17, col18);
    }

    private void initDataModel() {
        dataModel = new DataModel<>();
    }
}
