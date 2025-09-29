package de.ostfale.qk.ui.popup.points;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.points.RankingPoint;
import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.app.DataModel;
import io.quarkiverse.fx.views.FxView;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

@Singleton
@FxView("points-view")
public class PointsViewController extends BaseController<List<RankingPoint>> {

    @Inject
    PointsViewService pointsViewService;

    private List<TableColumn<List<RankingPoint>, Integer>> columns;

    @FXML
    private ComboBox<AgeClass> cbAgePoints;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col01;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col02;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col03;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col04;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col05;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col06;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col07;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col08;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col09;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col10;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col11;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col12;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col13;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col14;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col15;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col16;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col17;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> col18;

    @FXML
    private TableColumn<List<RankingPoint>, Integer> colIndex;

    @FXML
    private TableView<List<RankingPoint>> tblPontView;

    @FXML
    public void initialize() {
        Log.debug("PointsViewController :: Initialize PointsViewController");
        initDataModel();
        initColumnList();
        initTable();
        initFilter();
    }

    public void updateColumValues(AgeClass selectedAgeClass) {
        Log.debugf("PointsViewController :: update values for age class %s", selectedAgeClass);
        var pointsList = pointsViewService.getPointValues(selectedAgeClass);
        dataModel.updateModel(pointsList, tblPontView);

    }

    private void initTable() {
        Log.debug("PointsViewController :: Init Table");
        colIndex.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getFirst().value()).asObject());
        col01.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(1).value()).asObject());
        col02.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(2).value()).asObject());
        col03.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(3).value()).asObject());
        col04.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(4).value()).asObject());
        col05.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(5).value()).asObject());
        col06.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(6).value()).asObject());
        col07.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(7).value()).asObject());
        col08.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(8).value()).asObject());
        col09.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(9).value()).asObject());
        col10.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(10).value()).asObject());
        col11.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(11).value()).asObject());
        col12.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(12).value()).asObject());
        col13.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(13).value()).asObject());
        col14.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(14).value()).asObject());
        col15.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(15).value()).asObject());
        col16.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(16).value()).asObject());
        col17.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(17).value()).asObject());
        col18.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().get(18).value()).asObject());

    }

    private void initFilter() {
        cbAgePoints.getItems().addAll(AgeClass.getFilterPointsValues());
        cbAgePoints.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Log.debugf("Age class changed to %s", newValue);
                updateColumnHeaders(newValue);
                updateColumValues(newValue);
            }
        });
        cbAgePoints.getSelectionModel().select(0);
    }

    private void updateColumnHeaders(AgeClass selectedAgeClass) {
        Log.debugf("PointsViewController :: update header for age class %s", selectedAgeClass);
        var cHeader = pointsViewService.getHeaderNames(selectedAgeClass);
        for (int i = 0; i < columns.size(); i++) {
            var currColumn = columns.get(i);
            if (i < cHeader.size()) {
                var headerName = cHeader.get(i);
                currColumn.setText(headerName);
                currColumn.setVisible(true);
            } else {
                currColumn.setVisible(false);
            }
        }
    }

    private void initColumnList() {
        columns = List.of(col01, col02, col03, col04, col05, col06, col07, col08, col09, col10,
                col11, col12, col13, col14, col15, col16, col17, col18);
    }

    private void initDataModel() {
        dataModel = new DataModel<>();
    }
}
