package de.ostfale.qk.ui.dashboard;


import de.ostfale.qk.ui.app.BaseController;
import io.quarkiverse.fx.views.FxView;
import jakarta.enterprise.context.Dependent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.jboss.logging.Logger;

@Dependent
@FxView("dashboard-view")
public class DashboardController extends BaseController<DashboardUIModel> {

    private static final Logger log = Logger.getLogger(DashboardController.class);

    @FXML
    AnchorPane apDashboard;


    @FXML
    public void initialize() {
        log.info("DashboardController initialized");
    }


    @FXML
    void downloadRankingFile(ActionEvent event) {
        log.info("Download Ranking File");
    }
}
