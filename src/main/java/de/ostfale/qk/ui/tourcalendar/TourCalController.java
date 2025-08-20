package de.ostfale.qk.ui.tourcalendar;

import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.tourcalendar.model.TourCalUIModel;
import io.quarkiverse.fx.views.FxView;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;
import javafx.fxml.FXML;

@Dependent
@FxView("tour-cal-view")
public class TourCalController extends BaseController<TourCalUIModel> {

    @FXML
    public void initialize() {
        Log.debug("TourCalController :: Initialize TourCalController");
    }
}
