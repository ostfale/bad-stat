package de.ostfale.qk.ui.tourcalendar.popup;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.tourcalendar.TourCalService;
import de.ostfale.qk.ui.tourcalendar.model.TourCalUIModel;
import io.quarkiverse.fx.views.FxView;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Map;
import java.util.Objects;

@Singleton
@FxView("tour-cal-details")
public class TourCalDetailsController extends BaseController<TourCalUIModel> {

    private static final Integer SINGLE_COL = 2;
    private static final Integer DOUBLE_COL = 3;
    private static final Integer MIXED_COL = 4;

    private static final Integer U9_ROW = 8;
    private static final Integer U11_ROW = 9;
    private static final Integer U13_ROW = 10;
    private static final Integer U15_ROW = 11;
    private static final Integer U17_ROW = 12;
    private static final Integer U19_ROW = 13;
    private static final Integer U22_ROW = 14;
    private static final Integer O19_ROW = 15;
    private static final Integer O35_ROW = 16;

    private static final Map<AgeClass, Integer> displayMap = Map.of(
            AgeClass.U9, U9_ROW,
            AgeClass.U11, U11_ROW,
            AgeClass.U13, U13_ROW,
            AgeClass.U15, U15_ROW,
            AgeClass.U17, U17_ROW,
            AgeClass.U19, U19_ROW,
            AgeClass.U22, U22_ROW,
            AgeClass.O19, O19_ROW,
            AgeClass.O35, O35_ROW
    );

    @Inject
    TourCalService tourCalService;

    @FXML
    private GridPane gpTournament;

    @FXML
    private Label lblCategory;

    @FXML
    private Label lblClosingDate;

    @FXML
    private Label lblCountry;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblLocation;

    @FXML
    private Label lblName;

    @FXML
    private Label lblOrganizer;

    @FXML
    public void initialize() {
        Log.debug("TourCalDetailsController :: Initialize TourCalDetailsController");
    }

    public void updateUI(TourCalUIModel model) {
        Log.debug("TourCalDetailsController :: Update details display");
        lblName.setText(model.tournamentName());
        lblDate.setText(model.startDate());
        lblClosingDate.setText(model.closedDate());
        lblLocation.setText(model.location());
        lblCountry.setText(model.countryCode());
        lblCategory.setText(model.categoryName());
        lblOrganizer.setText(model.organizer());
        updateAgeDisciplineInfo(model);
    }

    private void updateAgeDisciplineInfo(TourCalUIModel model) {
        model.ageClassDisciplines().forEach(ad -> {
            var rowIndex = displayMap.get(ad.ageClass());
            addToGridPane(ad.isSingle(), SINGLE_COL, rowIndex);
            addToGridPane(ad.isDouble(), DOUBLE_COL, rowIndex);
            addToGridPane(ad.isMixed(), MIXED_COL, rowIndex);
        });
    }

    private void addToGridPane(boolean isActive, int colNo, int rowNo) {
        var discLabel = createDisciplineActiveLabel(isActive);
        gpTournament.getChildren().removeIf(node -> Objects.equals(GridPane.getColumnIndex(node), colNo) && Objects.equals(GridPane.getRowIndex(node), rowNo));
        GridPane.setHalignment(discLabel, HPos.CENTER);
        gpTournament.add(discLabel, colNo, rowNo);
    }

    private Label createDisciplineActiveLabel(boolean disciplineActive) {
        var label = new Label();
        label.setText("");
        if (disciplineActive) {
            label.setGraphic(createFontIcon("fa-plus-circle", Color.GREEN));
        } else {
            label.setGraphic(createFontIcon("fa-minus-circle", Color.DARKRED));
        }
        return label;
    }

    private FontIcon createFontIcon(String literal, Color aColor) {
        var ic = new FontIcon(literal);
        ic.setIconSize(16);
        ic.setIconColor(aColor);
        return ic;
    }
}
