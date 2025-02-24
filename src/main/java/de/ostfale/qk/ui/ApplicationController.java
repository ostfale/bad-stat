package de.ostfale.qk.ui;

import de.ostfale.qk.ui.statistics.PlayerStatisticsHandler;
import de.ostfale.qk.ui.statistics.StatisticsController;
import io.quarkiverse.fx.views.FxView;
import io.quarkiverse.fx.views.FxViewData;
import io.quarkiverse.fx.views.FxViewRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jboss.logging.Logger;

@Singleton
@FxView("app-view")
public class ApplicationController {

    private static final Logger log = Logger.getLogger(ApplicationController.class);

    @FXML
    Parent root;

    @FXML
    private BorderPane bpApp;

    @FXML
    AnchorPane centerAnchorPane;


    @Inject
    FxViewRepository fxViewRepository;


    @Inject
    PlayerStatisticsHandler playerTourStatsHandler;


    @Inject
    StatisticsController statisticsController;

    @FXML
    public void initialize() {
        Stage dashboardStage = createStageWithScene();
        log.info("Dashboard stage successfully initialized and shown.");
        dashboardStage.show();
    }

    @FXML
    void showDashboardView(ActionEvent event) {
        log.info("Show Dashboard View");
        FxViewData playerGrid = fxViewRepository.getViewData("player-stat-info");
        bpApp.setCenter(playerGrid.getRootNode());
    }

    @FXML
    void showRankingView(ActionEvent event) {
        log.info("Show Ranking View");
    }

    @FXML
    void showStatisticsView(ActionEvent event) {
        log.info("Show Statistics View");
        bpApp.setCenter(statisticsController.getUI());
        playerTourStatsHandler.refreshUI();
    }

    @FXML
    void showTournaments(ActionEvent event) {
        log.info("Show Tournaments View");
    }

    @FXML
    void showFavorites(ActionEvent event) {
        log.info("Show Favorites View");
    }

    @FXML
    void closeApplication(ActionEvent event) {
        System.exit(0);
    }

    private Stage createStageWithScene() {
        final String STYLESHEET_PATH = "/static/css/app-view.css";
        final String ICON_PATH = "images/shuttle_logo.jpg";

        Stage stage = new Stage();
        stage.setTitle("Badminton Statistics");
        stage.getIcons().add(new Image(ICON_PATH));

        Scene scene = new Scene(this.root);
        scene.getStylesheets().add(STYLESHEET_PATH);
        stage.setScene(scene);
        return stage;
    }
}
