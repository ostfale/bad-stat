package de.ostfale.qk.ui.dashboard;

import de.ostfale.qk.ui.app.BaseController;
import de.ostfale.qk.ui.dashboard.model.DashboardRankingUIModel;
import de.ostfale.qk.ui.dashboard.model.DashboardUIModel;
import io.quarkiverse.fx.views.FxView;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public void initialize() {
        log.info("DashboardController initialized");
    }

    @FXML
    void downloadRankingFile(ActionEvent event) {
        log.info("DashboardController :: Download Ranking File");
        String rankingFileName = dashboardService.downloadRankingFile();
        if (!rankingFileName.isEmpty()) {
            log.debug("Download finished successfully -> update ranking file display");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String formattedDateTime = LocalDateTime.now().format(formatter);
            this.lblLastDownload.setText(formattedDateTime);
            this.lblLastFile.setText(rankingFileName);
        }
    }

    private void updateRankintDisplay(DashboardRankingUIModel model) {
        log.debug("DashBoardController :: Update ranking information");

        this.lblLastDownload.setText(model.getFileDownloadTimestamp());
        this.lblLastFile.setText(model.getDownloadFileName());
        this.lblAllPlayers.setText(String.valueOf(model.getNofPlayers()));
        this.lblFemalePlayers.setText(String.valueOf(model.getNofFemalePlayers()));
        this.lblMalePlayers.setText(String.valueOf(model.getNofMalePlayers()));
    }
}
