package de.ostfale.qk.ui.dashboard;

import de.ostfale.qk.domain.tourcal.TourCalendarDashboard;
import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.dashboard.model.DashboardRankingUIModel;
import de.ostfale.qk.ui.dashboard.model.DashboardUIModel;
import io.quarkiverse.fx.RunOnFxThread;
import io.quarkiverse.fx.views.FxView;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;

@Dependent
@FxView("dashboard-view")
public class DashboardController extends BaseController<DashboardUIModel> {

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

    // tournament calendar

    @FXML
    private Label lblTourCalFileDate;

    @FXML
    private Button btnTourCalDownload;


    @FXML
    private ProgressIndicator progressIndicator;


    @FXML
    public void initialize() {
        Log.info("DashboardController initialized");
        var dashboardRankingUIModel = dashboardService.updateCurrentRankingStatus();
        updateRankingDisplay(dashboardRankingUIModel);
        lblCurrCW.setText("( " + dashboardService.getActualCalendarYear() + " KW " + dashboardService.getCurrentCW() + " )");
        lblLastCW.setText("KW " + dashboardService.getLastCW());
        lblOnlineCurrCW.setText("KW " + dashboardService.getOnlineCW());
        updateTournamentCalendarDisplay(dashboardService.getTourCalendarDashboardData());
    }

    @FXML
    void downloadCurrCWRanking(ActionEvent event) {
        Log.info("DashboardController :: Download Current CW Ranking");
        var downloadSuccess = dashboardService.loadCurrentCWRankingFile();
        if (downloadSuccess) {
            Log.debug("DashboardController :: Download Current CW Ranking successful");
            updateRankingDisplay(dashboardService.updateCurrentRankingStatus());
        } else {
            Log.warn("DashboardController :: Download Current CW Ranking failed");
        }
    }

    @FXML
    void downloadLastCWRanking(ActionEvent event) {
        Log.debug("DashboardController :: Download Last CW Ranking");
        var downloadSuccess = dashboardService.loadLastCWRankingFile();
        if (downloadSuccess) {
            Log.debug("DashboardController :: Download Last CW Ranking successful");
            updateRankingDisplay(dashboardService.updateCurrentRankingStatus());
        } else {
            Log.warn("DashboardController :: Download Last CW Ranking failed");
        }
    }

    @FXML
    void downloadTournamentCalendar(ActionEvent event) {
        Log.debug("DashboardController :: Download tournament calendar");
        showProgress();
        disableDownloadButtons(true);

        dashboardService.loadPlannedTournamentsAsync()
                .thenAccept(_ -> {
                    Log.debug("DashboardController :: Download tournament calendar successful");
                    hideProgress();
                    updateTournamentCalendarDisplay(dashboardService.getTourCalendarDashboardData());
                })
                .exceptionally(throwable -> {
                    Log.error("DashboardController :: Download tournament calendar failed", throwable);
                    hideProgress();
                    return null;
                });

    }

    @FXML
    void refreshFromLocalRanking(ActionEvent event) {
        Log.debug("DashboardController :: Refresh from local ranking");
        // parse current ranking from local file
        DashboardRankingUIModel dashboardRankingUIModel = dashboardService.updateCurrentRankingFile();
        updateRankingDisplay(dashboardRankingUIModel);
    }

    @RunOnFxThread
    public void updateTournamentCalendarDisplay(TourCalendarDashboard dashboardData) {
        Log.debug("DashBoardController :: Update tournament calendar information");
        lblTourCalFileDate.setText(dashboardData.downloadDate());
    }

    private void updateRankingDisplay(DashboardRankingUIModel model) {
        if (model == null) {
            Log.warn("DashBoardController :: Update ranking information failed. Model is null");
            return;
        }
        Log.debug("DashBoardController :: Update ranking information");
        this.lblLastDownload.setText(model.getFileDownloadTimestamp());
        this.lblLastFile.setText(model.getDownloadFileName());
        this.lblAllPlayers.setText(String.valueOf(model.getNofPlayers()));
        this.lblFemalePlayers.setText(String.valueOf(model.getNofFemalePlayers()));
        this.lblMalePlayers.setText(String.valueOf(model.getNofMalePlayers()));
        this.lblStatCW.setText(model.getDbUpdateInCW());
    }

    @RunOnFxThread
    void showProgress() {
        progressIndicator.setVisible(true);
        progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    }

    @RunOnFxThread
    void hideProgress() {
        progressIndicator.setVisible(false);
        disableDownloadButtons(false);
    }

    @RunOnFxThread
    void disableDownloadButtons(boolean disable) {
        btnTourCalDownload.setDisable(disable);
    }
}
