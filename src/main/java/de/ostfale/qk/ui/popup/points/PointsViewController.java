package de.ostfale.qk.ui.popup.points;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.points.TourPointsViewModel;
import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.app.DataModel;
import io.quarkiverse.fx.views.FxView;
import io.quarkus.logging.Log;
import jakarta.inject.Singleton;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

@Singleton
@FxView("points-view")
public class PointsViewController extends BaseController<TourPointsViewModel> {

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
        initDataModel();
    }

    private void initDataModel() {
        dataModel = new DataModel<>();
    }
}
