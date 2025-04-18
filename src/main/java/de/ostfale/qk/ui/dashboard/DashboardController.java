package de.ostfale.qk.ui.dashboard;

import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.dashboard.model.DashboardRankingUIModel;
import de.ostfale.qk.ui.dashboard.model.DashboardUIModel;
import io.quarkiverse.fx.views.FxView;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.jboss.logging.Logger;

@Dependent
@FxView("dashboard-view")
public class DashboardController extends BaseController<DashboardUIModel> {

    private static final Logger log = Logger.getLogger(DashboardController.class);

    @Inject
    DashboardService dashboardService;

    @FXML
    private AnchorPane apDashboard;

    @FXML
    private Button btnRankCurrCWDownload;

    @FXML
    private Button btnRankLastCWDownload;

    @FXML
    private Button btnRankRefresh;

    @FXML
    private Label lblAllPlayers;

    @FXML
    private Label lblFemalePlayers;

    @FXML
    private Label lblLastDownload;

    @FXML
    private Label lblLastFile;

    @FXML
    private Label lblMalePlayers;

    @FXML
    private Label lblRanking;

    @FXML
    private Label lblOnlineCurrCW;

    @FXML
    private Label lblLastCW;

    @FXML
    private Label lblStatCW;

    @FXML
    private Label lblCurrCW;

    @FXML
    public void initialize() {
        log.info("DashboardController initialized");
        var dashboardRankingUIModel = dashboardService.updateCurrentRankingStatus();
        updateRankingDisplay(dashboardRankingUIModel);
        lblCurrCW.setText("( " + dashboardService.getActualCalendarYear() + " KW " + dashboardService.getCurrentCW() + " )");
        lblLastCW.setText("KW " + dashboardService.getLastCW());
        lblOnlineCurrCW.setText("KW " + dashboardService.getOnlineCW());
    }

    @FXML
    void downloadCurrCWRanking(ActionEvent event) {
        log.info("DashboardController :: Download Current CW Ranking");
        var downloadSuccess = dashboardService.loadCurrentCWRankingFile();
        if (downloadSuccess) {
            log.debug("DashboardController :: Download Current CW Ranking successful");
            var dashboardRankingUIModel = dashboardService.updateCurrentRankingStatus();
            updateRankingDisplay(dashboardRankingUIModel);
        } else {
            log.warn("DashboardController :: Download Current CW Ranking failed");
        }
    }

    @FXML
    void downloadLastCWRanking(ActionEvent event) {
        log.debug("DashboardController :: Download Last CW Ranking");
        var downloadSuccess = dashboardService.loadLastCWRankingFile();
        if (downloadSuccess) {
            log.debug("DashboardController :: Download Last CW Ranking successful");
            var dashboardRankingUIModel = dashboardService.updateCurrentRankingStatus();
            updateRankingDisplay(dashboardRankingUIModel);
        } else {
            log.warn("DashboardController :: Download Last CW Ranking failed");
        }
    }

    @FXML
    void refreshFromLocalRanking(ActionEvent event) {
        log.debug("DashboardController :: Refresh from local ranking");
        // parse current ranking from local file
        DashboardRankingUIModel dashboardRankingUIModel = dashboardService.updateCurrentRankingFile();
        updateRankingDisplay(dashboardRankingUIModel);
    }

    private void updateRankingDisplay(DashboardRankingUIModel model) {
        log.debug("DashBoardController :: Update ranking information");
        this.lblLastDownload.setText(model.getFileDownloadTimestamp());
        this.lblLastFile.setText(model.getDownloadFileName());
        this.lblAllPlayers.setText(String.valueOf(model.getNofPlayers()));
        this.lblFemalePlayers.setText(String.valueOf(model.getNofFemalePlayers()));
        this.lblMalePlayers.setText(String.valueOf(model.getNofMalePlayers()));
        this.lblStatCW.setText(model.getDbUpdateInCW());
    }
}
