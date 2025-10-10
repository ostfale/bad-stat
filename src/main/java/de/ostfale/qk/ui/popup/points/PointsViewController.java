package de.ostfale.qk.ui.popup.points;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.app.DataModel;
import io.quarkiverse.fx.views.FxView;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

@Singleton
@FxView("points-view")
public class PointsViewController extends BaseController<List<String>> {

    @Inject
    PointsViewService pointsViewService;

    private List<TableColumn<List<String>, String>> columns;

    @FXML
    private ComboBox<AgeClass> cbAgePoints;

    @FXML
    private TableColumn<List<String>, String> col01;

    @FXML
    private TableColumn<List<String>, String> col02;

    @FXML
    private TableColumn<List<String>, String> col03;

    @FXML
    private TableColumn<List<String>, String> col04;

    @FXML
    private TableColumn<List<String>, String> col05;

    @FXML
    private TableColumn<List<String>, String> col06;

    @FXML
    private TableColumn<List<String>, String> col07;

    @FXML
    private TableColumn<List<String>, String> col08;

    @FXML
    private TableColumn<List<String>, String> col09;

    @FXML
    private TableColumn<List<String>, String> col10;

    @FXML
    private TableColumn<List<String>, String> col11;

    @FXML
    private TableColumn<List<String>, String> col12;

    @FXML
    private TableColumn<List<String>, String> col13;

    @FXML
    private TableColumn<List<String>, String> col14;

    @FXML
    private TableColumn<List<String>, String> col15;

    @FXML
    private TableColumn<List<String>, String> col16;

    @FXML
    private TableColumn<List<String>, String> col17;

    @FXML
    private TableColumn<List<String>, String> col18;

    @FXML
    private TableColumn<List<String>, String> colIndex;

    @FXML
    private TableView<List<String>> tblPontView;

    @FXML
    public void initialize() {
        Log.debug("PointsViewController :: Initialize PointsViewController");
        initColumnList();
        initDataModel();
        initFilter();
        initTable();
    }

    private void initTable() {
        Log.debug("PointsViewController :: initialize table");
        colIndex.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getFirst()));
        col01.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));
        col02.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2)));
        col03.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3)));
        col04.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(4)));
        col05.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(5)));
        col06.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(6)));
        col07.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(7)));
        col08.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(8)));
        col09.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(9)));
        col10.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(10)));
        col11.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(11)));
        col12.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(12)));
        col13.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(13)));
        col14.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(14)));
        col15.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(15)));
        col16.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(16)));
        col17.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(17)));
        col18.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(18)));
        update(AgeClass.U9);
        cbAgePoints.getSelectionModel().select(AgeClass.U9);
    }

    private void initColumnList() {
        columns = List.of(colIndex, col01, col02, col03, col04, col05, col06, col07, col08, col09, col10,
                col11, col12, col13, col14, col15, col16, col17, col18);
    }

    private void initDataModel() {
        dataModel = new DataModel<>();
    }

    private void initFilter() {
        cbAgePoints.getItems().addAll(FXCollections.observableArrayList(AgeClass.getFilterPointsValues()));
        cbAgePoints.setOnAction(event -> {
            var ageClass = cbAgePoints.getSelectionModel().getSelectedItem();
            Log.debugf("TOURNAMENT :: Filter points for age class: {}", ageClass.name());
            update(ageClass);
        });
    }

    public void update(AgeClass ageClass) {
        updateColumnHeaders(ageClass);
        var pointList = pointsViewService.getPointValues(ageClass);
        dataModel.updateModel(pointList, tblPontView);
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
                currColumn.setText("");
            }
        }
    }
}





